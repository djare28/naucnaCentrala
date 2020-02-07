package com.example.webshop.sendArtical;

import com.example.webshop.dto.ArticleDTO;
import com.example.webshop.dto.KoautorDTO;
import com.example.webshop.model.Koautor;

public interface ArticleService {

    public abstract ArticleDTO getArticalFromProcessVariables(String processInstanceID);

    public abstract KoautorDTO getCoAuthorFromProcessVariables(String processInstanceID);

}
