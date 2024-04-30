package com.app.twitter.controller;

import com.app.twitter.dto.comment.CommentRequestDto;
import com.app.twitter.dto.comment.CommentUpdateDto;
import com.app.twitter.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity<Object> saveComment(@RequestBody CommentRequestDto commentRequestDto) {
        return commentService.saveComment(commentRequestDto);
    }

    @GetMapping("/comments")
    public ResponseEntity<Object> getAllComments() {
        return commentService.getAllComments();
    }

    @GetMapping("/comment")
    public ResponseEntity<Object> getCommentById(@RequestParam(name = "commentID") Long id) {
        return commentService.getCommentById(id);
    }


    @PatchMapping("/comment")
    public ResponseEntity<Object> updateComment(@RequestBody CommentUpdateDto commentUpdateDto) {
        return commentService.updateComment(commentUpdateDto);
    }

    @DeleteMapping("/comment")
    public ResponseEntity<Object> deleteComment(@RequestParam(name = "commentID") Long id) {
        return commentService.deleteComment(id);
    }
}
