package org.alexbezverkhniy.springcms.core.repositories;

import org.alexbezverkhniy.springcms.core.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by bezverkhniy_10534 on 11/09/2014.
 */
@RepositoryRestResource(path = "posts", itemResourceRel = "posts")
public interface PostRepository extends PagingAndSortingRepository<Post, Long>, CrudRepository<Post, Long> {
    public Page<Post> findByTitleLikeIgnoreCase(@Param(value = "title")String title, Pageable p);
    public Page<Post> findByTextLikeIgnoreCase(@Param(value = "text")String text, Pageable p);
}
