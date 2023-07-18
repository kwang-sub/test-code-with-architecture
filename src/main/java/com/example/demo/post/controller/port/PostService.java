package com.example.demo.post.controller.port;

import com.example.demo.common.sevice.port.ClockHolder;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;

public interface PostService {
    Post getPostById(long id);

    Post create(PostCreate postCreate, ClockHolder clockHolder);

    Post update(long id, PostUpdate postUpdate);
}
