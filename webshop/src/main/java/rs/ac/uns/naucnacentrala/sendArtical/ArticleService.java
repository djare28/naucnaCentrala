package rs.ac.uns.naucnacentrala.sendArtical;

import rs.ac.uns.naucnacentrala.dto.ArticleDTO;
import rs.ac.uns.naucnacentrala.dto.KoautorDTO;

public interface ArticleService {

    public abstract ArticleDTO getArticalFromProcessVariables(String processInstanceID);

    public abstract KoautorDTO getCoAuthorFromProcessVariables(String processInstanceID);

}
