package com.example.post_service.vo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RequestPost {
    private MultipartFile multipartFile;
    private String contents;
}
