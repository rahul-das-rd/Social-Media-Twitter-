package com.app.twitter.service;

import com.app.twitter.dto.comment.CommentRequestDto;
import com.app.twitter.dto.comment.CommentResponseDto;
import com.app.twitter.dto.comment.CommentUpdateDto;
import com.app.twitter.dto.error.ErrorResponse;
import com.app.twitter.model.Comment;
import com.app.twitter.model.Post;
import com.app.twitter.model.User;
import com.app.twitter.repository.CommentRepository;
import com.app.twitter.repository.PostRepository;
import com.app.twitter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public ResponseEntity<Object> getAllComments() {
        return new ResponseEntity<>(commentRepository.findAll(), HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<Object> deleteComment(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isEmpty()) {
            return ResponseEntity.ok(new ErrorResponse("Comment does not exist"));
        } else {
            comment.get().setCommentCreator(null);


            //Break the relationship from post side as well.

            if (comment.get().getCommentPost() != null && comment.get().getCommentPost().getComments() != null) {
                comment.get().getCommentPost().getComments().remove(comment.get());
                postRepository.save(comment.get().getCommentPost());
            }
            comment.get().setCommentPost(null);
            commentRepository.save(comment.get());

            commentRepository.deleteById(id);

            if (commentRepository.findById(id).isPresent()) {
                //If that comment still exists
                log.warn("Comment with id {} was not deleted from database", id);
                commentRepository.delete(comment.get());
            }
            return ResponseEntity.ok("Comment deleted successfully");
        }
    }

    public ResponseEntity<Object> getCommentById(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isEmpty()) {
            return ResponseEntity.ok(new ErrorResponse("Comment does not exist"));
        } else {
            CommentResponseDto commentResponseDto = new CommentResponseDto();
            commentResponseDto.setCommentId(comment.get().getCommentID());
            commentResponseDto.setCommentBody(comment.get().getCommentBody());
            if (comment.get().getCommentCreator() != null) {
                commentResponseDto.setCommentCreator(comment.get().getCommentCreatorDTO());
            } else {
                commentRepository.deleteById(id);
                return ResponseEntity.ok(new ErrorResponse("Comment does not exist"));
            }
            return ResponseEntity.ok(commentResponseDto);
        }
    }

    public ResponseEntity<Object> saveComment(CommentRequestDto commentRequestDto) {
        Optional<User> user = userRepository.findById(commentRequestDto.getUserID());
        if (user.isEmpty()) {
            return ResponseEntity.ok(new ErrorResponse("User does not exist"));
        } else {
            Optional<Post> post = postRepository.findById(commentRequestDto.getPostID());
            if (post.isEmpty()) {
                return ResponseEntity.ok(new ErrorResponse("Post does not exist"));
            } else {
                Comment comment = new Comment();
                comment.setCommentBody(commentRequestDto.getCommentBody());
                comment.setCommentCreator(user.get());
                comment.setCommentPost(post.get());
                Comment savedComment = commentRepository.save(comment);
                post.get().getComments().add(savedComment);
                postRepository.save(post.get());
                return ResponseEntity.ok("Comment created successfully");
            }
        }
    }

    public ResponseEntity<Object> updateComment(CommentUpdateDto commentUpdateDto) {
        Optional<Comment> comment = commentRepository.findById(commentUpdateDto.getCommentID());
        if (comment.isEmpty()) {
            return ResponseEntity.ok(new ErrorResponse("Comment does not exist"));
        }
        comment.get().setCommentBody(commentUpdateDto.getCommentBody());
        commentRepository.save(comment.get());
        return ResponseEntity.ok("Comment edited successfully");
    }

}
