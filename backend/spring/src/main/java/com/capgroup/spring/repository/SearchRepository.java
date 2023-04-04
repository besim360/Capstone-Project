package com.capgroup.spring.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstraction layer built around Hibernate for Spring to handle interactions with the database
 */
@NoRepositoryBean
public interface SearchRepository<T, ID> extends JpaRepository<T, ID> {
    List<T> searchBy(String text, Integer limit, String... fields);

    List<T> boolSearchBy(List<String> query, List<String> operators, List<String> fields, Integer startYear,
                         Integer endYear, Integer limit);


    //List<T> boolSearchBy(List<String> text, List<String> boolOps, List<String> fields, int limit);
}
