package com.hacheery.ecommercebackend.service.impl;

import com.hacheery.ecommercebackend.service.UploadService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class UploadServiceImpl implements UploadService {
    private final String IMGUR_API_URL = "https://api.imgur.com/3/image";
    private final String CLIENT_ID = "c09ead16268e17f";

    private final String BEAR_TOKEN = "126112ac41fb6dc2177d8b29b30329cf77339526";

    public String uploadImage(byte[] imageBytes) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Client-ID " + CLIENT_ID);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", new ByteArrayResource(imageBytes));
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(IMGUR_API_URL, requestEntity, Map.class);
            if(response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map data = response.getBody();
                return data.get("data").toString();
            } else {
                throw new RuntimeException("Image upload to Imgur failed");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
