package com.capgroup.spring.model;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object that stores article information
 *
 * @param startYear need integers to check for null on updates
 */

public record ArticleDTO(@NotBlank String title, String authors, String sourceAbbrev, String sourceLong, String volNum,
                         String date, Integer startYear, Integer endYear, String pages, String subjectCodes,
                         String doi) {
}
