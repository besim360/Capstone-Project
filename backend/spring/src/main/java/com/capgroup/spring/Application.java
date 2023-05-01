package com.capgroup.spring;

import com.capgroup.pdfparser.DataInput;
import com.capgroup.spring.index.Indexer;
import com.capgroup.spring.model.Article;
import com.capgroup.spring.model.Subject;
import com.capgroup.spring.repository.ArticleRepository;
import com.capgroup.spring.repository.SearchRepositoryImpl;
import com.capgroup.spring.repository.SubjectRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.ArrayList;
import java.util.Map;
// http://localhost:9001/article/search?text=query&limit=5 example query
/**
 * Where application is run. Database and index can be optionally rebuilt here.
 * IMPORTANT WARNING:
 * Uncommenting the currently commented out methods initializeData(args) and buildIndex() and running the app will
 * completely rebuild the database with whatever data is in the folders excelfiles and pdfs (the data in these folders
 * are based on data provided in the dropbox and will likely be out of date if Murphy adds more articles). Rebuilding
 * can take between 5-10 minutes and will leave the database empty if interrupted before completely indexing/finishing.
 * These methods (initializeData(args) and buildIndex()) are important to keep as comments just in case the database
 * needs to be rebuilt.
 * The methods initializeData() and buildIndex() should remain commented out unless you want to completely rebuild the
 * index and database.
 */

@SpringBootApplication
@EntityScan
@ComponentScan
@EnableJpaRepositories(basePackages = {"com.capgroup.spring.repository"}, repositoryBaseClass = SearchRepositoryImpl.class)
@ConfigurationPropertiesScan
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

/*
	@Bean
	public ApplicationRunner initializeData(ArticleRepository articleRepository, SubjectRepository subjectRepository) throws Exception {
		return (ApplicationArguments args) -> {
			Map<String, Subject> subjectMap = DataInput.getSubjects("excelfiles/Subjects.xlsx");
			subjectRepository.saveAll(subjectMap.values());
			ArrayList<Article> articles = DataInput.enterData("excelfiles/MainForm.xlsx",
					"excelfiles/Sources.xlsx", subjectMap, "pdfs/");
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
 */

	@Bean
	public ApplicationRunner initializeData(ArticleRepository articleRepository, SubjectRepository subjectRepository) throws Exception {
		return (ApplicationArguments args) -> {
		};
	}
}
