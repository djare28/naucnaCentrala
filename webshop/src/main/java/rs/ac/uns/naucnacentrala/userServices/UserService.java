package rs.ac.uns.naucnacentrala.userServices;

import rs.ac.uns.naucnacentrala.dto.CasopisDTO;
import rs.ac.uns.naucnacentrala.dto.CorrectionDTO;
import rs.ac.uns.naucnacentrala.dto.UredniciRecenzentiDTO;

import java.util.List;

public interface UserService {
    public List<CasopisDTO> getMyPapers(String username);
    public CorrectionDTO getCorrectionData(Long casopisId);
    public UredniciRecenzentiDTO getUrednikRecenzent(Long casopisId, String username);
}
