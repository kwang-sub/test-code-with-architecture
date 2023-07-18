package com.example.demo.post.service;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.common.sevice.port.ClockHolder;
import com.example.demo.post.controller.port.PostService;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.post.service.port.PostRepository;
import com.example.demo.user.domain.User;
import com.example.demo.user.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserServiceImpl userServiceImpl;

    @Override
    public Post getPostById(long id) {
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Posts", id));
    }

    @Override
    public Post create(PostCreate postCreate, ClockHolder clockHolder) {
        User user = userServiceImpl.getById(postCreate.getWriterId());
        Post post = new Post();
        post.setWriter(user);
        post.setContent(postCreate.getContent());
        post.setCreatedAt(clockHolder.millis());
        return postRepository.save(post);
    }

    @Override
    public Post update(long id, PostUpdate postUpdate) {
        Post post = getPostById(id);
        post.setContent(postUpdate.getContent());
        post.setModifiedAt(Clock.systemUTC().millis());
        return postRepository.save(post);
    }
}