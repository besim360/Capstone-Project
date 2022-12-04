package com.capgroup.spring;

import com.capgroup.spring.model.Article;
import com.capgroup.spring.repository.ArticleRepository;
import com.capgroup.spring.index.Indexer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public ApplicationRunner initializeData(ArticleRepository articleRepository) throws Exception {
		return (ApplicationArguments args) -> {
			Article article1 = new Article();
			article1.setTitle("Saying No: How to Handle Missing Features");
			article1.setAuthors( "Nielsen, Jakob");
			article1.setSourceAbbrev("NNgroup");
			article1.setSourceLong("NNgroup.com (Norman Nielsen Group, incorporates old Alertbox)");
			article1.setDate("23 Jan.");
			article1.setStartYear(2000);
			article1.setEndYear(2000);
			article1.setSubjectCodes("d60");
			//article1.setTopics("web page construction, web design, intranet design, online");

			Article article2 = new Article();
			article2.setTitle("Regulatory Usability");
			article2.setAuthors( "Nielsen, Jakob");
			article2.setSourceAbbrev("NNgroup");
			article2.setSourceLong("NNgroup.com (Norman Nielsen Group, incorporates old Alertbox)");
			article2.setDate("3 Sept.");
			article2.setStartYear(2000);
			article2.setEndYear(2000);
			article2.setSubjectCodes("d60, d4, b62");
			//article2.setTopics("web page construction, web design, intranet design, online, usability, usability testing, users, user-centered, UX, focus groups, ergonomics, human factors, legal writing, laws, legalities, contracts, letters of agreement, regulations, censorship, lawyers, law enforcement, police, crime, dishonesty, genocide, society, editing");

			Article article3 = new Article();
			article3.setTitle("His Master's Voice: Tiro and the Rise of the Roman Secretarial Class");
			article3.setAuthors( "Di Renzo, Anthony");
			article3.setVolNum("30.2");
			article3.setSourceAbbrev("JTWC");
			article3.setSourceLong("Journal of Technical Writing and Communication");
			article3.setDate("Ap.");
			article3.setStartYear(2000);
			article3.setEndYear(2000);
			article3.setPages("155-168");
			article3.setDoi("10.2190/B4YD-5FP7-1W8D-V3UC");
			article3.setSubjectCodes("b49, c572, c649");
			//article3.setTopics("history of technical communication, history of writing, history, personal names, cicero, rome, roman, place names");
			List<Article> articles = Arrays.asList(article1, article2, article3);
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
