package org.alexbezverkhniy.springcms.core.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Created by bezverkhniy_10534 on 13/08/2014.
 */
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String title;

    //private String author;
    @Column(length = 512)
    private String text;

    @ManyToOne
    @JsonBackReference
    @JoinTable(
            name = "USER_COMMENTS",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private User author;

    public Comment() {
    }

    public Comment(User author, String title, String text) {
        this.author = author;
        this.text = text;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
/*
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
*/
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
