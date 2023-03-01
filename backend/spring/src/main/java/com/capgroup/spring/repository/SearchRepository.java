package com.capgroup.spring.repository;

import com.capgroup.spring.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/**
 * An abstraction layer built around Hibernate for Spring to handle interactions with the database
 */
@NoRepositoryBean
public interface SearchRepository <T, ID extends Serializable> extends JpaRepository<T, ID> {
    List<T> searchBy(String text, int limit, String... fields);

    List<T> boolSearchBy(List<String> text, List<String> boolOps, List<String> fields, int limit);
}
