package com.example.webshop.sendArtical.camunda;

import lombok.NoArgsConstructor;
import org.camunda.bpm.engine.impl.form.type.AbstractFormFieldType;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.TypedValue;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class ChooseScienceSelection extends AbstractFormFieldType {

    public static final String TYPE_NAME = "oblast-select";
    protected Map<Long, String> values=new HashMap<>();

    public ChooseScienceSelection(Map<Long, String> values) {
        this.values = values;
    }

    @Override
    public String getName() {
        return TYPE_NAME;
    }

    @Override
    public TypedValue convertToFormValue(TypedValue typedValue) {
        return Variables.objectValue(typedValue.getValue(),false).create();
    }

    @Override
    public TypedValue convertToModelValue(TypedValue typedValue) {
        return Variables.objectValue(typedValue.getValue(),false).create();
    }

    public Object getInformation(String key) {
        return key.equals("values") ? this.values : null;
    }


    public Map<Long, String> getValues() {
        return this.values;
    }

    @Override
    public Object convertFormValueToModelValue(Object o) {
        return null;
    }

    @Override
    public String convertModelValueToFormValue(Object o) {
        return null;
    }
}