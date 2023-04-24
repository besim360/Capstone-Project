package com.capgroup.spring.controller;

import com.capgroup.spring.model.*;
import com.capgroup.spring.service.ArticleService;
import com.capgroup.spring.service.SubjectService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * REST API controller that receives HTTP requests from clients
 */

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:9000/", allowedHeaders = "*")
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private final ArticleService articleService;
    @Autowired
    private final SubjectService subjectService;

    /**
     * Constructor for our main controller
     * @param articleService handles all transactions regarding articles
     * @param subjectService handles all transactions regarding subjects
     */
    public ArticleController(ArticleService articleService, SubjectService subjectService) {
        this.articleService = articleService;
        this.subjectService = subjectService;
    }


    /**
     * Add article to database, will generate its own primary key so if two identical articles are inserted into the
     * database, both will appear as different articles on a search request
     * @param title the tile of the article (required)
     * @param authors authors involved in the article
     * @param sourceAbbrev the abbreviated version of the source name
     * @param sourceLong the full version of the source name
     * @param volNum the volume number
     * @param date the date the article was published in terms of month and day
     * @param startYear the starting year the article was published
     * @param endYear the ending year the article was published, should be same as starting year in many cases
     * @param pages the number of pages present for the article
     * @param subjectCodes the subject codes to be associated with the article
     * @param doi the doi of the article
     * @param file a file for full text search if one is present
     * @return ResponseEntity
     */
    @PostMapping("/admin/addArticle")
    public ResponseEntity<String> addArticle(@RequestParam(value = "title", required = true) String title, @RequestParam(value = "authors", required = false) String authors,
                                             @RequestParam(value = "sourceAbbrev", required = false) String sourceAbbrev, @RequestParam(value = "sourceLong", required = false) String sourceLong,
                                             @RequestParam(value = "volNum", required = false) String volNum, @RequestParam(value = "date", required = false) String date,
                                             @RequestParam(value = "startYear", required = false) Integer startYear, @RequestParam(value = "endYear", required = false) Integer endYear,
                                             @RequestParam(value = "pages", required = false) String pages, @RequestParam(value = "subjectCodes", required = false) String subjectCodes,
                                             @RequestParam(value = "doi", required = false) String doi, @RequestParam(value = "file", required = false) MultipartFile file){

        var wasSuccessful = articleService.addArticle(title, authors,
                sourceAbbrev, sourceLong, volNum,
                date, startYear, endYear, pages,
                subjectCodes, doi, file);
        if (wasSuccessful) {
            return new ResponseEntity<>("Article updated.", HttpStatusCode.valueOf(201)); //return contents of article added and response status
        }
        else {
            return new ResponseEntity<>("Failed to update article", HttpStatusCode.valueOf(400));
        }
    }

    /**
     * Updates an article's fields that exists in the database already, all fields but articleId are optional
     * @param articleId the primary key of the article generated on saving to the repository
     * @param title the new title of the article
     * @param authors the new authors of the article
     * @param sourceAbbrev the new abbreviated source of the article
     * @param sourceLong the new full source of the article
     * @param volNum the new volume number of the article
     * @param date the new month and day of the article
     * @param startYear the new starting year of the article
     * @param endYear the new ending year of the article
     * @param pages the new page range of the article
     * @param subjectCodesToAdd a list of subject codes to be added to the article
     * @param subjectCodesToRemove a list of subject codes to be removed from the article
     * @param doi the new doi of the article
     * @param file a new file to be parsed for full text search on the article
     * @return
     */
    @PutMapping("/admin/updateArticle/{articleId}")
    public ResponseEntity<String> updateArticle(@NotNull @PathVariable Long articleId,
                                              @RequestParam(value = "title", required = false) String title, @RequestParam(value = "authors", required = false) String authors,
                                              @RequestParam(value = "sourceAbbrev", required = false) String sourceAbbrev, @RequestParam(value = "sourceLong", required = false) String sourceLong,
                                              @RequestParam(value = "volNum", required = false) String volNum, @RequestParam(value = "date", required = false) String date,
                                              @RequestParam(value = "startYear", required = false) Integer startYear, @RequestParam(value = "endYear", required = false) Integer endYear,
                                              @RequestParam(value = "pages", required = false) String pages, @RequestParam(value = "subjectCodesToAdd", required = false) String subjectCodesToAdd,
                                              @RequestParam(value = "subjectCodesToRemove", required = false) String subjectCodesToRemove,
                                              @RequestParam(value = "doi", required = false) String doi, @RequestParam(value = "file", required = false) MultipartFile file){

        var wasSuccessful = articleService.updateArticle(articleId, title, authors,
                sourceAbbrev, sourceLong, volNum,
                date, startYear, endYear, pages,
                subjectCodesToAdd, subjectCodesToRemove, doi, file);
        if (wasSuccessful) {
            return new ResponseEntity<>("Article updated.", HttpStatusCode.valueOf(201)); //return contents of article added and response status
        }
        else {
            return new ResponseEntity<>("Failed to update article", HttpStatusCode.valueOf(400));
        }
    }

    /**
     * Delete article from repository by id, cannot be undone
     * @param articleId internal id of article to be removed
     */
    @DeleteMapping("/admin/{articleId}")
    public void deleteArticle(@PathVariable Long articleId){
        log.info("Request for article deletion of ID : {}", articleId);
        this.articleService.deleteArticle(articleId);
    }

    /**
     * Add subject to repository to be later associated with articles
     * @param subjectCode the primary key of the subject, is defined by the user and must be unique
     * @param generalTopic the general topic of the subject
     * @param topics all topics associated with the subject, separated by spaces (need to check if pdfmain tokenization is correctly set up)
     */
    @PostMapping("/admin/addSubject")
    public void addSubject(@RequestParam(value = "subjectCode", required = true) String subjectCode,
                                             @RequestParam(value = "generalTopic", required = true) String generalTopic,
                                             @RequestParam(value = "topics", required = true) String topics){
        log.info("Request for subject addition: [{},{},{}]", subjectCode, generalTopic, topics);
        this.subjectService.addSubject(subjectCode, generalTopic, topics);
    }

    /**
     * Update subject in repository
     * @param subjectCode primary key of subject, must already exist in repository
     * @param generalTopic general topic of the subject
     * @param topics all topics associated with the subject, separated by spaces
     */
    @PutMapping("/admin/updateSubject")
    public void updateSubject(@RequestParam(value = "subjectCode", required = true) String subjectCode,
                           @RequestParam(value = "generalTopic", required = false) String generalTopic,
                           @RequestParam(value = "topics", required = false) String topics){
        log.info("Request for subject updating: [{},{},{}]", subjectCode, generalTopic, topics);
        this.subjectService.updateSubject(subjectCode, generalTopic, topics);
    }

    /**
     * Delete subject code from repository
     * WARNING: will delete from all associated articles
     * @param id subject code to delete
     */
    @DeleteMapping("/admin/{subjectCode}")
    public void deleteSubject(@PathVariable String id){
        log.info("Request for subject deletion of ID : {}", id);
        this.subjectService.deleteSubject(id);
    }

    /**
     * Returns all subject codes present in repository
     * @return subjects in json form containing code, general topic, and topics
     */
    @GetMapping("/subjects")
    public List<Subject> getAllSubjects(){
        log.info("Returning all subjects in database");
        return subjectService.getAllSubjects();
    }

    /**
     * Handles requests for general searches of articles
     * @param text content that is being searched
     * @param fields fields to search, if none will search titles, authors, sourceAbbrev, sourceLong, subjects.topics,
     *               doi, subjects.subjectCode, subjects.generalTopic, fullText
     * @param limit maximum number of results to return
     * @return json array of articles
     */
    @GetMapping("/search")
    public List<Article> searchArticles(@RequestParam(value = "text", required = true) String text, @RequestParam(value="fields", required = false) String fields,
                                  @RequestParam(value = "limit", required = false) Integer limit) {

        log.info("Request for article search received with data : " + text + "," + fields + "," + limit); //nasty string depending on how it gets formatted under the hood.
        List<String> fieldList = null;
        if (fields != null) {
            fieldList = Arrays.stream(fields.split(",", 0)).toList(); //same here
        }
        else {
            fieldList = new ArrayList<>();
        }
        return articleService.searchArticles(text, fieldList, limit);
    }


    /**
     * Receives and handles boolean search requests. Calls on ArticleService to handle these requests to return a List
     * of articles that match the provided search parameters.
     *
     * Example URL: http://localhost:9001/article/bool?query=writing,nielsen,writing&operators=or,or,or&fields=title,authors,subjects.topics&startYear=2015&endYear=2020&limit=20
     *
     * @param query the list of keyword terms to search
     * @param operators the boolean operators applied to each clause of the search
     * @param fields the fields to search
     * @param startYear all returned articles must be published in or after this year (not required)
     * @param endYear all returned articles must be published in or before this year (not required)
     * @param limit number of articles returned should not exceed limit
     * @return a json array of articles that match all clauses of the boolean search with the given parameters
     */
    @GetMapping("/bool")
    public List<Article> boolSearchArticles(@RequestParam(value = "query", required = true) List<String> query,
                                            @RequestParam(value = "operators", required = true) List<String> operators,
                                            @RequestParam(value = "fields", required = true) List<String> fields,
                                            @RequestParam(value = "startYear", required = false) Integer startYear,
                                            @RequestParam(value = "endYear", required = false) Integer endYear,
                                            @RequestParam(value = "limit", required = false) Integer limit){
        log.info("Request for boolean article search received with data: " + query + "," + operators + "," + fields + ", "
                + startYear + ", " + endYear + ", " + limit);

        if (startYear != null && endYear != null) {
            if (startYear > endYear) {
                Integer temp = startYear;
                startYear = endYear;
                endYear = temp;
            }
        }

        return articleService.boolSearchArticles(query, operators, fields, startYear, endYear, limit);
    }
}
