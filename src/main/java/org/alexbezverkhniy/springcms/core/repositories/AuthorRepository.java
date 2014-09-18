package org.alexbezverkhniy.springcms.core.repositories;

import org.alexbezverkhniy.springcms.core.domain.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by bezverkhniy_10534 on 11/09/2014.
 */

/**
 * TODO: For future extending of User class
 */
@RepositoryRestResource(collectionResourceRel = "authors", path = "authors", exported = true)
public interface AuthorRepository extends CrudRepository<Author, Long> {
}
