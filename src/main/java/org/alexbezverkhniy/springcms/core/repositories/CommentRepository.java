package org.alexbezverkhniy.springcms.core.repositories;

import org.alexbezverkhniy.springcms.core.domain.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

/**
 * Created by bezverkhniy_10534 on 13/08/2014.
 */
@RepositoryRestResource(path = "comments", itemResourceRel = "comments")
//@Repository
public interface CommentRepository extends PagingAndSortingRepository<Comment, Long>, CrudRepository<Comment, Long> {
}
