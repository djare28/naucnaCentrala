package com.example.webshop.sendArtical.camunda;

import com.example.webshop.common.utils.ObjectMapperUtils;
import com.example.webshop.dto.KoautorDTO;
import com.example.webshop.model.Article;
import com.example.webshop.model.Koautor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashMap;
import java.util.Set;

@Service
public class ValidationOfCoAuthor implements JavaDelegate {

    @Autowired
    KoautorRepository koautorRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    ArticleService articleService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        ValidatorFactory validatorFactory= Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        KoautorDTO koautor = articleService.getCoAuthorFromProcessVariables(execution.getProcessInstanceId());
        Set<ConstraintViolation<KoautorDTO>> violations = validator.validate(koautor);
        boolean flag_val=true;
        HashMap<String,String> valErrors=new HashMap<String,String>();
        for (ConstraintViolation<KoautorDTO> violation : violations) {
            flag_val=false;
            valErrors.put(violation.getPropertyPath().toString(),violation.getMessage());
        }
        if(!flag_val) {
            execution.setVariable("validationErrors", valErrors);
        }else{
            if(koautor.getIme()==null){
                flag_val = false;
                valErrors.put("ime", "Can not be empty");
                execution.setVariable("validationErrors", valErrors);
            }else if(koautor.getPrezime()==null){
                flag_val = false;
                valErrors.put("prezime", "Can not be empty");
                execution.setVariable("validationErrors", valErrors);
            }else if(koautor.getEmail()==null){
                flag_val = false;
                valErrors.put("email", "Can not be empty");
                execution.setVariable("validationErrors", valErrors);
            }else {
                execution.setVariable("validationErrors", null);
                Koautor ko = ObjectMapperUtils.map(koautor, Koautor.class);
                ko = koautorRepository.save(ko);

                String articleId = execution.getVariable("article").toString();
                Article article = articleRepository.findOneById(Long.parseLong(articleId));
                article.getKoautori().add(ko);
                article = articleRepository.save(article);

                //execution.setVariable("article", realArticle);
            }
        }
        execution.setVariable("flag_ko",flag_val);

    }
}
