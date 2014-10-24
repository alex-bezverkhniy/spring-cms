package org.alexbezverkhniy.springcms.core.domain;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by bezverkhniy_10534 on 20/10/2014.
 */
@Entity
public class Category extends BaseEntity<Long> implements Serializable {
    @Type(type = "materialized_clob")
    private String title;
    private String text;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "POST_CATEGORIES",
            joinColumns = @JoinColumn(name = "post_id", unique = false),
            inverseJoinColumns = @JoinColumn(name = "category_id", unique = false)
    )
    @Column(nullable = true)
    private Set<Post> posts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Category parent;

    public Category() {
        this(null, null, null, null);
    }

    public Category(String title, String text) {
        this(title, text, null, null);
    }

    public Category(String title, String text, Category category) {
        this(title, text, null, category);
    }

    public Category(String title, String text, Set<Post> posts, Category parent) {
        this.title = title;
        this.text = text;
        this.posts = posts;
        this.parent = parent;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addComment(Post comment) {
        if(posts == null) {
            posts = new HashSet<Post>();
        }
        posts.add(comment);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }
}
