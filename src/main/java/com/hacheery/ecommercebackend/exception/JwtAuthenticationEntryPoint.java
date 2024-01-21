package com.hacheery.ecommercebackend.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // Xử lý việc trả về một phản hồi JSON cho client khi token hết hạn hoặc xác thực không thành công.
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // HTTP 401 Unauthorized

        // Tạo một đối tượng JSON chứa thông báo lỗi
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Token Expired"); // Thay đổi thông báo theo ý muốn

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(), responseBody);
    }
}
