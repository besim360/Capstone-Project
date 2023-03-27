package com.capgroup.spring.repository;

import com.capgroup.spring.model.ArticleProjection;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;
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
import java.util.List;

/**
 * Handles interactions between Hibernate and Spring, Hibernate search methods will be defined in here for AND, NOT, OR
 */
@Transactional
public class SearchRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
        implements SearchRepository<T, ID> {

    private final EntityManager entityManager;

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
    public List searchBy(String text, int limit, String... fields) {
        SearchResult<ArticleProjection> result = getSearchResult(text, limit, fields);

        return result.hits();
    }

    private SearchResult<ArticleProjection> getSearchResult(String text, int limit, String[] fields) {
        SearchSession searchSession = Search.session(entityManager);

        SearchResult<ArticleProjection> result =
                searchSession
                        .search(getDomainClass())
                        .select(f-> f.composite(
                                list -> new ArticleProjection(
                                        (Long) list.get(0),         //id
                                        (String) list.get(1),       //title
                                        (String) list.get(2),       //authors
                                        (String) list.get(3),       //sourceAbbrev
                                        (String) list.get(4),       //sourceLong
                                        (String) list.get(5),       //volNum
                                        (String) list.get(6),       //date
                                        (Integer) list.get(7),      //startYear
                                        (Integer) list.get(8),      //endYear
                                        (String) list.get(9),       //pages
                                        (String) list.get(10),      //subjectCodes
                                        (String) list.get(11),      //topics
                                        (String) list.get(12)       //doi
                                ),
                                f.id(Long.class),
                                f.field("title", String.class),
                                f.field("authors", String.class),
                                f.field("sourceAbbrev", String.class),
                                f.field("sourceLong", String.class),
                                f.field("volNum", String.class),
                                f.field("date", String.class),
                                f.field("startYear", Integer.class),
                                f.field("endYear", Integer.class),
                                f.field("pages", String.class),
                                f.field("subjectCodes", String.class),
                                f.field("topics", String.class),
                                f.field("doi", String.class)
                        ))
                        .where(f -> f.match().fields(fields).matching(text))
                        .fetch(limit);
        return result;
    }

    @Override
    public List boolSearchBy(ArrayList<List<String>> queries, int limit){
        return getBoolSearchResult(queries, limit);
    }

    /**
     * This will take a list of queries. Each query is a collection of strings passed in a very particular order. The
     * text of the query is in the 0th position, the field is in the 1st position, and the boolean operator is in the
     * 2nd position. It must be given in that order via the URL.
     * @param queries the list of queries
     * @param limit the max number of hits to be returned
     * @return null
     */
    private List<T> getBoolSearchResult(ArrayList<List<String>> queries, int limit){

        // might need to look into how boolean query is being built to make sure it is working properly
        SearchSession searchSession = Search.session(entityManager);
        BooleanQuery.Builder internal = new BooleanQuery.Builder();
        BooleanQuery.Builder external = new BooleanQuery.Builder();
        int clauses = 0;

        for (int i = 0; i < queries.size(); i++){

            List<String> q = queries.get(i);
            String t = q.get(0);
            String field = q.get(1);
            TermQuery termQuery = new TermQuery(new Term(field, t));
            String op = q.get(2);

            if (op.toLowerCase().equals("not")) {
                internal.add(termQuery, BooleanClause.Occur.MUST_NOT);
                clauses++;
            } else if (op.toLowerCase().equals("and")){
                internal.add(termQuery, BooleanClause.Occur.MUST);
                clauses++;

            } else {
                internal.add(termQuery, BooleanClause.Occur.SHOULD);
                clauses++;
            }

        }

        //b.setMinimumNumberShouldMatch(clauses);
        external.add(internal.build(), BooleanClause.Occur.MUST);
        BooleanQuery bq = external.build();
        List<T> hits =
                searchSession
                        .search(getDomainClass())
                        .extension(LuceneExtension.get())
                        .where(f -> f.fromLuceneQuery(bq))
                        .fetchHits(limit);

        return hits;
    }

    /*
    @Override
    public List boolSearchBy(List<String> text, List<String> boolOps, List<String> fields, int limit){
        return getBoolSearchResult(text, boolOps, fields, limit);
    }

    private List<Article> getBoolSearchResult(List<String> text, List<String> boolOps, List<String> fields, int limit){
        SearchSession searchSession = Search.session(entityManager);
        BooleanQuery.Builder b = new BooleanQuery.Builder();

        int clauses = 0;
        for (int i = 0; i < text.size(); i++){

            String t = text.get(i);
            String field = fields.get(i);
            TermQuery termQuery = new TermQuery(new Term(field, t));
            String op = boolOps.get(i);

            if (op.toLowerCase().equals("not")) {
                b.add(termQuery, BooleanClause.Occur.MUST_NOT);
                clauses++;
            } else if (op.toLowerCase().equals("and")){
                b.add(termQuery, BooleanClause.Occur.MUST);
                clauses++;

            } else if (op.toLowerCase().equals("or")){
                b.add(termQuery, BooleanClause.Occur.SHOULD);
                clauses++;
            }

        }

        //b.setMinimumNumberShouldMatch(clauses);
        BooleanQuery bq = b.build();
        List<Article> hits =
                searchSession
                        .search(Article.class)
                        .extension(LuceneExtension.get())
                        .where(f -> f.fromLuceneQuery(bq))
                        .fetchHits(limit);

        return hits;
    }

     */
}
