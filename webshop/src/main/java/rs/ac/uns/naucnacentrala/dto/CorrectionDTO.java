package rs.ac.uns.naucnacentrala.dto;

import lombok.Data;
import org.camunda.bpm.engine.form.FormField;

import java.util.ArrayList;
import java.util.List;

@Data
public class CorrectionDTO {
    private Long id;
    private List<FormField> formFields;
    private Double clanarina;
    private String naziv;
    private String komeSeNaplacuje;
    private List<Long> naciniPlacanja = new ArrayList<>();
    private List<Long> naucneOblasti = new ArrayList<>();

}
