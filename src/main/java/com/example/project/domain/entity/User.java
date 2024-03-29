package com.example.project.domain.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Setter
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Builder
@ToString
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private LocalDateTime deletedAt;
    @Column(nullable = false)
    private String password;

    @CreatedDate
//    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
//    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    @Column(updatable = false)
    private String createdAt;

    @LastModifiedDate
    private String lastModifiedAt;

//2023-01-10 10:39:06.677143
// 2023-01-10 10:50:26
    @PrePersist
//      String으로 바꾸기
    public void onPrePersist(){
        this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.lastModifiedAt = this.createdAt;
    }

    @PreUpdate
    public void onPreUpdatePersist(){
        this.lastModifiedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }


    @Enumerated(EnumType.STRING)
//    @Column(name="role")
    private UserRole role = UserRole.USER;


    @Column(nullable = false)
    private String userName;


}

