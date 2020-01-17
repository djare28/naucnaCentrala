package com.example.webshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Casopis {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "issn", unique = true, nullable = false)
    private Long issn;

    @Column(name = "clanarina", unique = false, nullable = false)
    private Double clanarina;

    @Column(name = "naziv", unique = false, nullable = false)
    private String naziv;

    @Column(name = "komeSeNaplacuje", unique = false, nullable = false)
    private String komeSeNaplacuje;


    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "casopis_naucna_oblast",
            joinColumns = @JoinColumn(name = "casopis_id"),
            inverseJoinColumns = @JoinColumn(name = "naucna_oblast_id"))
    private List<NaucnaOblast> naucneOblasti = new ArrayList<>();


    @ManyToOne(fetch = FetchType.EAGER)
    protected Korisnik glavniUrednik;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "casopis_urednik",
            joinColumns = @JoinColumn(name = "casopis_id"),
            inverseJoinColumns = @JoinColumn(name = "urednik_id"))
    private List<Korisnik> urednici = new ArrayList<>();


    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "casopis_recenzent",
            joinColumns = @JoinColumn(name = "casopis_id"),
            inverseJoinColumns = @JoinColumn(name = "recenzent_id"))
    private List<Korisnik> recenzenti = new ArrayList<>();

    @Column(name = "aktiviran", unique = false, nullable = false)
    private Boolean aktiviran;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "casopis_nacin_placanja",
            joinColumns = @JoinColumn(name = "casopis_id"),
            inverseJoinColumns = @JoinColumn(name = "nacin_placanja_id"))
    private List<NacinPlacanja> naciniPlacanja = new ArrayList<>();
}
