package com.example.webshop.sendArtical.camunda;

import com.example.webshop.model.Casopis;
import com.example.webshop.newJournal.CasopisRepository;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ChooseJournalTaskListener implements TaskListener {

    @Autowired
    FormService formService;

    @Autowired
    CasopisRepository casopisRepository;

    @Override
    public void notify(DelegateTask delegateTask) {
        List<FormField> formFieldList = formService.getTaskFormData(delegateTask.getId()).getFormFields();
        if (formFieldList != null) {
            for (FormField field : formFieldList) {
                if (field.getId().equals("selBox1")) {
                    Map<Long, String> items = ((ChooseJournalSelection) field.getType()).getValues();
                    for (Casopis casopis : casopisRepository.findAll()) {
                        items.put(casopis.getId(), casopis.getNaziv());
                    }
                }
            }
        }
    }
}
