package org.alexbezverkhniy.springcms.core.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by bezverkhniy_10534 on 11/09/2014.
 */
@Entity
public class Post extends BaseEntity<Long> implements Serializable {

    private String title;

    @ManyToOne
    @JsonBackReference
    @JoinTable(
            name = "AUTHOR_POSTS",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Author author;

    @Type(type = "materialized_clob")
    private String text;

    @OneToMany
    @JoinTable(
            name = "POST_COMMENTS",
            joinColumns = @JoinColumn(name = "post_id", unique = false),
            inverseJoinColumns = @JoinColumn(name = "comment_id", unique = false)
    )
    @Column(nullable = true)
    private Set<Comment> comments;

    public void addComment(Comment comment) {
        if(comments == null) {
            comments = new HashSet<Comment>();
        }

        comments.add(comment);
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }
}
