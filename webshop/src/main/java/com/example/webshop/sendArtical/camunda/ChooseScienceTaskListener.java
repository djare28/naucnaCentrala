package com.example.webshop.sendArtical.camunda;

import com.example.webshop.model.Casopis;
import com.example.webshop.model.NaucnaOblast;
import com.example.webshop.newJournal.CasopisRepository;
import com.example.webshop.newJournal.NaucnaOblastRepository;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ChooseScienceTaskListener implements TaskListener {

    @Autowired
    FormService formService;

    @Autowired
    NaucnaOblastRepository naucnaOblastRepository;

    @Override
    public void notify(DelegateTask delegateTask) {
        List<FormField> formFieldList = formService.getTaskFormData(delegateTask.getId()).getFormFields();
        if (formFieldList != null) {
            for (FormField field : formFieldList) {
                if (field.getId().equals("naucna_oblast")) {
                    Map<Long, String> items = ((ChooseJournalSelection) field.getType()).getValues();
                    for (NaucnaOblast naucnaOblast : naucnaOblastRepository.findAll()) {
                        items.put(naucnaOblast.getId(), naucnaOblast.getNaziv());
                    }
                }
            }
        }
    }
}