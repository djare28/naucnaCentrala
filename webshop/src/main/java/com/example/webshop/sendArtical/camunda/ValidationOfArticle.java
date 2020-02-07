package com.example.webshop.sendArtical.camunda;

import com.example.webshop.common.utils.ObjectMapperUtils;
import com.example.webshop.dto.ArticleDTO;
import com.example.webshop.model.Article;
import com.example.webshop.sendArtical.ArticleRepository;
import com.example.webshop.sendArtical.ArticleService;
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
public class ValidationOfArticle implements JavaDelegate {

    @Autowired
    ArticleService articleService;

    @Autowired
    ArticleRepository articleRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception{
        ValidatorFactory validatorFactory= Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        ArticleDTO article = articleService.getArticalFromProcessVariables(execution.getProcessInstanceId());
        Set<ConstraintViolation<ArticleDTO>> violations = validator.validate(article);
        boolean flag_val=true;
        HashMap<String,String> valErrors=new HashMap<String,String>();
        for (ConstraintViolation<ArticleDTO> violation : violations) {
            flag_val=false;
            valErrors.put(violation.getPropertyPath().toString(),violation.getMessage());
        }
        if(!flag_val) {
            execution.setVariable("validationErrors", valErrors);
        }else{
            if(article.getNaslov()==null){
                flag_val = false;
                valErrors.put("naslov", "Can not be empty");
                execution.setVariable("validationErrors", valErrors);
            }else if(article.getApstrakt()==null){
                flag_val = false;
                valErrors.put("apstrakt", "Can not be empty");
                execution.setVariable("validationErrors", valErrors);
            }else if(article.getPdf()==null){
                flag_val = false;
                valErrors.put("pdf", "Can not be empty");
                execution.setVariable("validationErrors", valErrors);
            }else {
                execution.setVariable("validationErrors", null);
                Article realArticle = ObjectMapperUtils.map(article, Article.class);
                realArticle.setEnabled(false);
                realArticle = articleRepository.save(realArticle);
                execution.setVariable("article", realArticle.getId());
            }
        }
        execution.setVariable("flag_r",flag_val);
    }
}
