package com.example.webshop.dto;

import com.example.webshop.model.Koautor;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDTO {

    private String naslov;
    private String apstrakt;
    private String kljucniPojmovi;
    private String naucnaOblast;
    private String pdf;
    private List<Koautor> koautori;
}
