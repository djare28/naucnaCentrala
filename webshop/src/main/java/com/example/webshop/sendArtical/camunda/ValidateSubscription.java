package com.example.webshop.sendArtical.camunda;

import com.example.webshop.model.Korisnik;
import com.example.webshop.newUser.KorisnikRepository;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidateSubscription implements JavaDelegate {

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    KorisnikRepository korisnikRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        Korisnik korisnik = korisnikRepository.findOneByUsername("djare28");

        delegateExecution.setVariable("autor_pretplacen", korisnik.getPlatioClanarinu());
    }
}
