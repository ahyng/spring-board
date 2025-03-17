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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

        return "redirect:/articles/" + saved.getId();
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

    @GetMapping("/articles/{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model) {
        // 수정할 데이터 가져오기
        Article articleEntity = articleRepository.findById(id).orElse(null);

        //모델에 데이터 등록
        model.addAttribute("article", articleEntity);

        return "articles/edit";
    }

    @PostMapping("/articles/update")
    public String update(ArticleForm form) {
        log.info(form.toString());
        // 1. DTO를 Entity로 변환
        Article articleEntity = form.toEntity();
        log.info(articleEntity.toString());
        // 2. Entity를 DB에 저장
        // DB에서 기존 데이터 가져오기
        Article target = articleRepository.findById(articleEntity.getId()).orElse(null);
        // 데이터 갱신
        if (target != null) {
            articleRepository.save(articleEntity);
        }
        // 3. 수정 결과 페이지로 리다이렉트
        return "redirect:/articles/" + articleEntity.getId();
    }

    @GetMapping("/articles/{id}/delete")
    public String delete(@PathVariable("id") Long id, RedirectAttributes rttr) {
        log.info("삭제 요청이 들어왔습니다.");
        // 1. 삭제할 대상 가져오기
        Article target = articleRepository.findById(id).orElse(null);
        log.info(target.toString());
        // 2. 대상 엔티티 삭제
        if (target != null) {
            articleRepository.delete(target);
            rttr.addFlashAttribute("msg", "삭제되었습니다!");
        }
        // 3. 결과 페이지로 리다이렉트
        return "redirect:/articles";
    }
}
