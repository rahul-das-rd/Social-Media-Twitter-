package com.app.twitter.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    private Long commentId;
    private String commentBody;
    private CommentCreatorDto commentCreator;

    public CommentResponseDto(Long commentId, String commentBody) {
        this.commentId = commentId;
        this.commentBody = commentBody;
    }


}
