package com.example.webshop.newJournal;

import com.example.webshop.dto.FormSubmissionDto;
import com.example.webshop.model.*;
import com.example.webshop.newUser.KorisnikRepository;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ValidateNewPaperService  implements JavaDelegate {
    @Autowired
    CasopisRepository casopisRepository;

    @Autowired
    NacinPlacanjaRepository nacinPlacanjaRepository;

    @Autowired
    IdentityService identityService;

    @Autowired
    KorisnikRepository korisnikRepository;

    @Autowired
    NaucnaOblastRepository naucnaOblastRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        List<FormSubmissionDto> noviCasopisForma = (List<FormSubmissionDto>)execution.getVariable("noviCasopisForma");

        boolean validation = true;

        Casopis casopis = new Casopis();
        casopis.setAktiviran(false);

        for (FormSubmissionDto formField : noviCasopisForma) {
            if(formField.getFieldId().equals("id")){
                casopis.setId(Long.parseLong(formField.getFieldValue(), 10));
            }
            if(formField.getFieldId().equals("naziv")) {
                if(formField.getFieldValue()==null || formField.getFieldValue().trim().equals("")){
                    validation = false;
                    System.out.println(1);
                    break;
                }
                else {
                    casopis.setNaziv(formField.getFieldValue());
                }
            }
            if(formField.getFieldId().equals("clanarina")) {
                if(formField.getFieldValue()==null || formField.getFieldValue().trim().equals("")){
                    validation = false;
                    System.out.println(2);
                    break;
                }
                else {
                    try{
                        Double d = Double.parseDouble(formField.getFieldValue());
                        casopis.setClanarina(d);
                    }catch (Exception e){
                        validation = false;
                        System.out.println(3);
                        break;
                    }
                }
            }
            if(formField.getFieldId().equals("komeSeNaplacuje")) {
                if(!formField.getFieldValue().equals("Čitalac") && !formField.getFieldValue().equals("Autor")){
                    validation = false;
                    System.out.println(4);
                    break;
                }
                else {
                    casopis.setKomeSeNaplacuje(formField.getFieldValue());
                }
            }

            if(formField.getFieldId().contains("naucnaOblast")){
                if(formField.getFieldValue()==null || formField.getFieldValue().trim().equals("")){
                    validation = false;
                    System.out.println(5);
                    break;
                }
                else {
                    NaucnaOblast naucnaOblast = naucnaOblastRepository.getOne(Long.parseLong(formField.getFieldValue(), 10));
                    casopis.getNaucneOblasti().add(naucnaOblast);
                    naucnaOblast.getCasopisi().add(casopis);
                    naucnaOblastRepository.save(naucnaOblast);
                }
            }

            if(formField.getFieldId().contains("nacinPlacanja")){
                if(formField.getFieldValue()==null || formField.getFieldValue().trim().equals("")){
                    validation = false;
                    System.out.println(6);
                    break;
                }
                else {
                    NacinPlacanja nacinPlacanja = nacinPlacanjaRepository.getOne(Long.parseLong(formField.getFieldValue(), 10));
                    casopis.getNaciniPlacanja().add(nacinPlacanja);
                    nacinPlacanja.getCasopisi().add(casopis);
                    nacinPlacanjaRepository.save(nacinPlacanja);
                }
            }
        }

        if(casopis.getNaucneOblasti().size()==0){
            validation=false;
            System.out.println(7);
        }else{
            String naucneOblasti = "";
            for (NaucnaOblast no:casopis.getNaucneOblasti()) {
                if(naucneOblasti.equals("")){
                    naucneOblasti+=no.getNaziv();
                }else{
                    naucneOblasti+=", "+no.getNaziv();
                }
            }
            execution.setVariable("naucneOblasti",naucneOblasti);
        }

        if(casopis.getNaciniPlacanja().size()==0){
            validation=false;
            System.out.println(8);
        }else{
            String naciniPlacanja = "";
            for (NacinPlacanja np:casopis.getNaciniPlacanja()) {
                if(naciniPlacanja.equals("")){
                    naciniPlacanja+=np.getNaziv();
                }else{
                    naciniPlacanja+=", "+np.getNaziv();
                }
            }
            execution.setVariable("naciniPlacanja",naciniPlacanja);
        }

        if(validation==false){
            execution.setVariable("validacija", false);
            return;
        }

        String username = (String)execution.getVariable("starterIdVariable");
        Korisnik glavniUrednik = korisnikRepository.findOneByUsername(username);
        casopis.setGlavniUrednik(glavniUrednik);

        if(casopis.getId()!=null){
            Casopis oldCasopis = casopisRepository.getOne(casopis.getId());
            casopis.setRecenzenti(new ArrayList<>());
            casopis.setUrednici(new ArrayList<>());
            casopis.setIssn(oldCasopis.getIssn());
        }else{
            Long maxIssn = casopisRepository.findMaxIssn();
            if(maxIssn == null){
                maxIssn = 10000000L;
            }
            casopis.setIssn(maxIssn+1);
            casopis.setProcessInstanceId(execution.getProcessInstanceId());
        }

        casopis = casopisRepository.save(casopis);

        execution.setVariable("id", casopis.getId());
        execution.setVariable("validacija", true);

        System.out.println((Long)execution.getVariable("id"));
    }
}
