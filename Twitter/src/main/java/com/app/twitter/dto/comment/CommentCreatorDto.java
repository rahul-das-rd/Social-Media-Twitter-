package com.app.twitter.dto.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCreatorDto {
    private Long userID;
    private String name;

    public CommentCreatorDto() {
    }

    public CommentCreatorDto(String name, Long userID) {
        this.name = name;
        this.userID = userID;
    }
}
