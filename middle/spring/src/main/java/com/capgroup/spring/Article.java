package com.capgroup.spring;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;


@Entity
@Indexed
@Table(name = "article")
public class Article {

    public Article(Long id, String title, String authors, String sourceAbbrev, String sourceLong, String volNum,
                   String date, int startYear, int endYear, String pages, String subjectCodes, String topics, String doi){
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.sourceAbbrev = sourceAbbrev;
        this.sourceLong = sourceLong;
        this.volNum = volNum;
        this.date = date;
        this.startYear = startYear;
        this.endYear = endYear;
        this.pages = pages;
        this.subjectCodes = subjectCodes;
        this.topics = topics;
        this.doi = doi;
    }

    public Article() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @FullTextField
    private String title;

    @FullTextField
    private String authors;

    @FullTextField
    private String sourceAbbrev;

    @FullTextField
    private String sourceLong;

    @FullTextField
    private String volNum;

    @FullTextField
    private String date;

    @GenericField
    private int startYear;

    @GenericField
    private int endYear;

    @FullTextField
    private String pages;

    @FullTextField
    private String subjectCodes;

    @FullTextField
    private String topics;

    @FullTextField
    private String doi;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getSourceAbbrev() {
        return sourceAbbrev;
    }

    public void setSourceAbbrev(String sourceAbbrev) {
        this.sourceAbbrev = sourceAbbrev;
    }

    public String getSourceLong() {
        return sourceLong;
    }

    public void setSourceLong(String sourceLong) {
        this.sourceLong = sourceLong;
    }

    public String getVolNum() {
        return volNum;
    }

    public void setVolNum(String volNum) {
        this.volNum = volNum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getSubjectCodes() {
        return subjectCodes;
    }

    public void setSubjectCodes(String subjectCodes) {
        this.subjectCodes = subjectCodes;
    }

    public String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

}
