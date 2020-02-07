package com.example.webshop.sendArtical.camunda;

import com.example.webshop.model.Casopis;
import com.example.webshop.newJournal.CasopisRepository;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.value.StringValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

@Service
public class WhoIsPaying implements JavaDelegate {

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    CasopisRepository casopisRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        ArrayList<String> variableNames=new ArrayList<String>(Arrays.asList("selBox1"));
        VariableMap map=runtimeService.getVariablesTyped(delegateExecution.getProcessInstanceId(),variableNames,true);
        StringValue value=map.getValueTyped("selBox1");

        Casopis casopis = casopisRepository.findOneByNaziv(value.toString());

        delegateExecution.setVariable("ko_placa",casopis.getKomeSeNaplacuje());
    }
}
