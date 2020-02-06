package com.example.webshop.sendArtical.camunda;

import com.example.webshop.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Article findOneById(Long id);

}
