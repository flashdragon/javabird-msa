package com.example.post_service.jpa;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;


public interface PostRepository extends CrudRepository<PostEntity, Long> {
    PostEntity findByPostId(String postId);

    @Transactional
    @Modifying
    @Query("DELETE FROM PostEntity p WHERE p.postId = :postId AND p.userId = :userId")
    int deleteByPostIdAndUserId(String postId, String userId);

    Iterable<PostEntity> findByUserId(String userId);

}
