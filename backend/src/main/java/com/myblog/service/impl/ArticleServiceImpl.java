package com.myblog.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.entity.Article;
import com.myblog.mapper.ArticleMapper;
import com.myblog.service.ArticleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Override
    public Page<Article> getArticlePage(Integer pageNum, Integer pageSize, String keyword, String category) {
        Page<Article> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        
        // 只查询已发布的文章
        wrapper.eq(Article::getStatus, 1);
        
        // 关键词搜索
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.and(w -> w
                    .like(Article::getTitle, keyword)
                    .or()
                    .like(Article::getSummary, keyword)
                    .or()
                    .like(Article::getTags, keyword)
            );
        }
        
        // 分类筛选
        if (StrUtil.isNotBlank(category)) {
            wrapper.eq(Article::getCategory, category);
        }
        
        // 排序：置顶优先，然后按创建时间倒序
        wrapper.orderByDesc(Article::getIsTop);
        wrapper.orderByDesc(Article::getCreateTime);
        
        return this.page(page, wrapper);
    }

    @Override
    public List<Article> getTopArticles() {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getIsTop, 1)
                .eq(Article::getStatus, 1)
                .orderByDesc(Article::getCreateTime)
                .last("LIMIT 10");
        return this.list(wrapper);
    }

    @Override
    public Article getArticleDetail(Long id) {
        return this.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void incrementViewCount(Long id) {
        LambdaUpdateWrapper<Article> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Article::getId, id)
                .setSql("view_count = view_count + 1");
        this.update(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateArticle(Article article) {
        if (article.getViewCount() == null) {
            article.setViewCount(0L);
        }
        if (article.getLikeCount() == null) {
            article.setLikeCount(0L);
        }
        if (article.getCommentCount() == null) {
            article.setCommentCount(0L);
        }
        if (article.getStatus() == null) {
            article.setStatus(1);
        }
        if (article.getIsTop() == null) {
            article.setIsTop(0);
        }
        return this.saveOrUpdate(article);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteArticle(Long id) {
        return this.removeById(id);
    }

    @Override
    public List<String> getAllCategories() {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Article::getCategory)
                .eq(Article::getStatus, 1)
                .groupBy(Article::getCategory);
        List<Article> articles = this.list(wrapper);
        return articles.stream()
                .map(Article::getCategory)
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toList());
    }
}