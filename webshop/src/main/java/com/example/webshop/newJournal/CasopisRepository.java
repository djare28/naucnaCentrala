package com.example.webshop.newJournal;

import com.example.webshop.model.Casopis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface CasopisRepository extends JpaRepository<Casopis, Long> {

    @Query(value = "SELECT MAX(c.issn) FROM Casopis c")
    Long findMaxIssn();

    List<Casopis> findAll();
}
