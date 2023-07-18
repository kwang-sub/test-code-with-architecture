package com.example.demo.medium.response;

import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PostResponseTest {

    @Test
    void 변환_테스트() {
        // given
        User user = User.builder()
                .id(1L)
                .status(UserStatus.ACTIVE)
                .lastLoginAt(1L)
                .email("kwang@mail.com")
                .address("Seoul")
                .nickname("kwang")
                .build();

        Post post = new Post();
        post.setContent("test");
        post.setWriter(user);

        // when
        PostResponse postResponse = PostResponse.toResponse(post);

        // then
        Assertions.assertThat(postResponse.getContent()).isEqualTo("test");
    }
}