package org.alexbezverkhniy.springcms.core.domain;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * Created by bezverkhniy_10534 on 16/09/2014.
 */
@MappedSuperclass
public class BaseAuthorizedEntity extends BaseEntity<Long>{
    @ManyToOne(fetch = FetchType.LAZY)
    protected Author author;

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
