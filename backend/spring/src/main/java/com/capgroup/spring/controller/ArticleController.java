package com.capgroup.spring.controller;

import com.capgroup.spring.model.*;
import com.capgroup.spring.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


/**
 * REST API controller that receives HTTP requests from clients
 */
@Slf4j
@RestController
@RequestMapping("/article")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("/admin/addArticle") //look at how to integrate DTO here
    public @ResponseBody String addArticle(@RequestParam(value = "title", required = true) String title, @RequestParam(value = "authors", required = false) String authors,
                                           @RequestParam(value = "sourceAbbrev", required = false) String sourceAbbrev, @RequestParam(value = "sourceLong", required = false) String sourceLong,
                                           @RequestParam(value = "volNum", required = false) String volNum, @RequestParam(value = "date", required = false) String date,
                                           @RequestParam(value = "startYear", required = false) Integer startYear, @RequestParam(value = "endYear", required = false) Integer endYear,
                                           @RequestParam(value = "pages", required = false) String pages, @RequestParam(value = "subjectCodes", required = false) String subjectCodes,
                                           @RequestParam(value = "topics", required = false) String topics, @RequestParam(value = "doi", required = false) String doi,
                                           @RequestParam(value = "file", required = false) MultipartFile file){
        articleService.addArticle(title, authors,
                sourceAbbrev, sourceLong, volNum,
                date, startYear, endYear, pages,
                subjectCodes, topics, doi, file);
        return "Article added."; //return contents of article added and response status
    }
    /*public ResponseEntity<?> addArticle(HttpServletRequest request) { //role we are looking for is 'admin'
        request.getHeader("Authorization");
    }*/
    /*public void testing(){
        log.info("accessed addArticle!");
    }*/
    @PutMapping("/admin/updateArticle/{articleId}")
    public @ResponseBody String updateArticle(@NotNull @PathVariable Long articleId,
                                              @RequestParam(value = "title", required = true) String title, @RequestParam(value = "authors", required = false) String authors,
                                              @RequestParam(value = "sourceAbbrev", required = false) String sourceAbbrev, @RequestParam(value = "sourceLong", required = false) String sourceLong,
                                              @RequestParam(value = "volNum", required = false) String volNum, @RequestParam(value = "date", required = false) String date,
                                              @RequestParam(value = "startYear", required = false) Integer startYear, @RequestParam(value = "endYear", required = false) Integer endYear,
                                              @RequestParam(value = "pages", required = false) String pages, @RequestParam(value = "subjectCodes", required = false) String subjectCodes,
                                              @RequestParam(value = "topics", required = false) String topics, @RequestParam(value = "doi", required = false) String doi,
                                              @RequestParam(value = "file", required = false) MultipartFile file){

        articleService.updateArticle(articleId, title, authors,
                sourceAbbrev, sourceLong, volNum,
                date, startYear, endYear, pages,
                subjectCodes,topics, doi, file);
        return "Article updated."; //return contents of article added and response status
    }
    @DeleteMapping("/admin/{articleId}")
    public void deleteArticle(@PathVariable Long articleId){
        log.info("Request for article deletion of ID : " + articleId);
        this.articleService.deleteArticle(articleId);
    }
    /*
    Order of operations:
    1) Ui makes request to /article/admin
        UI passes: Authorization Header, Article ID parameter
    2) API takes token from UI authorization Header request and makes request to keycloak to authenticate the token
    3) API either returns 401 unauthorized (if keycloak returns unauthorized) OR API does the thing and returns its response
     */
    @GetMapping("/search")
    public List<?> searchArticles(SearchRequestDTO searchRequestDTO) {

        log.info("Request for article search received with data : " + searchRequestDTO);

        return articleService.searchArticles(searchRequestDTO.getText(), searchRequestDTO.getFields(), searchRequestDTO.getLimit());
    }


    @GetMapping("/bool")
    public List<Article> boolSearchArticles(BooleanRequestDTO booleanRequestDTO){
        log.info("Request for boolean article search received with data: " + booleanRequestDTO);
        ArrayList<List<String>> queries = new ArrayList<>(3);
        queries.add(booleanRequestDTO.getQuery_one());
        if(!booleanRequestDTO.getQuery_two().isEmpty()){
            queries.add(booleanRequestDTO.getQuery_two());
            if (!booleanRequestDTO.getQuery_three().isEmpty()){
                queries.add(booleanRequestDTO.getQuery_three());
            }
        }
        return articleService.boolSearchArticles(queries, booleanRequestDTO.getLimit());
        //return articleService.boolSearchArticles(booleanRequestDTO.getText(), booleanRequestDTO.getBoolOps(),
        //        booleanRequestDTO.getFields(), booleanRequestDTO.getLimit());
    }
}
