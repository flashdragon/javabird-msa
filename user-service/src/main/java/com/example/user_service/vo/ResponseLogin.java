package com.example.user_service.vo;

import lombok.Data;

@Data
public class ResponseLogin {
    private String token;
    private String userId;

    public ResponseLogin (String token, String userId) {
        this.token = token;
        this.userId = userId;
    }
}
