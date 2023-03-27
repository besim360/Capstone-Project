package com.capgroup.spring;

import com.capgroup.pdfparser.DataInput;
import com.capgroup.spring.model.Article;
import com.capgroup.spring.repository.ArticleRepository;
import com.capgroup.spring.index.Indexer;
import com.capgroup.spring.repository.SearchRepositoryImpl;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.ArrayList;


// http://localhost:9001/article/search?text=query&limit=5

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = SearchRepositoryImpl.class)
@ConfigurationPropertiesScan
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

/*
	@Bean
	public ApplicationRunner initializeData(ArticleRepository articleRepository) throws Exception {
		return (ApplicationArguments args) -> {

			ArrayList<Article> articles = DataInput.enterData("backend/excelfiles/MainForm.xlsx",
					"backend/excelfiles/Sources.xlsx", "backend/excelfiles/Subjects.xlsx", "backend/pdfs/");
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
