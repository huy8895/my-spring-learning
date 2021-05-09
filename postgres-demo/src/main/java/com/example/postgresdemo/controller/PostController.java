package com.example.postgresdemo.controller;

import com.example.postgresdemo.dao.PostDAO;
import com.example.postgresdemo.dto.PostRepository;
import com.example.postgresdemo.entity.Post;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(("/posts"))
public class PostController {
    private final PostDAO postDAO;
    private final PostRepository postRepository;

    public PostController(PostDAO postDAO, PostRepository postRepository) {
        this.postDAO = postDAO;
        this.postRepository = postRepository;
    }

    @PostMapping
    public Object createPost(@RequestBody Post post){
        return postRepository.save(post);
    }

    @GetMapping
    public Object getPosts() {
        return postDAO.getPosts();
    }

    @GetMapping("/tuple")
    public Object getPostsByTuple() {
        return postDAO.getPostByTuple();
    }

}
