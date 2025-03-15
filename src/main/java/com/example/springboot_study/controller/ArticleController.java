package com.example.springboot_study.controller;

import com.example.springboot_study.dto.ArticleForm;
import com.example.springboot_study.entity.Article;
import com.example.springboot_study.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/articles/new")
    public String newArticleForm() {
        return "articles/new";
    }

    @PostMapping("/articles/create")
    public String createArticle(ArticleForm form) {
        System.out.println(form.toString());
        // 1. DTO를 엔티티로 변환
        Article article = form.toEntity();
        log.info(form.toString());
//        System.out.println(article.toString());

        // 2. 레파지토리로 엔티티를 DB에 저장
        Article saved = articleRepository.save(article);
        log.info(saved.toString());
//        System.out.println(saved.toString());

        return "articles/new";
    }

    @GetMapping("/articles/{id}")
    public String show(@PathVariable("id") Long id) {
        log.info("id=" + id);
        return "";
    }
}
