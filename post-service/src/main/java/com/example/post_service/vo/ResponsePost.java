package com.example.post_service.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponsePost {
    private String imageName;
    private String storedName;
    private String contents;
    private String postId;
    private String userId;
    private int like;
}
