package org.alexbezverkhniy.springcms.core.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.Type;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * User class
 * 
 * @author "Alex Bezverkhniy"
 *
 */
@Entity
public class User implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String username;
    private String password;
    @Type(type = "numeric_boolean")
    private boolean accountNonExpired;

    @Type(type = "numeric_boolean")
    private boolean accountNonLocked;

    @Type(type = "numeric_boolean")
    private boolean credentialsNonExpired;

    @ManyToMany
    private Set<Authority> authorities;

    @OneToMany
    @JoinTable(
            name = "USER_COMMENTS",
            joinColumns = @JoinColumn(name = "user_id", unique = false),
            inverseJoinColumns = @JoinColumn(name = "comment_id", unique = false)
    )
    private Set<Comment> comments;

    @Type(type = "numeric_boolean")
    private boolean enabled;

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    //@Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    //@Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    //@Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public void addAuthority(Authority authority) {
        if(authorities == null) {
            authorities = new HashSet<Authority>();
        }

        authorities.add(authority);
    }

    public void addComment(Comment comment) {
        if(comments == null) {
            comments = new HashSet<Comment>();
        }

        comments.add(comment);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", accountNonExpired=" + accountNonExpired +
                ", accountNonLocked=" + accountNonLocked +
                ", credentialsNonExpired=" + credentialsNonExpired +
                ", authorities=" + authorities +
                ", enabled=" + enabled +
                '}';
    }
}
