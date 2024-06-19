package com.example.user_service.client;

import com.example.user_service.utils.ApiUtils.ApiResult;
import com.example.user_service.vo.ResponsePost;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="post-service")
public interface PostServiceClient {

    @GetMapping("/posts/users/{userId}")
    ApiResult<List<ResponsePost>> getPosts(@PathVariable("userId")String userId);
}
