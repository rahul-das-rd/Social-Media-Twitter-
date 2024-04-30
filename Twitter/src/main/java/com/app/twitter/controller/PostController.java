package com.app.twitter.controller;

import com.app.twitter.dto.post.PostRequestDto;
import com.app.twitter.dto.post.PostUpdateDto;
import com.app.twitter.model.Post;
import com.app.twitter.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;

    @GetMapping("/")
    public ResponseEntity<Object> getPosts() {
        return postService.getAllPosts();
    }

    @PostMapping("/post")
    public ResponseEntity<Object> savePost(@RequestBody PostRequestDto postRequestDto) {
        return postService.savePost(postRequestDto);
    }

    @PatchMapping("/post")
    public ResponseEntity<Object> updatePost(@RequestBody PostUpdateDto postUpdateDto) {
        return postService.updatePost(postUpdateDto);
    }

    @GetMapping("/post")
    public ResponseEntity<?> getPostById(@RequestParam(name = "postID") Long id) {
        return postService.getPostById(id);
    }

    @DeleteMapping("/post")
    public ResponseEntity<Object> deletePost(@RequestParam(name = "postID") Long id) {
        return postService.deletePost(id);
    }


}
