package com.capgroup.spring.controller;

import com.capgroup.spring.model.Article;
import com.capgroup.spring.model.SearchRequestDTO;
import com.capgroup.spring.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/article")
public class ArticleController {
    private ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/search")
    public List<Article> searchArticles(SearchRequestDTO searchRequestDTO) {

        log.info("Request for article search received with data : " + searchRequestDTO);

        return articleService.searchArticles(searchRequestDTO.getText(), searchRequestDTO.getFields(), searchRequestDTO.getLimit());
    }
}
