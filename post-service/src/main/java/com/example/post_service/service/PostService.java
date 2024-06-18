package com.example.post_service.service;

import com.example.post_service.dto.PostDto;
import com.example.post_service.jpa.PostEntity;

import java.io.IOException;

public interface PostService {
    PostDto createPost(PostDto postDto) throws IOException;

    Iterable<PostEntity> getPostByAll();

    PostDto getPostByPostId(String postId);

    boolean deleteByPostIdAndUserId(String postId, String userId);
}
