package org.alexbezverkhniy.springcms.core.domain;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by bezverkhniy_10534 on 06/08/2014.
 */
@Entity
public class Authority implements GrantedAuthority {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String authority;

    private String description;
/*
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users",
            joinColumns = @JoinColumn(name = "username", unique = true),
            inverseJoinColumns = @JoinColumn(name = "username")
    )
    private User user;
*/

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthority() {
        return authority;
    }
    public void setAuthority(String authority) {
        this.authority = authority;
    }


/*
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
*/
}
