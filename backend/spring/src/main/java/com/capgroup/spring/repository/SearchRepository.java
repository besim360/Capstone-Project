package com.capgroup.spring.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * An abstraction layer built around Hibernate for Spring to handle interactions with the database
 */
@NoRepositoryBean
public interface SearchRepository <T, ID extends Serializable> extends JpaRepository<T, ID> {
    List searchBy(String text, int limit, String... fields);

    List boolSearchBy(ArrayList<List<String>> queries, int limit);

    //List<T> boolSearchBy(List<String> text, List<String> boolOps, List<String> fields, int limit);
}
