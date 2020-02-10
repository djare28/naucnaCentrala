package rs.ac.uns.naucnacentrala.dto;

import java.util.List;

import org.camunda.bpm.engine.form.FormField;

public class FormFieldsDto {
    String processInstanceId;
    String taskId;
    List<FormField> formFields;

    public FormFieldsDto(String taskId, String processInstanceId, List<FormField> formFields) {
        super();
        this.processInstanceId = processInstanceId;
        this.taskId = taskId;
        this.formFields = formFields;
    }

    public FormFieldsDto() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public List<FormField> getFormFields() {
        return formFields;
    }

    public void setFormFields(List<FormField> formFields) {
        this.formFields = formFields;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }


}