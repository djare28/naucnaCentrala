package rs.ac.uns.naucnacentrala.camunda.journal;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.naucnacentrala.model.Casopis;
import rs.ac.uns.naucnacentrala.model.CasopisStatus;
import rs.ac.uns.naucnacentrala.model.User;
import rs.ac.uns.naucnacentrala.newJournal.CasopisRepository;
import rs.ac.uns.naucnacentrala.newUser.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class ActivateJournalValidationTask implements JavaDelegate {

    @Autowired
    CasopisRepository casopisRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Casopis casopis=casopisRepository.findByProcessInstanceId(execution.getProcessInstanceId());
        ArrayList<String> uredniciIds= (ArrayList<String>) execution.getVariable("uredniciSel");
        ArrayList<String> userIds= (ArrayList<String>) execution.getVariable("recezentiSel");
        boolean flag_val=true;
        if(userIds.size()<2){
            HashMap<String,String> valErros=new HashMap<>();
            flag_val=false;
            valErros.put("recezentiSel","You have to select at least 2 reviewers");
            execution.setVariable("validationErrors",valErros);
        }else {
            for (String id : uredniciIds) {
                User user = userRepository.getOne(Long.valueOf(id));
                user.getUredjujemCasopise().add(casopis);
                casopis.setCasopisStatus(CasopisStatus.WAITING_FOR_APPROVAL);
                casopis.getUrednici().add(user);
                casopisRepository.save(casopis);
            }
            for (String id : userIds) {
                User user = userRepository.getOne(Long.valueOf(id));
                user.getRecenziramCasopise().add(casopis);
                casopis.setCasopisStatus(CasopisStatus.WAITING_FOR_APPROVAL);
                casopis.getRecezenti().add(user);
                casopisRepository.save(casopis);
            }
        }
        execution.setVariable("flag_val",flag_val);
    }

}
