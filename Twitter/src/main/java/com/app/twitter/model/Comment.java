package com.app.twitter.model;

import com.app.twitter.dto.comment.CommentCreatorDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long commentID;
    private String commentBody;
    @ManyToOne
    private User commentCreator;

    @JsonIgnore
    @ManyToOne
    private Post commentPost;

    public CommentCreatorDto getCommentCreatorDTO() {
        CommentCreatorDto commentCreatorDto = new CommentCreatorDto();
        commentCreatorDto.setUserID(this.commentCreator.getUserID());
        commentCreatorDto.setName(this.commentCreator.getName());
        return commentCreatorDto;
    }
}
