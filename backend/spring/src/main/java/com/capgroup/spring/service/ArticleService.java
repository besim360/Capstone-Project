package com.capgroup.spring.service;

import com.capgroup.spring.model.Article;
import com.capgroup.spring.repository.ArticleRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * The business layer that forwards the call to the searchBy function
 */
@Service
public class ArticleService {
    private ArticleRepository articleRepository;

    private static final List<String> SEARCHABLE_FIELDS = Arrays.asList("title", "authors", "sourceLong", "topics", "doi");

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> searchArticles(String text, List<String> fields, int limit) {

        List<String> fieldsToSearchBy = fields.isEmpty() ? SEARCHABLE_FIELDS : fields;

        boolean containsInvalidField = fieldsToSearchBy.stream(). anyMatch(f -> !SEARCHABLE_FIELDS.contains(f));

        if(containsInvalidField) {
            throw new IllegalArgumentException();
        }

        return articleRepository.searchBy(
                text, limit, fieldsToSearchBy.toArray(new String[0]));
    }
}
