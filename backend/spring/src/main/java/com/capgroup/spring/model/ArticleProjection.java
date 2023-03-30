package com.capgroup.spring.model;


public record ArticleProjection(Long id, String title, String authors,
                                String sourceAbbrev, String sourceLong, String volNum, String date,
                                int startYear, int endYear, String pages, String subjectCodes, String topics, String doi){ }

