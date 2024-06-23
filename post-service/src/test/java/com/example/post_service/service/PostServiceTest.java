package com.example.post_service.service;

import com.example.post_service.jpa.PostEntity;
import com.example.post_service.jpa.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PostServiceTest {
    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;

    private Long postId;

    @BeforeEach
    public void init() {
        PostEntity post = new PostEntity();
        post.setPostId("testPost");
        post.setUserId("testUser");
        post.setImageName("testImage");
        post.setStoredName("testStoredImage");
        post.setContents("testContents");
        post.setLikeCount(0);
        postRepository.save(post);
        postId = post.getId();
    }


    @Test
    public void testConcurrentLikes() throws InterruptedException {
        int numberOfThreads = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    postService.likePost("testPost", "testUser");
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 스레드가 완료될 때까지 대기

        PostEntity post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        assertEquals(numberOfThreads, post.getLikeCount());
    }
}
