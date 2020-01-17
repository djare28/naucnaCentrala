package com.example.webshop.newUser;

import com.example.webshop.model.Korisnik;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivateUserService implements JavaDelegate {
    @Autowired
    KorisnikRepository korisnikRepository;

    @Autowired
    IdentityService identityService;

    @Override
    public void execute(DelegateExecution execution) {

        Long userId = (Long)execution.getVariable("id");

        Korisnik korisnik = korisnikRepository.getOne(userId);
        korisnik.setAktiviran(true);
        korisnikRepository.save(korisnik);
    }
}
