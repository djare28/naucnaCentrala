package rs.ac.uns.naucnacentrala.newPaper;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.naucnacentrala.model.Paper;

import java.util.List;

public interface PaperRepository extends JpaRepository<Paper, Long> {

    public Paper findOneById(Long id);
    public List<Paper> findAllByCasopis_Id(Long id);
}
