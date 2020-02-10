package rs.ac.uns.naucnacentrala.newJournal;

import rs.ac.uns.naucnacentrala.model.Casopis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CasopisRepository extends JpaRepository<Casopis, Long> {

    @Query(value = "SELECT MAX(c.issn) FROM Casopis c")
    Long findMaxIssn();

    List<Casopis> findAll();

    Casopis findOneByNaziv(String naziv);
}
