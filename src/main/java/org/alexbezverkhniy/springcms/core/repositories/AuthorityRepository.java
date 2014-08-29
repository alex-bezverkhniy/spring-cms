package org.alexbezverkhniy.springcms.core.repositories;

import org.alexbezverkhniy.springcms.core.domain.Authority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by bezverkhniy_10534 on 06/08/2014.
 */
@RepositoryRestResource(exported = true)
public interface AuthorityRepository extends PagingAndSortingRepository<Authority, Long>, CrudRepository<Authority, Long> {

}
