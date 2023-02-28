package com.capgroup.spring;

import com.capgroup.spring.model.Article;
import com.capgroup.spring.repository.ArticleRepository;
import com.capgroup.spring.index.Indexer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

// http://localhost:9000/article/search?text=query&limit=5

@SpringBootApplication
public class Application {


	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/*
	@Bean
	public ApplicationRunner initializeData(ArticleRepository articleRepository) throws Exception {
		return (ApplicationArguments args) -> {

			ArrayList<Article> articles = DataInput.enterData("backend/excelfiles/MainForm.xlsx");
			articleRepository.saveAllAndFlush(articles);

		};
	}

	@Bean
	public ApplicationRunner buildIndex(Indexer indexer){
		System.out.print("Reached runner");
		return (ApplicationArguments args) -> {
			indexer.indexPersistedData("com.capgroup.spring.model.Article");
		};
	}
	 */

}
