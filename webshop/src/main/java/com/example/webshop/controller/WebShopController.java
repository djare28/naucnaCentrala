package com.example.webshop.controller;

import com.example.webshop.dto.*;
import com.example.webshop.model.NacinPlacanja;
import com.example.webshop.model.NaucnaOblast;
import com.example.webshop.newJournal.NacinPlacanjaRepository;
import com.example.webshop.newJournal.NaucnaOblastRepository;
import com.example.webshop.userServices.UserService;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/webshop")
@CrossOrigin("*")
public class WebShopController {
    @Autowired
    NaucnaOblastRepository naucnaOblastRepository;

    @Autowired
    IdentityService identityService;

    @Autowired
    UserService userService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    FormService formService;

    @Autowired
    NacinPlacanjaRepository nacinPlacanjaRepository;


    @GetMapping(path = "/registration", produces = "application/json")
    public @ResponseBody FormFieldsDto registrationStart() {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("registracija");

        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);

        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();
        for(FormField fp : properties) {
            System.out.println(fp.getId() + fp.getType());
        }

        return new FormFieldsDto(task.getId(), pi.getId(), properties);
    }

    @PreAuthorize("hasRole('UREDNIK')")
    @GetMapping(path = "/newPaper/{username}", produces = "application/json")
    public @ResponseBody FormFieldsDto newPaperStart(@PathVariable String username) {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("noviCasopis");
        runtimeService.setVariable(pi.getId(), "starterIdVariable", username);

        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);

        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();
        for(FormField fp : properties) {
            System.out.println(fp.getId() + fp.getType());
        }

        return new FormFieldsDto(task.getId(), pi.getId(), properties);
    }

    @PostMapping(path = "/registration/{taskId}", produces = "application/json")
    public @ResponseBody String registrationSubmit(@PathVariable String taskId, @RequestBody List<FormSubmissionDto> dto) {

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        HashMap<String, Object> map = this.mapListToDto(dto);

        runtimeService.setVariable(processInstanceId, "registracionaForma", dto);
        formService.submitTaskForm(taskId, map);

        return "\""+processInstanceId+"\"";
    }

    @PreAuthorize("hasRole('UREDNIK')")
    @PostMapping(path = "/newPaper/{processId}", produces = "application/json")
    public @ResponseBody String newPaperSubmit(@PathVariable String processId, @RequestBody List<FormSubmissionDto> dto) {

        Task task = taskService.createTaskQuery().processInstanceId(processId).list().get(0);

        HashMap<String, Object> map = this.mapListToDto(dto);

        runtimeService.setVariable(processId, "noviCasopisForma", dto);
        formService.submitTaskForm(task.getId(), map);

        return "\""+processId+"\"";
    }

    @PreAuthorize("hasRole('UREDNIK')")
    @PostMapping(path = "/newPaper/uredniciRecenzenti/{processId}", produces = "application/json")
    public @ResponseBody Boolean newPaperUredniciRecenzentiSubmit( @PathVariable String processId, @RequestBody UredniciRecenzentiDTO dto) {
        Task task = taskService.createTaskQuery().processInstanceId(processId).list().get(0);

        for (String s:dto.getRecenzenti()
             ) {
            System.out.println(s);
        }

        HashMap<String, Object> map = new HashMap<>();

        if(dto.getRecenzenti().size() < 2){
            return false;
        }

        map.put("recenzenti",dto.getRecenzenti());
        map.put("urednici",dto.getUrednici());

        runtimeService.setVariable(processId, "uredniciRecenzenti", dto);
        formService.submitTaskForm(task.getId(), map);

        return true;
    }

    @GetMapping(path = "/sciencefields", produces = "application/json")
    public @ResponseBody List<NaucnaOblastDTO> getNaucneOblasti(){
        List<NaucnaOblastDTO> ret = new ArrayList<>();

        List<NaucnaOblast> noList = naucnaOblastRepository.findAll();

        for (NaucnaOblast no : noList) {
            NaucnaOblastDTO noDTO = new NaucnaOblastDTO();
            noDTO.setNaziv(no.getNaziv());
            noDTO.setId(no.getId());

            ret.add(noDTO);
        }

        return ret;
    }

    @GetMapping(path = "/payments", produces = "application/json")
    public @ResponseBody List<NacinPlacanjaDTO> getNacinePlacanja(){
        List<NacinPlacanjaDTO> ret = new ArrayList<>();

        List<NacinPlacanja> npList = nacinPlacanjaRepository.findAll();

        for (NacinPlacanja np : npList) {
            NacinPlacanjaDTO npDTO = new NacinPlacanjaDTO();
            npDTO.setNaziv(np.getNaziv());
            npDTO.setId(np.getId());

            ret.add(npDTO);
        }

        return ret;
    }

    @GetMapping(path = "/registration/validation/{processId}", produces = "application/json")
    public @ResponseBody Boolean getValidation(@PathVariable String processId) {
        return (Boolean)runtimeService.getVariable(processId, "validacija");
    }

    private HashMap<String, Object> mapListToDto(List<FormSubmissionDto> list)
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        String naciniPlacanja = "";
        String naucneOblasti = "";
        for(FormSubmissionDto temp : list){
            if(!temp.getFieldId().contains("naucnaOblast") || temp.getFieldId().contains("nacinPlacanja")) {
                map.put(temp.getFieldId(), temp.getFieldValue());
            }
            else if(temp.getFieldId().contains("naucnaOblast")){
                if(naucneOblasti.equals("")){
                    naucneOblasti+=temp.getFieldValue();
                }else{
                    naucneOblasti+=", "+temp.getFieldValue();
                }
            }
            else if(temp.getFieldId().contains("nacinPlacanja")){
                if(naciniPlacanja.equals("")){
                    naciniPlacanja+=temp.getFieldValue();
                }else{
                    naciniPlacanja+=", "+temp.getFieldValue();
                }
            }
        }
        if(!naucneOblasti.equals("")){
            map.put("naucneOblasti",naucneOblasti);
        }
        if(!naciniPlacanja.equals("")) {
            map.put("naciniPlacanja",naciniPlacanja);
        }

        return map;
    }

    @PostMapping(path = "/registration/confirmation/{processId}", produces = "application/json")
    public @ResponseBody ResponseEntity complete(@PathVariable String processId) {
        Task task = taskService.createTaskQuery().processInstanceId(processId).list().get(0);
        taskService.complete(task.getId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/tasks/{username}", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN') or hasRole('UREDNIK')")
    public @ResponseBody List<TaskDTO> getTasksForUser(@PathVariable String username) {
        List<TaskDTO> ret = new ArrayList<>();
        List<Task> taskList = taskService.createTaskQuery().taskAssignee(username).list();
        for (Task t:taskList) {
            TaskDTO tDTO = new TaskDTO();
            tDTO.setProcessId(t.getProcessInstanceId());
            tDTO.setInstanceId(t.getId());
            tDTO.setId(t.getTaskDefinitionKey());

            TaskFormData tfd = formService.getTaskFormData(t.getId());
            List<FormField> properties = tfd.getFormFields();
            for(FormField fp : properties) {
                if (fp.getId().equals("naziv")) {
                    tDTO.setNaziv((String)fp.getValue().getValue());
                }
                if (fp.getId().equals("glavniUrednik")) {
                    tDTO.setGlavniUrednik((String)fp.getValue().getValue());
                }
                if (fp.getId().equals("username")) {
                    tDTO.setUser((String)fp.getValue().getValue());
                }
                if (fp.getId().equals("clanarina")) {
                    tDTO.setClanarina((Long)fp.getValue().getValue());
                }
                if (fp.getId().equals("komeSeNaplacuje")) {
                    tDTO.setKomeSeNaplacuje((String)fp.getValue().getValue());
                }
                if (fp.getId().equals("recenzenti")) {
                    tDTO.setRecenzenti((String)fp.getValue().getValue());
                }
                if (fp.getId().equals("naucneOblasti")) {
                    tDTO.setNaucneOblasti((String)fp.getValue().getValue());
                }
                if (fp.getId().equals("naciniPlacanja")) {
                    tDTO.setNaciniPlacanja((String)fp.getValue().getValue());
                }
                if (fp.getId().equals("urednici")) {
                    tDTO.setUrednici((String)fp.getValue().getValue());
                }
            }

            ret.add(tDTO);
        }

        return ret;
    }

    @GetMapping(path = "/correction/{processId}", produces = "application/json")
    @PreAuthorize("hasRole('UREDNIK')")
    public @ResponseBody CorrectionDTO getCorrectionData(@PathVariable String processId) {

        Long casopisId = (Long)runtimeService.getVariable(processId,"id");

        CorrectionDTO correctionDTO = userService.getCorrectionData(casopisId);

        //ProcessInstance pi = runtimeService.startProcessInstanceByKey("noviCasopis");
        //runtimeService.setVariable(pi.getId(), "starterIdVariable", username);

        Task task = taskService.createTaskQuery().processInstanceId(processId).list().get(0);

        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();
        for(FormField fp : properties) {
            System.out.println(fp.getId() + fp.getType());
        }

        correctionDTO.setFormFields(properties);

        return correctionDTO;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/potvrdaRecenzenta", produces = "application/json")
    public @ResponseBody ResponseEntity potvrdaRecenzenta(@RequestBody PotvrdaRecenzentaDTO dto) {
        TaskFormData tfd = formService.getTaskFormData(dto.getTaskId());
        List<FormField> properties = tfd.getFormFields();
        HashMap<String, Object> map = new HashMap<String, Object>();
        for(FormField fp : properties) {
            if(fp.getId().equals("recenzentOdobren")){
                map.put("recenzentOdobren",dto.getAnswer());
            }
            else if(fp.getId().equals("username")){
                map.put("username",(String)fp.getValue().getValue());
            }
        }

        formService.submitTaskForm(dto.getTaskId(),map);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/potvrdaCasopisa", produces = "application/json")
    public @ResponseBody ResponseEntity potvrdaCasopisa(@RequestBody PotvrdaRecenzentaDTO dto) {
        TaskFormData tfd = formService.getTaskFormData(dto.getTaskId());
        List<FormField> properties = tfd.getFormFields();
        HashMap<String, Object> map = new HashMap<String, Object>();
        for(FormField fp : properties) {
            if(fp.getId().equals("odgovorAdmina")){
                map.put("odgovorAdmina",dto.getAnswer());
            }else if(fp.getId().equals("naziv")){
                map.put("naziv",(String)fp.getValue().getValue());
            }else if(fp.getId().equals("glavniUrednik")){
                map.put("glavniUrednik",(String)fp.getValue().getValue());
            }else if(fp.getId().equals("komeSeNaplacuje")){
                map.put("komeSeNaplacuje",(String)fp.getValue().getValue());
            }else if(fp.getId().equals("urednici")){
                map.put("urednici",(String)fp.getValue().getValue());
            }else if(fp.getId().equals("recenzenti")){
                map.put("recenzenti",(String)fp.getValue().getValue());
            }else if(fp.getId().equals("clanarina")){
                map.put("clanarina",(Long)fp.getValue().getValue());
            }else if(fp.getId().equals("naucneOblasti")){
                map.put("naucneOblasti",(String)fp.getValue().getValue());
            }else if(fp.getId().equals("naciniPlacanja")){
                map.put("naciniPlacanja",(String)fp.getValue().getValue());
            }
        }

        formService.submitTaskForm(dto.getTaskId(),map);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('UREDNIK')")
    @GetMapping(path = "/uredniciRecenzenti/{processId}", produces = "application/json")
    public @ResponseBody UredniciRecenzentiDTO getUrednikeRecenzente(@PathVariable String processId) {
        Long casopisId = (Long)runtimeService.getVariable(processId,"id");
        String username = (String)runtimeService.getVariable(processId,"starterIdVariable");

        UredniciRecenzentiDTO ret = userService.getUrednikRecenzent(casopisId,username);

        return ret;
    }

    @PreAuthorize("hasRole('UREDNIK')")
    @GetMapping(path = "/myPapers/{username}", produces = "application/json")
    public @ResponseBody List<CasopisDTO> getMyPapers(@PathVariable String username) {
        return userService.getMyPapers(username);
    }
}
