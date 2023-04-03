package com.capgroup.spring.repository;

import com.capgroup.spring.model.Article;
import org.springframework.stereotype.Repository;

/**
 * Abstract Interface for a repository specific to the Article class
 */
@Repository
public interface ArticleRepository extends SearchRepository<Article, Long> {

}
