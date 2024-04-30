package com.app.twitter.service;

import com.app.twitter.dto.comment.CommentResponseDto;
import com.app.twitter.dto.error.ErrorResponse;
import com.app.twitter.dto.post.PostRequestDto;
import com.app.twitter.dto.post.PostResponseDto;
import com.app.twitter.dto.post.PostUpdateDto;
import com.app.twitter.model.Comment;
import com.app.twitter.model.Post;
import com.app.twitter.model.User;
import com.app.twitter.repository.CommentRepository;
import com.app.twitter.repository.PostRepository;
import com.app.twitter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public ResponseEntity<Object> getAllPosts() {
        List<Post> postsLists= postRepository.findAllByOrderByPostIDDesc();
        return new ResponseEntity<>(postsLists, HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<Object> savePost(PostRequestDto postRequestDto) {
        Optional<User> user = userRepository.findById(postRequestDto.getUserID());
        if (user.isEmpty()) {
            return ResponseEntity.ok(new ErrorResponse("User does not exist"));
        }
        Post post = new Post();
        post.setPostBody(postRequestDto.getPostBody());
        post.setDate(LocalDate.now());
        post.setPostCreator(user.get());
        postRepository.save(post);

        if (user.get().getPosts() == null) {
            user.get().setPosts(new ArrayList<>());
        }
        user.get().getPosts().add(post);
        userRepository.save(user.get());
        return ResponseEntity.ok("Post created successfully");
    }


    public ResponseEntity<?> getPostById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            return ResponseEntity.ok(new ErrorResponse("Post does not exist"));
        }

        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        PostResponseDto postResponseDto = new PostResponseDto();
        postResponseDto.setPostBody(post.get().getPostBody());
        postResponseDto.setPostID(post.get().getPostID());
        postResponseDto.setDate(post.get().getDate());
        for (Comment comment : post.get().getComments()) {
            CommentResponseDto commentResponseDto = new CommentResponseDto();
            commentResponseDto.setCommentBody(comment.getCommentBody());
            commentResponseDto.setCommentId(comment.getCommentID());
            if (comment.getCommentCreator() != null) {
                commentResponseDto.setCommentCreator(comment.getCommentCreatorDTO());
            }
            commentResponseDtoList.add(commentResponseDto);
        }
        postResponseDto.setComments(commentResponseDtoList);
        return ResponseEntity.ok(postResponseDto);
    }

    public ResponseEntity<Object> updatePost(PostUpdateDto postUpdateDto) {
        Optional<Post> post = postRepository.findById(postUpdateDto.getPostID());
        if (post.isEmpty()) {
            return ResponseEntity.ok(new ErrorResponse("Post does not exist"));
        }
        post.get().setPostBody(postUpdateDto.getPostBody());
        postRepository.save(post.get());
        return ResponseEntity.ok("Post edited successfully");
    }


    public ResponseEntity<Object> deletePost(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            return ResponseEntity.ok(new ErrorResponse("Post does not exist"));
        }

        for (Comment comment : post.get().getComments()) {
            comment.setCommentCreator(null);
            commentRepository.save(comment);
            commentRepository.deleteById(comment.getCommentID());
        }

        if(post.get().getPostCreator().getUserID()!=null){
            Optional<User> user = userRepository.findById(post.get().getPostCreator().getUserID());
            if (user.isPresent()) {
                user.get().getPosts().remove(post.get());
                userRepository.save(user.get());
            }
        }

        post.get().setComments(null);
        post.get().setPostCreator(null);
        postRepository.save(post.get());

        postRepository.deleteById(id);

        return ResponseEntity.ok("Post deleted");
    }

}
