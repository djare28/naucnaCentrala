package rs.ac.uns.naucnacentrala.dto;

import lombok.Data;

@Data
public class CasopisDTO {
    private Long id;
    private Long issn;
    private String komeSeNaplacuje;
    private String naziv;
    private Double clanarina;
    private Boolean aktiviran;
}
