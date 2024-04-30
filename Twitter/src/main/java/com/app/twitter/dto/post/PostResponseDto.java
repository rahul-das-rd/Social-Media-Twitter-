package com.app.twitter.dto.post;

import com.app.twitter.dto.comment.CommentResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {
    private Long postID;
    private String postBody;
    private LocalDate date;


    private List<CommentResponseDto> comments = new ArrayList<>();

    public PostResponseDto(Long postID, String postBody, LocalDate date) {
        this.postID = postID;
        this.postBody = postBody;
        this.date = date;
    }
}
