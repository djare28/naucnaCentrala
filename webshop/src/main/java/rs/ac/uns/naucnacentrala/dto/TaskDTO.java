package rs.ac.uns.naucnacentrala.dto;

import lombok.Data;

@Data
public class TaskDTO {
    private String id;
    private String user;
    private String instanceId;
    private String recenzenti;
    private String processId;
    private Long clanarina;
    private String naucneOblasti;
    private String glavniUrednik;
    private String urednici;
    private String naciniPlacanja;
    private String naziv;
    private String komeSeNaplacuje;
}
