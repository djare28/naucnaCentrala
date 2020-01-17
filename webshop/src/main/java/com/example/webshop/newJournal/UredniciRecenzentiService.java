package com.example.webshop.newJournal;

import com.example.webshop.dto.UredniciRecenzentiDTO;
import com.example.webshop.model.Casopis;
import com.example.webshop.model.Korisnik;
import com.example.webshop.newUser.KorisnikRepository;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UredniciRecenzentiService implements JavaDelegate {

    @Autowired
    CasopisRepository casopisRepository;

    @Autowired
    KorisnikRepository korisnikRepository;

    @Autowired
    IdentityService identityService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        List<Korisnik> urednici = new ArrayList<>();
        List<Korisnik> recenzenti = new ArrayList<>();

        Long casopisId = (Long)execution.getVariable("id");
        UredniciRecenzentiDTO urDTO = (UredniciRecenzentiDTO)execution.getVariable("uredniciRecenzenti");

        Casopis casopis = casopisRepository.getOne(casopisId);

        for (String r : urDTO.getRecenzenti()) {
            Korisnik recenzent = korisnikRepository.findOneByUsername(r);
            if(recenzent != null){
                recenzenti.add(recenzent);
                recenzent.getCasopisiRecenzent().add(casopis);
                korisnikRepository.save(recenzent);
            }
        }
        for (String u : urDTO.getUrednici()) {
            Korisnik urednik = korisnikRepository.findOneByUsername(u);
            if(urednik != null){
                urednici.add(urednik);
                urednik.getCasopisiUrednik().add(casopis);
                korisnikRepository.save(urednik);
            }
        }
        casopis.setRecenzenti(recenzenti);
        casopis.setUrednici(urednici);

        casopisRepository.save(casopis);
    }
}
