package com.example.webshop.newUser;

import com.example.webshop.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;



public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findOneByName(String name);
}
