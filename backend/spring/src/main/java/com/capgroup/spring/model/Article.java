package com.capgroup.spring.model;

import lombok.EqualsAndHashCode;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.*;


/**
 * Our main entity being stored in Hibernate, contains all the data related to a specific article to be stored in the database */
@Entity
@Indexed
@Table(name = "article")
@Getter
@Setter
@ToString
@EqualsAndHashCode

public class Article {


    /*public Article(String title, String authors) {
        this.title = title;
        this.authors = authors;
    }*/

    public Article() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @FullTextField
    @Column(name = "title", columnDefinition = "VARCHAR(512)")
    private String title;

    @FullTextField
    @Column(name = "authors")
    private String authors;

    @FullTextField
    @Column(name = "sourceAbbrev")
    private String sourceAbbrev;

    @FullTextField
    @Column(name = "sourceLong")
    private String sourceLong;

    @FullTextField
    @Column(name = "volNum")
    private String volNum;

    @FullTextField
    @Column(name = "date")
    private String date;

    @GenericField
    @Column(name = "startYear")
    private int startYear;

    @GenericField
    @Column(name = "endYear")
    private int endYear;

    @FullTextField
    @Column(name = "pages")
    private String pages;

    @FullTextField
    @Column(name = "subjectCodes")
    private String subjectCodes;

    @FullTextField
    @Column(name = "topics", columnDefinition = "VARCHAR(2048)")
    private String topics;

    @FullTextField
    @Column(name = "doi")
    private String doi;
}
