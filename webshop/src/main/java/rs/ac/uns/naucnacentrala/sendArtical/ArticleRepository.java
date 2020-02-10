package rs.ac.uns.naucnacentrala.sendArtical;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Article findOneById(Long id);

}
