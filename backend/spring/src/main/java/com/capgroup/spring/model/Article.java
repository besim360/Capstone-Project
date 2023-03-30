package com.capgroup.spring.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.search.engine.backend.types.Projectable;
import org.hibernate.search.engine.backend.types.Searchable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;


import java.util.Objects;


/**
 * Our main entity being stored in Hibernate, contains all the data related to a specific article to be stored in the database */
@Entity
@Indexed
@Table(name = "article")
@Getter
@Setter
@ToString

public class Article {


    /*public Article(String title, String authors) {
        this.title = title;
        this.authors = authors;
    }*/

    public Article() {
    }

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

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
    private int startYear;

    @GenericField(projectable = Projectable.YES)
    @Column(name = "endYear")
    private int endYear;

    @FullTextField(searchable = Searchable.NO, projectable = Projectable.YES)
    @Column(name = "pages")
    private String pages;

    @FullTextField(projectable = Projectable.YES)
    @Column(name = "subjectCodes")
    private String subjectCodes;

    @FullTextField(analyzer = "standard", projectable = Projectable.YES)
    @Column(name = "topics", columnDefinition = "VARCHAR(2048)")
    private String topics;

    @FullTextField(projectable = Projectable.YES)
    @Column(name = "doi")
    private String doi;


    @FullTextField(analyzer = "stop", projectable = Projectable.NO)
    @Column(name = "fullText", columnDefinition = "TEXT")
    private String fullText;

    //replaces @EqualsandHashcode with these overrides
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Article article = (Article) o;
        return id != null && Objects.equals(id, article.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
