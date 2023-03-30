package com.capgroup.spring.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object where search parameters are defined
 */
@Value
public class SearchRequestDTO {

    @NotBlank
    private String text;

    private List<String> fields = new ArrayList<>();

    @Min(1)
    private int limit;
}
