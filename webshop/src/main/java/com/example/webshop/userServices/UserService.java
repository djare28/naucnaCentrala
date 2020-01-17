package com.example.webshop.userServices;

import com.example.webshop.dto.CasopisDTO;
import com.example.webshop.dto.CorrectionDTO;
import com.example.webshop.dto.UredniciRecenzentiDTO;

import java.util.List;

public interface UserService {
    public List<CasopisDTO> getMyPapers(String username);
    public CorrectionDTO getCorrectionData(Long casopisId);
    public UredniciRecenzentiDTO getUrednikRecenzent(Long casopisId, String username);
}
