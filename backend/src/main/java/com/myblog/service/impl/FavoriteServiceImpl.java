package com.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.entity.Article;
import com.myblog.entity.Favorite;
import com.myblog.mapper.FavoriteMapper;
import com.myblog.service.ArticleService;
import com.myblog.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ArticleService articleService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean toggleFavorite(Long articleId) {
        String ip = getClientIp();
        
        // 检查是否已收藏
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getArticleId, articleId)
                .eq(Favorite::getIp, ip);
        Favorite existingFavorite = this.getOne(wrapper);
        
        if (existingFavorite != null) {
            // 已收藏，取消收藏
            boolean success = this.removeById(existingFavorite.getId());
            if (success) {
                // 更新文章点赞数
                updateArticleLikeCount(articleId, -1);
            }
            return success;
        } else {
            // 未收藏，添加收藏
            Favorite favorite = new Favorite();
            favorite.setArticleId(articleId);
            favorite.setIp(ip);
            boolean success = this.save(favorite);
            if (success) {
                // 更新文章点赞数
                updateArticleLikeCount(articleId, 1);
            }
            return success;
        }
    }

    @Override
    public boolean isFavorited(Long articleId) {
        String ip = getClientIp();
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getArticleId, articleId)
                .eq(Favorite::getIp, ip);
        return this.count(wrapper) > 0;
    }

    @Override
    public Long getFavoriteCount(Long articleId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getArticleId, articleId);
        return this.count(wrapper);
    }

    @Override
    public Page<Favorite> getFavoritePage(Integer pageNum, Integer pageSize) {
        Page<Favorite> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Favorite::getCreateTime);
        return this.page(page, wrapper);
    }

    private void updateArticleLikeCount(Long articleId, int delta) {
        Article article = articleService.getById(articleId);
        if (article != null) {
            long newLikeCount = article.getLikeCount() + delta;
            if (newLikeCount < 0) {
                newLikeCount = 0;
            }
            article.setLikeCount(newLikeCount);
            articleService.updateById(article);
        }
    }

    private String getClientIp() {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多个代理时取第一个IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}