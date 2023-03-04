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

    private static final List<String> SEARCHABLE_FIELDS = Arrays.asList("title", "authors", "sourceLong", "topics", "doi", "sourceAbbrev", "fullText");

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

    public void addArticle(String title, String authors, String sourceAbbrev, String sourceLong, String volNum, String date, Integer startYear, Integer endYear, String pages, String subjectCodes, String doi){ //can create article here, then save
        Article article = new Article();
        if(title != null){
            article.setTitle(title);
        }
        if(authors != null){
            article.setAuthors(authors);
        }
        if(sourceAbbrev != null){
            article.setSourceAbbrev(sourceAbbrev);
        }
        if(sourceLong != null){
            article.setSourceLong(sourceLong);
        }
        if(volNum != null){
            article.setVolNum(volNum);
        }
        if(date != null){
            article.setDate(date);
        }
        if(startYear != null){
            article.setStartYear(startYear.intValue());
        }
        if(endYear != null){
            article.setEndYear(endYear.intValue());
        }
        if(pages != null){
            article.setPages(pages);
        }
        if(subjectCodes != null){
            article.setSubjectCodes(subjectCodes);
        }
        if(doi != null){
            article.setDoi(doi);
        }

        articleRepository.save(article);
    }
    public void updateArticle(Long id, String title, String authors, String sourceAbbrev, String sourceLong, String volNum, String date, Integer startYear, Integer endYear, String pages, String subjectCodes, String doi){
        Article article = articleRepository.getReferenceById(id);
        if(title != null){
            article.setTitle(title);
        }
        if(authors != null){
            article.setAuthors(authors);
        }
        if(sourceAbbrev != null){
            article.setSourceAbbrev(sourceAbbrev);
        }
        if(sourceLong != null){
            article.setSourceLong(sourceLong);
        }
        if(volNum != null){
            article.setVolNum(volNum);
        }
        if(date != null){
            article.setDate(date);
        }
        if(startYear != null){
            article.setStartYear(startYear.intValue());
        }
        if(endYear != null){
            article.setEndYear(endYear.intValue());
        }
        if(pages != null){
            article.setPages(pages);
        }
        if(subjectCodes != null){
            article.setSubjectCodes(subjectCodes);
        }
        if(doi != null){
            article.setDoi(doi);
        }
    }
    public void deleteArticle(Long id){ //likely rename to only pass article id
        articleRepository.deleteById(id); //may need to wrap in a try/catch
    }

    public List<Article> boolSearchArticles(List<String> text, List<String> boolOps, List<String> fields, int limit){

        List<String> fieldsToSearchBy = fields.isEmpty() ? SEARCHABLE_FIELDS : fields;

        boolean containsInvalidField = fields.stream(). anyMatch(f -> !SEARCHABLE_FIELDS.contains(f));
        if (containsInvalidField){
            throw new IllegalArgumentException();
        }

        return articleRepository.boolSearchBy(text, boolOps, fieldsToSearchBy, limit);
    }
}
