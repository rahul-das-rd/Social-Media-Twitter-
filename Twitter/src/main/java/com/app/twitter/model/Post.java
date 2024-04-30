package com.app.twitter.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long postID;
    private String postBody;
    private LocalDate date;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Comment> comments;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    private User postCreator;
}
