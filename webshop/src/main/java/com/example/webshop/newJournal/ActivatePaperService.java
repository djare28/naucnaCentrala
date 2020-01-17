package com.example.webshop.newJournal;

import com.example.webshop.model.Casopis;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ActivatePaperService implements JavaDelegate {
    @Autowired
    CasopisRepository casopisRepository;

    @Autowired
    IdentityService identityService;


    @Override
    public void execute(DelegateExecution execution) {

        Long casopisId = (Long)execution.getVariable("id");

        Casopis casopis = casopisRepository.getOne(casopisId);
        casopis.setAktiviran(true);
        casopisRepository.save(casopis);
    }
}
