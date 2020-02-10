package rs.ac.uns.naucnacentrala.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class FormSubmissionDto implements Serializable {
    private String fieldValue;
    private String fieldId;
}
