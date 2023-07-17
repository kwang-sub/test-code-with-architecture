package com.example.demo.post.controller.response;

import com.example.demo.post.domain.Post;
import com.example.demo.user.controller.response.UserResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponse {

    private Long id;
    private String content;
    private Long createdAt;
    private Long modifiedAt;
    private UserResponse writer;

    public static PostResponse toResponse(Post post) {
        PostResponse PostResponse = new PostResponse();
        PostResponse.setId(post.getId());
        PostResponse.setContent(post.getContent());
        PostResponse.setCreatedAt(post.getCreatedAt());
        PostResponse.setModifiedAt(post.getModifiedAt());
        PostResponse.setWriter(UserResponse.toResponse(post.getWriter()));
        return PostResponse;
    }
}
