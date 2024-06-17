package com.example.post_service.service;

import com.example.post_service.dto.PostDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{
    @Override
    public PostDto createPost(PostDto postDto) {
        return null;
    }
}
