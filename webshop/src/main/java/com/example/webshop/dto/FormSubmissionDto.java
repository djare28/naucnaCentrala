package com.example.webshop.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class FormSubmissionDto implements Serializable {
    private String fieldValue;
    private String fieldId;
}
