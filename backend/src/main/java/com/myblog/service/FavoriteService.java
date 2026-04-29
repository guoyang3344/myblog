package com.myblog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.entity.Favorite;

public interface FavoriteService extends IService<Favorite> {

    boolean toggleFavorite(Long articleId);

    boolean isFavorited(Long articleId);

    Long getFavoriteCount(Long articleId);

    Page<Favorite> getFavoritePage(Integer pageNum, Integer pageSize);
}