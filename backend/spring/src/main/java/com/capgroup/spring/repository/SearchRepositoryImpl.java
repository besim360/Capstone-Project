package com.capgroup.spring.repository;

import com.capgroup.spring.model.Article;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.util.QueryBuilder;
import org.hibernate.search.backend.lucene.LuceneExtension;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Handles interactions between Hibernate and Spring, Hibernate search methods will be defined in here for AND, NOT, OR
 */
@Transactional
public class SearchRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
        implements SearchRepository<T, ID> {

    private EntityManager entityManager;

    public SearchRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.entityManager = entityManager;
    }

    public SearchRepositoryImpl(
            JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }


    /**
     * Gets search results and returns a list of the hits.
     *
     * @param text the keyword to be searched
     * @param limit the number of articles returned will not exceed limit (optional)
     * @param fields which fields will be searched (defaults to all fields)
     * @return a List of articles
     */
    @Override
    public List<T> searchBy(String text, Integer limit, String... fields) {
        SearchResult<T> result = getSearchResult(text, limit, fields);

        return result.hits();
    }

    /**
     * Simple keyword search where only the set of fields can be modified. Used for basic searching functions.
     *
     * @param text the keyword to be searched
     * @param limit the number of articles returned will not exceed limit (optional)
     * @param fields which fields will be searched (defaults to all fields)
     * @return a search result object
     */
    private SearchResult<T> getSearchResult(String text, Integer limit, String[] fields) {
        SearchSession searchSession = Search.session(entityManager);

        SearchResult<T> result =
                searchSession
                        .search(getDomainClass())
                        .where(f -> f.match().fields(fields).matching(text))
                        .fetch(limit);
        return result;
    }

    /**
     * Calls boolean search method to return a list of results.
     *
     * @param query list of terms
     * @param operators list of boolean operators to apply to each boolean clause
     * @param fields list of fields being searched
     * @param startYear all articles must be published in or after this year
     * @param endYear all articles must be published in or before this year
     * @param limit number of articles returned will be equal or less than limit
     * @return list of results from search
     */
    @Override
    public List<T> boolSearchBy(List<String> query, List<String> operators, List<String> fields, Integer startYear,
                                Integer endYear, Integer limit){
        return getBoolSearchResult(query, operators, fields, startYear, endYear, limit);
    }

    /**
     * Handles boolean search requests and returns a List of articles based on the search parameters. Each parameter of
     * one line or clause of the boolean search should all be given in the same index of the three Lists that are passed
     * to this function.
     *
     * Examples: query = { "these", "are", "terms" }, operators = { "and", "or", "not" },
     *                          fields = { "topics", "title", "authors"}
     *
     * Example clause: term = query.get(0), boolean operator = operators.get(0), field = fields.get(0) ...
     *                              ... = keyword "these" MUST be in field topics of all hits
     *
     * Reference documentation: https://docs.jboss.org/hibernate/stable/search/reference/en-US/html_single/#search-query-querydsl
     *
     * @param query list of terms
     * @param operators list of boolean operators to apply to each boolean clause
     * @param fields list of fields being searched
     * @param startYear all articles must be published in or after this year
     * @param endYear all articles must be published in or before this year
     * @param limit number of articles returned will be equal or less than limit
     * @return search result hits
     */
    private List<T> getBoolSearchResult(List<String> query, List<String> operators, List<String> fields, Integer startYear,
                                        Integer endYear, Integer limit) {

        SearchSession searchSession = Search.session(entityManager);
        SearchResult<T> result =
                searchSession
                        .search(getDomainClass())
                        .where(f -> f.bool(b -> {
                            // iterates over given Lists to create each clause of the boolean search
                            for (int i = 0; i < query.size(); i++) {
                                switch (operators.get(i).toLowerCase()) {
                                    // boolean ops and, or, not correspond with hibernate's filters to must, should, mustNot
                                    case "and" -> b.must(f.match().field(fields.get(i)).matching(query.get(i)));
                                    case "or" -> b.should(f.match().field(fields.get(i)).matching(query.get(i)));
                                    case "not" -> b.mustNot(f.match().field(fields.get(i)).matching(query.get(i)));
                                }
                            }
                            // if start and end years were given, do not match results out of those ranges
                            if (startYear != null){
                                b.must(f.range().field("startYear").atLeast(startYear));
                            }
                            if (endYear != null){
                                b.must(f.range().field("endYear").atMost(endYear));
                            }
                        }))
                        .fetch(limit);

        return result.hits();
    }
}
