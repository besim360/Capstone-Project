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


    public ArticleController(ArticleService articleService, SubjectService subjectService) {
        this.articleService = articleService;
        this.subjectService = subjectService;
    }

    /*
        Order of operations:
        1) Ui makes request to /article/admin
            UI passes: Authorization Header, Article ID parameter
        2) API takes token from UI authorization Header request and makes request to keycloak to authenticate the token
        3) API either returns 401 unauthorized (if keycloak returns unauthorized) OR API does the thing and returns its response
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
    @DeleteMapping("/admin/{articleId}")
    public void deleteArticle(@PathVariable Long articleId){
        log.info("Request for article deletion of ID : {}", articleId);
        this.articleService.deleteArticle(articleId);
    }
    @PostMapping("/admin/addSubject")
    public void addSubject(@RequestParam(value = "subjectCode", required = true) String subjectCode,
                                             @RequestParam(value = "generalTopic", required = true) String generalTopic,
                                             @RequestParam(value = "topics", required = true) String topics){
        log.info("Request for subject addition: [{},{},{}]", subjectCode, generalTopic, topics);
        this.subjectService.addSubject(subjectCode, generalTopic, topics);
    }
    @PutMapping("/admin/updateSubject")
    public void updateSubject(@RequestParam(value = "subjectCode", required = true) String subjectCode,
                           @RequestParam(value = "generalTopic", required = false) String generalTopic,
                           @RequestParam(value = "topics", required = false) String topics){
        log.info("Request for subject updating: [{},{},{}]", subjectCode, generalTopic, topics);
        this.subjectService.updateSubject(subjectCode, generalTopic, topics);
    }
    @DeleteMapping("/admin/{subjectCode}")
    public void deleteSubject(@PathVariable String id){
        log.info("Request for subject deletion of ID : {}", id);
        this.subjectService.deleteSubject(id);
    }
    @GetMapping("/subjects")
    public List<Subject> getAllSubjects(){
        log.info("Returning all subjects in database");
        return subjectService.getAllSubjects();
    }

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
