package hibernatesearch;

import org.hibernate.search.annotations.*;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Indexed
@Table(name = "article")
public class Article {
    public Article() {
    }

    @Id
    private Long id;

    @Field
    private String title;

    @Field
    private String authors;

    @Field
    private String sourceAbbrev;

    @Field
    private String sourceLong;

    @Field
    private String volNum;

    @Field
    private String date;

    @Field
    private int startYear;

    @Field
    private int endYear;

    @Field
    private String pages;

    @Field
    private String subjectCodes;

    @Field
    private String topics;

    @Field
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
