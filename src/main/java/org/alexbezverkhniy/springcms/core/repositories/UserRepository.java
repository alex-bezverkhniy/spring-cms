package org.alexbezverkhniy.springcms.core.repositories;

import org.alexbezverkhniy.springcms.core.domain.Authority;
import org.alexbezverkhniy.springcms.core.domain.Post;
import org.alexbezverkhniy.springcms.core.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by bezverkhniy_10534 on 21/07/2014.
 */

@RepositoryRestResource(collectionResourceRel = "users", path = "users", exported = true)
public interface UserRepository extends  CrudRepository<User, Long>, PagingAndSortingRepository<User, Long> {
    User findByUsername(@Param("name") String name);
    public Page<User> findByUsernameLikeIgnoreCase(@Param(value = "name")String name, Pageable p);
}
