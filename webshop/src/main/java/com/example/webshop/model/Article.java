package com.example.webshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Article {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String naslov;

    @Column
    private String apstrakt;

    @Column
    private String kljucniPojmovi;

    @Column
    private String naucnaOblast;

    @Column
    private String pdf;

    @Column
    private Boolean enabled;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "casopis_koautor",
            joinColumns = @JoinColumn(name = "casopis_id"),
            inverseJoinColumns = @JoinColumn(name = "koautor_id"))
    private List<Koautor> koautori = new ArrayList<>();


}
