package org.alexbezverkhniy.springcms.core.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "POST_CATEGORIES",
            joinColumns = @JoinColumn(name = "post_id", unique = false),
            inverseJoinColumns = @JoinColumn(name = "category_id", unique = false)
    )
    @Column(nullable = true)
    private List<Category> categories;

    @Transient
    private String categoriesList;

    public String getCategoriesList() {
        if(categories != null && categories.size() > 0) {
            categoriesList = "";
            for(Category cat : categories) {
                categoriesList += cat.getTitle() + ", ";
            }
            categoriesList = categoriesList.substring(0, categoriesList.length()-2);
        }
        return categoriesList;
    }

    public void addComment(Comment comment) {
        if(comments == null) {
            comments = new HashSet<Comment>();
        }

        comments.add(comment);
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public void addCategory(Category category) {
        if(categories == null) {
            categories = new ArrayList<Category>();
        }

        categories.add(category);
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
