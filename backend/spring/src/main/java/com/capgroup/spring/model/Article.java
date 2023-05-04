package com.capgroup.spring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.search.engine.backend.types.ObjectStructure;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Searchable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;


/**
 * Our main entity being stored in Hibernate, contains all the data related to a
 * specific article to be stored in the database
 */
@Entity
@Indexed
@Table(name = "article")
@Getter
@Setter
@ToString
@Transactional
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Article {

    public Article() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Long id;

    @EqualsAndHashCode.Include
    @FullTextField(projectable = Projectable.YES)
    @Column(name = "title", columnDefinition = "VARCHAR(1024)")
    private String title;


    @FullTextField(projectable = Projectable.YES)
    @Column(name = "authors")
    private String authors;


    @FullTextField(projectable = Projectable.YES)
    @Column(name = "sourceAbbrev")
    private String sourceAbbrev;


    @FullTextField(projectable = Projectable.YES)
    @Column(name = "sourceLong")
    private String sourceLong;


    @FullTextField(projectable = Projectable.YES)
    @Column(name = "volNum")
    private String volNum;


    @FullTextField(projectable = Projectable.YES)
    @Column(name = "date")
    private String date;


    @GenericField(projectable = Projectable.YES)
    @Column(name = "startYear")
    private Integer startYear;


    @GenericField(projectable = Projectable.YES)
    @Column(name = "endYear")
    private Integer endYear;


    @FullTextField(searchable = Searchable.NO, projectable = Projectable.YES)
    @Column(name = "pages")
    private String pages;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.MERGE
    })
    @IndexedEmbedded(structure = ObjectStructure.NESTED, includeEmbeddedObjectId = true, includeDepth = 1)
    @JoinTable(name = "articles_subjects", joinColumns = { @JoinColumn(name = "article_id") }, inverseJoinColumns = { @JoinColumn(name = "subject_id") })
    private List<Subject> subjects = new ArrayList<>();



    @FullTextField(projectable = Projectable.YES)
    @Column(name = "doi")
    private String doi;


    @JsonIgnore
    @FullTextField(analyzer = "stop", projectable = Projectable.NO)
    @Column(name = "fullText", columnDefinition = "TEXT")
    private String fullText;

    /**
     * Adds subject to article and maintains many-to-many relationship by adding article to subject
     * @param subject entity to be added to article's subject list
     */
    public void addSubject(Subject subject){
        subjects.add(subject);
        subject.getArticles().add(this);
    }

    /**
     * Removes subject from article and maintains many-to-many relationship by removing article from subject
     * @param subject entity to be removed from article's subject list
     */
    public void removeSubject(Subject subject){
        subjects.remove(subject);
        subject.getArticles().remove(this);
    }

}
