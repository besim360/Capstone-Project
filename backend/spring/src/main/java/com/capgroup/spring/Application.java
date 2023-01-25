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

// http://localhost:9000/article/search?text=query&limit=5

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
			article1.setTopics("web page construction, web design, intranet design, online");

			Article article2 = new Article();
			article2.setTitle("Regulatory Usability");
			article2.setAuthors( "Nielsen, Jakob");
			article2.setSourceAbbrev("NNgroup");
			article2.setSourceLong("NNgroup.com (Norman Nielsen Group, incorporates old Alertbox)");
			article2.setDate("3 Sept.");
			article2.setStartYear(2000);
			article2.setEndYear(2000);
			article2.setSubjectCodes("d60, d4, b62");
			article2.setTopics("web page construction, web design, intranet design, online, usability, usability " +
					"testing, users, user-centered, UX, focus groups, ergonomics, human factors, legal writing, laws, " +
					"legalities, contracts, letters of agreement, regulations, censorship, lawyers, law enforcement, " +
					"police, crime, dishonesty, genocide, society, editing");

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
			article3.setTopics("history of technical communication, history of writing, history, " +
					"personal names, cicero, rome, roman, place names");


			Article article4 = new Article();
			article4.setTitle("Comparing Cultural Perceptions of Editing from the Author's Point of View");
			article4.setAuthors( "Eaton, Angela, Pamela Estes Brewer, Tiffany Craft Portewig, and Cynthia R. Davidson");
			article4.setVolNum("55.2");
			article4.setSourceAbbrev("TC(STC)");
			article4.setSourceLong("Technical Communication");
			article4.setDate("May");
			article4.setStartYear(2008);
			article4.setEndYear(2008);
			article4.setPages("140-166");
			article4.setDoi(null);
			article4.setSubjectCodes("b34, b129, b2, b173");
			article4.setTopics("book reviews: written, internationalism, intercultural issues, transnational, diversity, " +
					"culture, localization, localizing, immigration, migrants, globalism, multiculturalism, bilingualism, " +
					"multilingualism, universal, composition of themes, gender, women-men, feminism, transgender, queer, masculinity, sexism");

			Article article5 = new Article();
			article5.setTitle("Copyright, Free Speech, and Democracy: Eldred v. Ashcroft and Its Implications for Technical Communicators");
			article5.setAuthors( "Herrington, TyAnna");
			article5.setVolNum("20.1");
			article5.setSourceAbbrev("TechCommQ");
			article5.setSourceLong("Technical Communication Quarterly");
			article5.setDate(null);
			article5.setStartYear(2010);
			article5.setEndYear(2010);
			article5.setPages("47-72");
			article5.setDoi("10.1080/10572252.2011.528321");
			article5.setSubjectCodes("d49, b62, b164");
			article5.setTopics("copyright, intellectual property, fair use, plagiarism, patents, trademarks, " +
					"information access, open access, legal writing, laws, legalities, contracts, letters of agreement," +
					" regulations, censorship, lawyers, law enforcement, police, crime, dishonesty, genocide, technology " +
					"transfer, diffusion, knowledge transfer, knowledge sharing, information-transfer model, boundary " +
					"spanning, boundary spanner");



			List<Article> articles = Arrays.asList(article1, article2, article3, article4, article5);
			articleRepository.saveAll(articles);
			//articleRepository.findAll();
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
