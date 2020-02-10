package rs.ac.uns.naucnacentrala.controller;

import rs.ac.uns.naucnacentrala.common.utils.CamundaUtils;
import rs.ac.uns.naucnacentrala.dto.FormSubmissionDto;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.variable.value.BooleanValue;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/restapi/bpmn")
@CrossOrigin("*")
public class CamundaController {

    private static final String VALIDATION_ERRORS_VARIABLE="validationErrors";
    private static final String VALIDATION_FLAG_VARIABLE="flag_val";

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    CamundaUtils camundaUtils;

    @Autowired
    IdentityService identityService;

    @Autowired
    TaskService taskService;

    @Autowired
    FormService formService;


    @PreAuthorize("hasRole('UREDNIK')")
    @RequestMapping(method = RequestMethod.GET, value = "/task/active/{processInstanceId}")
    public ResponseEntity getActiveTask(@PathVariable String processInstanceId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        identityService.setAuthenticatedUserId(auth.getName());

        Task task = taskService.createTaskQuery().active().processInstanceId(processInstanceId).singleResult();
        if (task.getAssignee().equals(auth.getName())) {
            return ResponseEntity.ok().body(camundaUtils.createFormDTO(task, processInstanceId));
        }else{
            return  ResponseEntity.status(404).build();
        }
    }


    @RequestMapping(method = RequestMethod.POST, value = "/form/{taskId}")
    public ResponseEntity post(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
        Task task=taskService.createTaskQuery().taskId(taskId).singleResult();
        HashMap<String, Object> map = camundaUtils.mapListToDto(dto);
        String processInstanceId=task.getProcessInstanceId();
        formService.submitTaskForm(taskId, map);


        Execution execution=runtimeService.createExecutionQuery().processInstanceId(processInstanceId).singleResult();
        if(execution==null){
            return  ResponseEntity.status(201).build();
        }else {
            BooleanValue validationValue = runtimeService.getVariableTyped(processInstanceId, VALIDATION_FLAG_VARIABLE);
            if (validationValue.getValue()) {
                return new ResponseEntity<>(HttpStatus.CREATED);
            } else {
                ObjectValue valErrorsVal = runtimeService.getVariableTyped(task.getProcessInstanceId(), VALIDATION_ERRORS_VARIABLE);
                HashMap<String, String> valErrors = (HashMap<String, String>) valErrorsVal.getValue();
                Task activeTask = taskService.createTaskQuery().processInstanceId(processInstanceId).active().list().get(0);
                valErrors.put("taskID", activeTask.getId());
                return ResponseEntity.badRequest().body(valErrors);
            }
        }
    }

    @PreAuthorize("hasRole('UREDNIK') or hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.POST, value = "/protected/form/{taskId}")
    public ResponseEntity postProtected(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        identityService.setAuthenticatedUserId(auth.getName());

        HashMap<String, Object> map = camundaUtils.mapListToDto(dto);
        Task task=taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId=task.getProcessInstanceId();
        if(task.getAssignee().equals(auth.getName())) {
            formService.submitTaskForm(taskId, map);
        }else{
            return ResponseEntity.status(403).build();
        }


        Execution execution=runtimeService.createExecutionQuery().processInstanceId(processInstanceId).singleResult();
        if(execution==null){
            return  ResponseEntity.status(201).build();
        }else {
            BooleanValue validationValue = runtimeService.getVariableTyped(processInstanceId, VALIDATION_FLAG_VARIABLE);
            if (validationValue.getValue()) {
                return new ResponseEntity<>(HttpStatus.CREATED);
            } else {
                ObjectValue valErrorsVal = runtimeService.getVariableTyped(task.getProcessInstanceId(), VALIDATION_ERRORS_VARIABLE);
                HashMap<String, String> valErrors = (HashMap<String, String>) valErrorsVal.getValue();
                Task activeTask = taskService.createTaskQuery().processInstanceId(processInstanceId).active().list().get(0);
                valErrors.put("taskID", activeTask.getId());
                return ResponseEntity.badRequest().body(valErrors);
            }
        }
    }


}
