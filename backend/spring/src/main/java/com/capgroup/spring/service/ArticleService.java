package com.capgroup.spring.service;

import com.capgroup.pdfparser.PDFMain;
import com.capgroup.spring.model.Article;
import com.capgroup.spring.model.Subject;
import com.capgroup.spring.repository.ArticleRepository;
//import com.capgroup.spring.repository.SubjectRepository;
import com.capgroup.spring.repository.SubjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;


import java.io.IOException;
import java.io.InputStream;
import java.util.*;


/**
 * The business layer that handles all requests to the articleRepository,
 * requires a reference to subjectService because certain transactions need
 * to add and remove subjects to articles and work with the subjectRepository
 */
@Service
@Slf4j
public class ArticleService {



    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private SubjectService subjectService;


    private static final List<String> SEARCHABLE_FIELDS = Arrays.asList("title", "authors", "sourceLong",
            "subjects.topics", "doi", "sourceAbbrev", "subjects.subjectCode", "subjects.generalTopic", "fullText");

    public ArticleService(@NonNull ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    /**
     * Passes valid searches to article repository
     * @param text text so be searched for
     * @param fields fields for text to be searched in
     * @param limit number of elements to return
     * @return valid hits from query
     */
    @Transactional(readOnly = true)
    public List<Article> searchArticles(String text, List<String> fields, Integer limit) {

        List<String> fieldsToSearchBy = fields.isEmpty() ? SEARCHABLE_FIELDS : fields;
        boolean containsInvalidField = fieldsToSearchBy.stream().anyMatch(f -> !SEARCHABLE_FIELDS.contains(f));

        if (containsInvalidField) {
            throw new IllegalArgumentException();
        }

        return articleRepository.searchBy(
                text, limit, fieldsToSearchBy.toArray(new String[0]));
    }

    /**
     * Add new article to database, ID is automatically generated when saves to repository, so it does not check
     * if an identical article is already present in database
     * @param title Title of article (required)
     * @param authors Authors of article
     * @param sourceAbbrev Abbreviated source article was published by
     * @param sourceLong Full source name article was published by
     * @param volNum Volume number of article
     * @param date Month/Day of article
     * @param startYear Starting year article was published
     * @param endYear End year article was published
     * @param pages Pages of article (often used for journals)
     * @param subjectCodes IDs of subjects to be associated with article
     * @param doi DOI number of article
     * @param file PDF file to be processed for full-text search on article
     * @return boolean of success/failure
     */
    @Transactional(readOnly = false)
    public boolean addArticle(String title, String authors, String sourceAbbrev, String sourceLong, String volNum,
                           String date, Integer startYear, Integer endYear, String pages,
                              String subjectCodes, String doi, MultipartFile file) {
        Article article = new Article();
        if (title != null) {
            article.setTitle(title);
        }
        if (authors != null) {
            article.setAuthors(authors);
        }
        if (sourceAbbrev != null) {
            article.setSourceAbbrev(sourceAbbrev);
        }
        if (sourceLong != null) {
            article.setSourceLong(sourceLong);
        }
        if (volNum != null) {
            article.setVolNum(volNum);
        }
        if (date != null) {
            article.setDate(date);
        }
        if (startYear != null) {
            article.setStartYear(startYear.intValue());
        }
        if (endYear != null) {
            article.setEndYear(endYear.intValue());
        }
        if (pages != null) {
            article.setPages(pages);
        }
        if (subjectCodes != null) {
            var subjectList = subjectService.getSubjects(Arrays.stream(subjectCodes.split(",", 0)).toList());
            for(var subj : subjectList){
                article.addSubject(subj);
            }
        }
        if (doi != null) {
            article.setDoi(doi);
        }
        if (file != null) {
            try {
                InputStream tempFile = file.getInputStream();
                String text = PDFMain.parseFile(tempFile);
                System.out.println(text);
                article.setFullText(text);
            } catch (IOException e) {
                log.info("Error parsing PDF: " + e.getMessage());
                return false;
            }
        }
        articleRepository.save(article);
        return true;
    }

    /**
     * Update existing article in database
     * @param id ID of article
     * @param title Title of article
     * @param authors Authors of article
     * @param sourceAbbrev Abbreviated source of article
     * @param sourceLong Full source name of article
     * @param volNum Volume number of article
     * @param date Month/Day of article
     * @param startYear Starting year article was published
     * @param endYear End year article was published
     * @param pages Pages of article (often used for journals)
     * @param subjectCodesToAdd Subject codes to be added to article
     * @param subjectCodesToRemove Subject codes to be removed from article
     * @param doi DOI of article
     * @param file File stream of article to be parsed for full-text search
     * @return
     */
    @Transactional(readOnly = false)
    public boolean updateArticle(Long id, String title, String authors, String sourceAbbrev,
                                 String sourceLong, String volNum, String date, Integer startYear, Integer endYear,
                                 String pages, String subjectCodesToAdd, String subjectCodesToRemove, String doi, MultipartFile file) {
        Article article;
        try {
            article = articleRepository.getReferenceById(id);
        }
        catch (EntityNotFoundException e) {
            log.info("Error updating article: " + e.getMessage());
            return false;
        }
        if (title != null) {
            article.setTitle(title);
        }
        if (authors != null) {
            article.setAuthors(authors);
        }
        if (sourceAbbrev != null) {
            article.setSourceAbbrev(sourceAbbrev);
        }
        if (sourceLong != null) {
            article.setSourceLong(sourceLong);
        }
        if (volNum != null) {
            article.setVolNum(volNum);
        }
        if (date != null) {
            article.setDate(date);
        }
        if (startYear != null) {
            article.setStartYear(startYear.intValue());
        }
        if (endYear != null) {
            article.setEndYear(endYear.intValue());
        }
        if (pages != null) {
            article.setPages(pages);
        }
        if (subjectCodesToAdd != null) {
            var subjectsToAdd = subjectService.getSubjects(Arrays.stream(subjectCodesToAdd.split(",", 0)).toList());
            for(var subj : subjectsToAdd){
                article.addSubject(subj);
            }
        }
        if (subjectCodesToRemove != null){
            var subjectsToRemove = subjectService.getSubjects(Arrays.stream(subjectCodesToRemove.split(",", 0)).toList());
            for(var subj : subjectsToRemove){
                article.removeSubject(subj);
            }
        }
        if (doi != null) {
            article.setDoi(doi);
        }
        if (file != null) {
            try {
                InputStream tempFile = file.getInputStream();
                String text = PDFMain.parseFile(tempFile);
                System.out.println(text);
                article.setFullText(text);
            } catch (IOException e) {
                log.info("Error parsing PDF: " + e.getMessage());
                return false;
            }
        }
        return true;
    }

    /**
     * Delete article by ID, assumes exists and cannot be reverted
     * @param id ID of article
     */
    @Transactional(readOnly = false)
    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }


    /**
     *
     * Interacts with the article repository to request a boolean or advanced search with the given parameters. Each clause
     * or sub-query should have at least three attributes: the term (which is given in the query parameter), the
     * boolean operator and the field. A clause of the total boolean search will correspond to an index of these Lists.
     * So, a clause's query, boolean operator, and field will all be in index i of given lists. Therefore, all Lists
     * should be the same length.
     *
     * @param query a list of Strings that are the actual terms being searched for
     * @param operators the boolean operators being applied to those searches (AND, OR, NOT)
     * @param fields the attributes of the article that the search will look at for the queries (authors, topics, etc.)
     * @param startYear the earliest year that the returned articles should be published in (null if no such parameter)
     * @param endYear the latest year that the returned articles should be published in (null if no such parameter)
     * @param limit the maximum number of articles that should be returned.
     * @return a List of articles
     */
    @Transactional(readOnly = true)
    public List<Article> boolSearchArticles(List<String> query, List<String> operators, List<String> fields,
                                            Integer startYear, Integer endYear, Integer limit) {

        return articleRepository.boolSearchBy(query, operators, fields, startYear, endYear, limit);
    }
}
