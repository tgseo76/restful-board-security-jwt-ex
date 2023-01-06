package com.example.project.domain.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.parameters.P;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime LastModifiedAt;
    private String body;
    private String title;

    @ManyToOne
    private User user;

    public static Post of(String title, String body, User user) {
        Post entity = new Post();
        entity.setTitle(title);
        entity.setBody(body);
        entity.setUser(user);
        return entity;
    }


}
