package com.example.webshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Koautor {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ime;

    private String prezime;

    private String adresa;

    private String grad;

    private String Drzava;

    private String email;

    @JsonIgnore
    @ManyToMany(mappedBy = "koautori")
    private List<Article> articles = new ArrayList<>();

}
