package com.capgroup.spring.model;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * Each clause of the boolean search will correspond to an index of these lists;
 * i.e. the 1st element of boolOps is the boolean operation to be
 * applied to the 1st String in text, searching only the 1st field.
 * Searches with each element will be combined based on boolOp.
 */
@Data
public class BooleanRequestDTO {

    @NotBlank
    private List<String> query_one = new ArrayList<>();
    private List<String> query_two = new ArrayList<>();
    private List<String> query_three = new ArrayList<>();

    /*
    @NotBlank
    private List<String> text = new ArrayList<>();

    @NotBlank
    private List<String> boolOps = new ArrayList<>();

    @NotBlank
    private List<String> fields = new ArrayList<>();

     */

    @Min(1)
    private int limit;
}