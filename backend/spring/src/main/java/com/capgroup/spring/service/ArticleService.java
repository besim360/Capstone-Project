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
 * The business layer
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
    //private static final List<String> NUMERIC_FIELDS = Arrays.asList("subjects.subjectCode", "startYear", "endYear");

    public ArticleService(@NonNull ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Transactional(readOnly = true)
    public List<Article> searchArticles(String text, List<String> fields, int limit) {

        List<String> fieldsToSearchBy = fields.isEmpty() ? SEARCHABLE_FIELDS : fields;
        boolean containsInvalidField = fieldsToSearchBy.stream().anyMatch(f -> !SEARCHABLE_FIELDS.contains(f));

        if (containsInvalidField) {
            throw new IllegalArgumentException();
        }

        return articleRepository.searchBy(
                text, limit, fieldsToSearchBy.toArray(new String[0]));
    }

    @Transactional(readOnly = false)
    public boolean addArticle(String title, String authors, String sourceAbbrev, String sourceLong, String volNum,
                           String date, Integer startYear, Integer endYear, String pages,
                              String subjectCodes, String doi, MultipartFile file) { //can create article here, then save
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

    @Transactional(readOnly = false)
    public void deleteArticle(Long id) { //likely rename to only pass article id
        articleRepository.deleteById(id); //may need to wrap in a try/catch
    }



    /*

    This is the version that splits text, boolOps and fields into separate lists
    public List<Article> boolSearchArticles(List<String> text, List<String> boolOps, List<String> fields, int limit){

        List<String> fieldsToSearchBy = fields.isEmpty() ? SEARCHABLE_FIELDS : fields;


        boolean containsInvalidField = fields.stream(). anyMatch(f -> !SEARCHABLE_FIELDS.contains(f));
        if (containsInvalidField){
            throw new IllegalArgumentException();
        }

        return articleRepository.boolSearchBy(text, boolOps, fieldsToSearchBy, limit);
    }*/



    @Transactional(readOnly = true)
    public List<Article> boolSearchArticles(ArrayList<List<String>> queries, int limit) {

        return articleRepository.boolSearchBy(queries, limit);
    }
}
