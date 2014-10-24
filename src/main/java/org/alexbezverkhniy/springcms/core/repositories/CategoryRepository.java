package org.alexbezverkhniy.springcms.core.repositories;

import org.alexbezverkhniy.springcms.core.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by bezverkhniy_10534 on 20/10/2014.
 */
public interface CategoryRepository  extends PagingAndSortingRepository<Category, Long>, CrudRepository<Category, Long> {
    public Page<Category> findByTitleLikeIgnoreCase(@Param(value = "title")String title, Pageable p);
    public Page<Category> findByParentTitle(@Param(value = "title")String title, Pageable p);
}
