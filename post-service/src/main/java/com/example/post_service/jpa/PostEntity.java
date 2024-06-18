package com.example.post_service.jpa;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "posts")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String postId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String imageName;

    @Column(nullable = false, unique = true)
    private String storedName;

    private String contents;
}
