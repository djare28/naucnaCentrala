package com.example.webshop.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class NacinPlacanja {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "naciniPlacanja")
    private List<Casopis> casopisi = new ArrayList<>();

    @Column(name = "naziv", unique = true, nullable = false)
    private String naziv;

}
