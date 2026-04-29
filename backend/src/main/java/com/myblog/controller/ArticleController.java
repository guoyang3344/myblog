package com.myblog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myblog.common.Result;
import com.myblog.entity.Article;
import com.myblog.service.ArticleService;
import com.myblog.service.FavoriteService;
import com.myblog.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private JwtUtil jwtUtil;

    // 前台接口 - 获取文章列表
    @GetMapping("/list")
    public Result<List<Article>> getArticleList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category
    ) {
        Page<Article> page = articleService.getArticlePage(pageNum, pageSize, keyword, category);
        return Result.success(page.getRecords(), page.getTotal());
    }

    // 前台接口 - 获取文章详情
    @GetMapping("/{id}")
    public Result<Map<String, Object>> getArticleDetail(@PathVariable Long id) {
        Article article = articleService.getArticleDetail(id);
        if (article == null || article.getStatus() != 1) {
            return Result.error("文章不存在");
        }

        // 增加浏览量
        articleService.incrementViewCount(id);
        article.setViewCount(article.getViewCount() + 1);

        // 检查是否已收藏
        boolean isFavorited = favoriteService.isFavorited(id);

        Map<String, Object> result = new HashMap<>();
        result.put("article", article);
        result.put("isFavorited", isFavorited);

        return Result.success(result);
    }

    // 前台接口 - 获取置顶文章
    @GetMapping("/top")
    public Result<List<Article>> getTopArticles() {
        List<Article> articles = articleService.getTopArticles();
        return Result.success(articles);
    }

    // 前台接口 - 获取所有分类
    @GetMapping("/categories")
    public Result<List<String>> getCategories() {
        List<String> categories = articleService.getAllCategories();
        return Result.success(categories);
    }

    // 后台接口 - 分页获取文章列表（包括草稿）
    @GetMapping("/admin/list")
    public Result<List<Article>> getAdminArticleList(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category
    ) {
        if (!validateToken(token)) {
            return Result.unauthorized();
        }

        // 后台查询不限制状态
        Page<Article> page = new Page<>(pageNum, pageSize);
        // 这里简化处理，实际项目中应该有单独的后台查询方法
        page = articleService.getArticlePage(pageNum, pageSize, keyword, category);
        
        return Result.success(page.getRecords(), page.getTotal());
    }

    // 后台接口 - 获取文章详情（包括草稿）
    @GetMapping("/admin/{id}")
    public Result<Article> getAdminArticleDetail(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long id
    ) {
        if (!validateToken(token)) {
            return Result.unauthorized();
        }

        Article article = articleService.getArticleDetail(id);
        if (article == null) {
            return Result.error("文章不存在");
        }

        return Result.success(article);
    }

    // 后台接口 - 保存或更新文章
    @PostMapping("/admin/save")
    public Result<Boolean> saveArticle(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody Article article
    ) {
        if (!validateToken(token)) {
            return Result.unauthorized();
        }

        boolean success = articleService.saveOrUpdateArticle(article);
        if (success) {
            return Result.success(true);
        } else {
            return Result.error("保存失败");
        }
    }

    // 后台接口 - 删除文章
    @DeleteMapping("/admin/{id}")
    public Result<Boolean> deleteArticle(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long id
    ) {
        if (!validateToken(token)) {
            return Result.unauthorized();
        }

        boolean success = articleService.deleteArticle(id);
        if (success) {
            return Result.success(true);
        } else {
            return Result.error("删除失败");
        }
    }

    private boolean validateToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return false;
        }
        String actualToken = token.substring(7);
        return jwtUtil.validateToken(actualToken);
    }
}