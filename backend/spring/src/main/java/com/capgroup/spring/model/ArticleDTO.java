package com.capgroup.spring.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

/**
 * Data Transfer Object that stores article information
 */
@Value
public class ArticleDTO {
    @NotBlank
    private String title;

    private String authors;
    private String sourceAbbrev;
    private String sourceLong;
    private String volNum;
    private String date;
    private Integer startYear; //need integers to check for null on updates
    private Integer endYear;
    private String pages;
    private String subjectCodes;
    private String doi;
}
