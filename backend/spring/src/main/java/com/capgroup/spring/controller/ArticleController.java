package com.capgroup.spring.controller;

import com.capgroup.spring.model.Article;
import com.capgroup.spring.model.SearchRequestDTO;
import com.capgroup.spring.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    public @ResponseBody String addArticle(@RequestBody Article article){
        articleService.addArticle(article); //will need to check for failure and return different statuses
        return "Article added."; //return contents of article added and response status
    }
    /*public ResponseEntity<?> addArticle(HttpServletRequest request) { //role we are looking for is 'admin'
        request.getHeader("Authorization");
    }*/
    /*public void testing(){
        log.info("accessed addArticle!");
    }*/
    //@PutMapping("/admin/updateArticle/") //
    @DeleteMapping("/admin/{articleId}")
    public void deleteArticle(@PathVariable Long articleId){
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
    public List<Article> searchArticles(SearchRequestDTO searchRequestDTO) {

        log.info("Request for article search received with data : " + searchRequestDTO);

        return articleService.searchArticles(searchRequestDTO.getText(), searchRequestDTO.getFields(), searchRequestDTO.getLimit());
    }
}
