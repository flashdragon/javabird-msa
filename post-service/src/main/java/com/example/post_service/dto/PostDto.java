package com.example.post_service.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PostDto {
    private MultipartFile multipartFile;
    private String imageName;
    private String storedName;
    private String postId;
    private String userId;
    private String contents;
}
