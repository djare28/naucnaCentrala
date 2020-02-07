package com.example.webshop.sendArtical;

import com.example.webshop.dto.ArticleDTO;
import com.example.webshop.dto.KoautorDTO;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.value.StringValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

@Service
public class ArticleServiceImpl implements ArticleService {

    private static final String[] variableArr={"naslov","apstrakt","kljucni_pojmovi","naucna_oblast","pdf"};
    private static final String[] koautorArr={"imeKo","prezimeKo","adresaKo","gradKo","drzavaKo", "emailKo"};

    private static final String VALIDATION_RESULT="validationErrors";

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Override
    public ArticleDTO getArticalFromProcessVariables(String processInstanceID) {

        ArrayList<String> variableNames=new ArrayList<String>(Arrays.asList(variableArr));
        VariableMap map=runtimeService.getVariablesTyped(processInstanceID,variableNames,true);
        ArticleDTO artical=new ArticleDTO();

        StringValue naslovVal=map.getValueTyped("naslov");
        artical.setNaslov(naslovVal.getValue());
        StringValue apstraktVal=map.getValueTyped("apstrakt");
        artical.setApstrakt(apstraktVal.getValue());
        StringValue kljucniPojmoviVal=map.getValueTyped("kljucni_pojmovi");
        artical.setKljucniPojmovi(kljucniPojmoviVal.getValue());
        StringValue naucnaOblastVal=map.getValueTyped("naucna_oblast");
        artical.setNaucnaOblast(naucnaOblastVal.getValue());
        StringValue pdfVal=map.getValueTyped("pdf");
        artical.setPdf(pdfVal.getValue());

        return artical;

    }

    @Override
    public KoautorDTO getCoAuthorFromProcessVariables(String processInstanceID) {

        ArrayList<String> variableNames=new ArrayList<String>(Arrays.asList(koautorArr));
        VariableMap map=runtimeService.getVariablesTyped(processInstanceID,variableNames,true);
        KoautorDTO koautor = new KoautorDTO();

        StringValue naslovVal=map.getValueTyped("imeKo");
        koautor.setIme(naslovVal.getValue());
        StringValue naslovVal1=map.getValueTyped("prezimeKo");
        koautor.setPrezime(naslovVal1.getValue());
        StringValue naslovVal2=map.getValueTyped("adresaKo");
        koautor.setAdresa(naslovVal2.getValue());
        StringValue naslovVal3=map.getValueTyped("gradKo");
        koautor.setGrad(naslovVal3.getValue());
        StringValue naslovVal4=map.getValueTyped("drzavaKo");
        koautor.setDrzava(naslovVal4.getValue());
        StringValue naslovVal5=map.getValueTyped("emailKo");
        koautor.setDrzava(naslovVal5.getValue());

        return koautor;
    }
}
