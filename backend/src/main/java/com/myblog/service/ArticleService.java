package com.myblog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.entity.Article;

import java.util.List;

public interface ArticleService extends IService<Article> {

    Page<Article> getArticlePage(Integer pageNum, Integer pageSize, String keyword, String category);

    List<Article> getTopArticles();

    Article getArticleDetail(Long id);

    void incrementViewCount(Long id);

    boolean saveOrUpdateArticle(Article article);

    boolean deleteArticle(Long id);

    List<String> getAllCategories();
}