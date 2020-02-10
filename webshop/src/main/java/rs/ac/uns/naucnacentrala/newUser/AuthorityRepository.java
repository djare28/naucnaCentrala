package rs.ac.uns.naucnacentrala.newUser;

import rs.ac.uns.naucnacentrala.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;



public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findOneByName(String name);
}
