package com.example.springboot_study.controller;

import com.example.springboot_study.dto.ArticleForm;
import com.example.springboot_study.entity.Article;
import com.example.springboot_study.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

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
    public String show(@PathVariable("id") Long id, Model model) {
        log.info("id=" + id);
        // 1. id를 조회해 데이터 가져오기
        Optional<Article> articleEntity = Optional.ofNullable(articleRepository.findById(id).orElse(null));
        // 2. 모델에 데이터 등록
        articleEntity.ifPresent(article -> model.addAttribute("article", article));

        // 3. 뷰 페이지 반환
        return "articles/show";
    }

    @GetMapping("/articles")
    public String index(Model model) {
        // 1. 모든 데이터 가져오기
        Iterable<Article> articleEntityList = articleRepository.findAll();
        // 2. 모델에 데이터 등록
        model.addAttribute("articleList", articleEntityList);
        // 3. 뷰 페이지 설정
        return "articles/index";
    }
}
