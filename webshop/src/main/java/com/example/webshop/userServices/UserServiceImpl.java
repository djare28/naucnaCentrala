package com.example.webshop.userServices;

import com.example.webshop.dto.CasopisDTO;
import com.example.webshop.dto.CorrectionDTO;
import com.example.webshop.dto.UredniciRecenzentiDTO;
import com.example.webshop.model.*;
import com.example.webshop.newJournal.CasopisRepository;
import com.example.webshop.newUser.KorisnikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class UserServiceImpl implements  UserService {

    @Autowired
    KorisnikRepository korisnikRepository;

    @Autowired
    CasopisRepository casopisRepository;

    @Override
    public UredniciRecenzentiDTO getUrednikRecenzent(Long casopisId, String username){
        UredniciRecenzentiDTO ret = new UredniciRecenzentiDTO();
        ret.setUrednici(new ArrayList<>());
        ret.setRecenzenti(new ArrayList<>());

        HashMap<String,Korisnik> korisnici = new HashMap<>();

        Casopis casopis = casopisRepository.getOne(casopisId);
        for (NaucnaOblast naucnaOblast: casopis.getNaucneOblasti()) {
            for (Korisnik k : naucnaOblast.getKorisnici()) {
                try {
                    System.out.println(k.getId());
                    korisnici.put(k.getUsername(),k);
                }catch (Exception e){
                    continue;
                }
            }
        }

        for (Korisnik k:korisnici.values()) {
            Authority authority=((List<Authority>)k.getAuthorities()).get(0);
            System.out.println(authority.getName());
            if(authority.getName().equals("ROLE_UREDNIK") && !k.getUsername().equals(username)){
                ret.getUrednici().add(k.getUsername());
            }

            if(authority.getName().equals("ROLE_RECENZENT") && !k.getUsername().equals(username)){
                ret.getRecenzenti().add(k.getUsername());
                System.out.println("OVO JE RECENZENT");
            }
        }

        return ret;
    }

    @Override
    public CorrectionDTO getCorrectionData(Long casopisId){
        CorrectionDTO ret = new CorrectionDTO();
        Casopis casopis = casopisRepository.getOne(casopisId);

        ret.setNaziv(casopis.getNaziv());
        ret.setId(casopisId);
        ret.setKomeSeNaplacuje(casopis.getKomeSeNaplacuje());
        ret.setClanarina(casopis.getClanarina());
        for (NacinPlacanja np:casopis.getNaciniPlacanja()) {
            ret.getNaciniPlacanja().add(np.getId());
        }
        for (NaucnaOblast no:casopis.getNaucneOblasti()) {
            ret.getNaucneOblasti().add(no.getId());
        }

        return ret;
    }

    @Override
    public List<CasopisDTO> getMyPapers(String username){
        List<CasopisDTO> ret = new ArrayList<>();
        Korisnik korisnik = korisnikRepository.findOneByUsername(username);

        for (Casopis casopis:korisnik.getCasopisiGlavni()) {
            CasopisDTO dto = new CasopisDTO();
            dto.setNaziv(casopis.getNaziv());
            dto.setId(casopis.getId());
            dto.setIssn(casopis.getIssn());
            dto.setClanarina(casopis.getClanarina());
            dto.setKomeSeNaplacuje(casopis.getKomeSeNaplacuje());
            dto.setAktiviran(casopis.getAktiviran());

            ret.add(dto);
        }

        return ret;
    }
}
