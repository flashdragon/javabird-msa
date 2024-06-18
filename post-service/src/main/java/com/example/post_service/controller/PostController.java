package com.example.post_service.controller;

import com.example.post_service.dto.PostDto;
import com.example.post_service.jpa.PostEntity;
import com.example.post_service.service.PostService;
import com.example.post_service.utils.ApiUtils.ApiResult;
import com.example.post_service.vo.RequestPost;
import com.example.post_service.vo.ResponsePost;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.post_service.utils.ApiUtils.success;
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/")
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public ApiResult<ResponsePost> createPost(@ModelAttribute RequestPost post, @RequestHeader("userId") String userId) throws IOException {
        log.info("Post /posts");
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        PostDto postDto = mapper.map(post, PostDto.class);
        postDto.setUserId(userId);
        postService.createPost(postDto);
        ResponsePost responsePost = mapper.map(postDto, ResponsePost.class);
        return success(responsePost);
    }

    @GetMapping("/posts")
    public ApiResult<List<ResponsePost>> getPosts() {
        Iterable<PostEntity> postList = postService.getPostByAll();
        List<ResponsePost> result = new ArrayList<>();
        postList.forEach(v -> {
            result.add(new ModelMapper().map(v, ResponsePost.class));
        });
        return success(result);
    }

    @GetMapping("/posts/{postId}")
    public ApiResult<ResponsePost> getPost(@PathVariable("postId") String postId) {
        PostDto postDto = postService.getPostByPostId(postId);
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ResponsePost responsePost = mapper.map(postDto, ResponsePost.class);
        return success(responsePost);
    }

    @DeleteMapping("/posts/{postId}")
    public ApiResult<Boolean> deletePost(@PathVariable("postId") String postId, @RequestHeader("userId") String userId) {
        return success(postService.deleteByPostIdAndUserId(postId, userId));
    }

}
