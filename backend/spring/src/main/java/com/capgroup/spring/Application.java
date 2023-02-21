package com.capgroup.spring;

import com.capgroup.spring.model.Article;
import com.capgroup.spring.repository.ArticleRepository;
import com.capgroup.spring.index.Indexer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// http://localhost:9001/article/search?text=query&limit=5

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class}) //these are excluded so there is no auto-password generation on startup for a basic query
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public ApplicationRunner initializeData(ArticleRepository articleRepository) throws Exception {
		return (ApplicationArguments args) -> {

			ArrayList<Article> articles = DataInput.enterData("backend/excelfiles/MainFormYes_18Jan2023.xlsx");
			articleRepository.saveAll(articles);

		};
	}

	@Bean
	public ApplicationRunner buildIndex(Indexer indexer){
		System.out.print("Reached runner");
		return (ApplicationArguments args) -> {
			indexer.indexPersistedData("com.capgroup.spring.model.Article");
		};
	}

}
