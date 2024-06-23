package com.example.post_service.service;

import com.example.post_service.dto.PostDto;
import com.example.post_service.file.S3FileStore;
import com.example.post_service.jpa.PostEntity;
import com.example.post_service.jpa.PostRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final S3FileStore s3FileStore;

    @Override
    public PostDto createPost(PostDto postDto) throws IOException {
        postDto.setPostId(UUID.randomUUID().toString());
        s3FileStore.storeFile(postDto);
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        PostEntity postEntity = mapper.map(postDto, PostEntity.class);
        postEntity.setLikeCount(0);
        postRepository.save(postEntity);

        return postDto;
    }

    @Override
    public Iterable<PostEntity> getPostByAll() {
        return postRepository.findAll();
    }

    @Override
    public PostDto getPostByPostId(String postId) {
        PostEntity postEntity = postRepository.findByPostId(postId);
        if (postEntity == null)
            throw new NotFoundException("Post not found");
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        PostDto postDto = mapper.map(postEntity, PostDto.class);
        return postDto;
    }

    @Override
    public boolean deleteByPostIdAndUserId(String postId, String userId) {
        int deletedCount = postRepository.deleteByPostIdAndUserId(postId, userId);
        if (deletedCount == 0) {
            throw new RuntimeException("Post not found or you do not have permission to delete this post");
        }
        log.info("Post with ID {} deleted successfully", postId);
        return true;
    }

    @Override
    public Iterable<PostEntity> getPostByUserId(String userId) {
        return postRepository.findByUserId(userId);
    }

    @Override
    public boolean likePost(String postId, String userId) {
        PostEntity postEntity = postRepository.findByPostId(postId);
        postEntity.setLikeCount(postEntity.getLikeCount() + 1);
        return true;
    }
}
