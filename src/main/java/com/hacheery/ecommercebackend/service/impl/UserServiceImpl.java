package com.hacheery.ecommercebackend.service.impl;

import com.hacheery.ecommercebackend.exception.DuplicateException;
import com.hacheery.ecommercebackend.exception.ResourceNotFoundException;
import com.hacheery.ecommercebackend.exception.SQLException;
import com.hacheery.ecommercebackend.model.CurrentUser;
import com.hacheery.ecommercebackend.payload.request.UserRequest;
import com.hacheery.ecommercebackend.security.entity.Role;
import com.hacheery.ecommercebackend.security.entity.User;
import com.hacheery.ecommercebackend.security.repository.UserRepository;
import com.hacheery.ecommercebackend.service.UserService;
import com.hacheery.ecommercebackend.specification.UserSpecification;
import io.micrometer.common.util.StringUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UploadServiceImpl uploadService;
    private final JavaMailSender mailSender;
    @Override
    @Transactional
    public User createUser(@CurrentUser User currentUser, User user, MultipartFile file) {
        Objects.requireNonNull(user, "Thông tin về user không được để trống");
        System.out.println(currentUser.getRole());
        if(currentUser.getRole() != Role.ADMIN) throw new AccessDeniedException("Bạn không có quyền tạo user mới");
        if (StringUtils.isBlank(user.getUsername()))
            throw new IllegalArgumentException("Tên user không được để trống");
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new DuplicateException("Username này đã được sử dụng");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateException("Email này đã được sử dụng");
        }
        if(userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new DuplicateException("Số điện thoại này đã được sử dụng");
        }
        try {
            String imageInformation = uploadService.uploadImage(file.getBytes());
            String[] parts = imageInformation.split(",");
            String imageUrl = parts[parts.length-1].split("=")[1].trim();
            imageUrl = imageUrl.substring(0, imageUrl.length()-1);
            user.setImgUrl(imageUrl);
            User newUser = userRepository.save(user);
            sendVerificationEmail(user);
            return newUser;
        } catch (DataIntegrityViolationException e) {
            throw new SQLException("Lỗi lưu danh mục vào cơ sở dữ liệu", e);
        } catch (IOException | MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<User> getUsers(UserRequest userRequest, Pageable pageable) {
        Specification<User> spec = UserSpecification.searchByParameter(
                userRequest
        );
        return userRepository.findAll(spec, pageable);
    }

    @Override
    public User getUserById(Long userId) {
        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        return null;
    }

    @Override
    public void updateUser(@CurrentUser User currentUser,Long userId, User updatedUser) {
        if(currentUser.getId().equals(userId) || currentUser.getRole() == Role.ADMIN) {
            User existingUser = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "ID", userId));

            // Cập nhật thông tin người dùng
            if(!updatedUser.getName().isEmpty()) existingUser.setName(updatedUser.getName());
            if(!updatedUser.getAddress().isEmpty()) existingUser.setAddress(updatedUser.getAddress());
            existingUser.setDateOfBirth(updatedUser.getDateOfBirth());
            String allCountryRegex = "^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$";
            if(String.valueOf(updatedUser.getPhoneNumber()).matches(allCountryRegex)) {
                existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
            }
            if(updatedUser.getRole() != null) existingUser.setRole(updatedUser.getRole());
            userRepository.save(existingUser);
        } else throw new AuthorizationServiceException("Bạn không có quyền hạn để sửa thông tin user");
    }

    @Override
    public void deleteUser(Long userId) {

    }

    @Override
    public void deleteUsers() {
        userRepository.deleteAll();
    }

    private void sendVerificationEmail(User user)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "Your email address";
        String senderName = "Your company name";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Your company name.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getName());
        String verifyURL = "localhost:8080" + "/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

    }
}
