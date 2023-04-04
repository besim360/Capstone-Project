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

    private static final List<String> SEARCHABLE_FIELDS = Arrays.asList("title", "authors", "sourceLong",
            "subjects.topics", "doi", "sourceAbbrev", "subjects.subjectCode", "subjects.generalTopic", "fullText");

    public SearchRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.entityManager = entityManager;
    }

    public SearchRepositoryImpl(
            JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public List<T> searchBy(String text, Integer limit, String... fields) {
        SearchResult<T> result = getSearchResult(text, limit, fields);

        return result.hits();
    }

    private SearchResult<T> getSearchResult(String text, Integer limit, String[] fields) {
        SearchSession searchSession = Search.session(entityManager);

        SearchResult<T> result =
                searchSession
                        .search(getDomainClass())
                        .where(f -> f.match().fields(fields).matching(text))
                        .fetch(limit);
        return result;
    }

    @Override
    public List<T> boolSearchBy(List<String> query, List<String> operators, List<String> fields, Integer startYear,
                                Integer endYear, Integer limit){
        return getBoolSearchResult(query, operators, fields, startYear, endYear, limit);
    }

    private void boolSearchAll(BooleanQuery.Builder internal, String term, String op){
        for (String field : SEARCHABLE_FIELDS){
            TermQuery termQuery = new TermQuery(new Term(field, term));
            if (op.toLowerCase().equals("not")) {
                internal.add(termQuery, BooleanClause.Occur.MUST_NOT);
            } else if (op.toLowerCase().equals("and")){
                internal.add(termQuery, BooleanClause.Occur.MUST);
            } else {
                internal.add(termQuery, BooleanClause.Occur.SHOULD);
            }
        }
    }

    /**
     *
     *
     *
     * @param limit the max number of hits to be returned
     * @return null
     */
    private List<T> getBoolSearchResult(List<String> query, List<String> operators, List<String> fields, Integer startYear,
                                        Integer endYear, Integer limit) {

        // might need to look into how boolean query is being built to make sure it is working properly
        SearchSession searchSession = Search.session(entityManager);
        SearchResult<T> result =
                searchSession
                        .search(getDomainClass())
                        .where(f -> f.bool(b -> {
                            for (int i = 0; i < query.size(); i++) {
                                switch (operators.get(i).toLowerCase()) {
                                    case "and" -> b.must(f.match().field(fields.get(i)).matching(query.get(i)));
                                    case "or" -> b.should(f.match().field(fields.get(i)).matching(query.get(i)));
                                    case "not" -> b.mustNot(f.match().field(fields.get(i)).matching(query.get(i)));
                                }
                            }
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
