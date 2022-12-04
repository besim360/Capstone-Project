package com.capgroup.spring.repository;

import com.capgroup.spring.model.Article;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends SearchRepository<Article, Long> {
}
