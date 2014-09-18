package org.alexbezverkhniy.springcms.core.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by bezverkhniy_10534 on 12/09/2014.
 */
@Entity
@Table(name = "AUTHORS")
@PrimaryKeyJoinColumn(name = "USER_ID")
public class Author extends User {
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;
/*
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "AUTHOR_COMMENTS",
            joinColumns = @JoinColumn(name = "user_id", unique = false),
            inverseJoinColumns = @JoinColumn(name = "comment_id", unique = false)
    )
    private Set<Comment> comments;
*/
    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }
/*
    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Comment comment) {
        if(comments == null) {
            comments = new HashSet<Comment>();
        }

        comments.add(comment);
    }
    */
}
