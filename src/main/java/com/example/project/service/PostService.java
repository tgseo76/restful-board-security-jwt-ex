package com.example.project.service;

import com.example.project.domain.dto.Post.PostReadResponse;
import com.example.project.domain.dto.Post.PostResponse;
import com.example.project.domain.entity.Post;
import com.example.project.domain.entity.User;
import com.example.project.exception.ErrorCode;
import com.example.project.exception.MyException;
import com.example.project.repository.PostRepository;
import com.example.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
//@EnableJpaAuditing
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    //게시판 작성
    public PostResponse addPost(String title, String body, String name) {
        User userEntity = userRepository.findByUserName(name).orElseThrow(() -> new MyException(ErrorCode.USERNAME_NOT_FOUND, name));
//        if(userRepository.findByUserName(name).isPresent()){
//            User userentity = userRepository.findByUserName(name);
//        } else {
//            throw new MyException(ErrorCode.USERNAME_NOT_FOUND,name);
//
//        }

        //entity로 바꾸기
        Post post = Post.builder()
                .title(title)
                .body(body)
                .user(userEntity)
                .build();
        postRepository.save(post);

        //dto로 바꾸기
        PostResponse postResponse = PostResponse.builder()
                .postId(post.id)
                .message("포스트 등록 완료")
                .build();

        return postResponse;
    }

    //삭제
    public PostResponse delPost(Long id, String name) {

        Long checkId = userService.getUserByUserName(name).getUserId();
        Long inputId = getPostById(id).getUser().getUserId();

        if (checkId!=inputId) {
            throw new MyException(ErrorCode.Post_NOT_FOUND, "post id 없음");
        }
        postRepository.deleteById(id);
        PostResponse postResponse = new PostResponse("포스트 삭제 완료", id);

        return postResponse;
    }

    //1개 상세조회
    public PostReadResponse readPost(Long id) {
//        Post post = postRepository.findById(id).orElseThrow(()-> new MyException(ErrorCode.Post_NOT_FOUND,""));
        Post post = getPostById(id);

        PostReadResponse postReadResponse = PostReadResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .body(post.getBody())
                .userName(post.getUser().getUserName())
                .createdAt(post.getCreatedAt())
                .lastModifiedAt(post.getLastModifiedAt())
                .build();

        return postReadResponse;
    }

    //수정
    public PostResponse updatePost(Long id, String title, String body, String name) {
        Long checkId = userService.getUserByUserName(name).getUserId();
        Long inputId = getPostById(id).getUser().getUserId();

        if (checkId!=inputId) {
            throw new MyException(ErrorCode.Post_NOT_FOUND, "post id 없음");
        }

        Post post = getPostById(id);

            post.setTitle(title);
        post.setBody(body);
        postRepository.save(post);

        PostResponse postResponse = PostResponse.builder()
                .postId(post.id)
                .message("포스트 수정 완료")
                .build();

        return postResponse;
    }


    // post_id 있나 확인
    public Post getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new MyException(ErrorCode.Post_NOT_FOUND, "post id 없음"));
        return post;
//        return userRepository.findByUserName(userName)
//                .orElseThrow(() -> new MyException(ErrorCode.USERNAME_NOT_FOUND, ""));

    }


    public Page<PostReadResponse> listPost(Pageable pageable) {
        Page<Post> posts= postRepository.findAll(pageable);
        Page<PostReadResponse> postResponses=PostReadResponse.toDtoList(posts);
        return postResponses;

    }
}
