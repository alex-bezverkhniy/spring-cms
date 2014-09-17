package org.alexbezverkhniy.springcms.core.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by bezverkhniy_10534 on 13/08/2014.
 */
@Entity
public class Comment extends BaseEntity<Long> implements Serializable {

    private String title;

    @Column(length = 512)
    private String text;

    @ManyToOne
    @JsonBackReference
    @JoinTable(
            name = "AUTHOR_COMMENTS",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Author author;

    @ManyToOne
    @JsonBackReference
    @JoinTable(
            name = "POST_COMMENTS",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private Post post;


    public Comment() {
    }
    public Comment(Author author, String title, String text) {
        this.author = author;
        this.text = text;
        this.title = title;
    }


    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
