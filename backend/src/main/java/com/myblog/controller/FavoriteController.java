package com.myblog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myblog.common.Result;
import com.myblog.entity.Favorite;
import com.myblog.service.FavoriteService;
import com.myblog.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private JwtUtil jwtUtil;

    // 前台接口 - 切换收藏状态
    @PostMapping("/toggle")
    public Result<Map<String, Object>> toggleFavorite(@RequestBody Map<String, Long> params) {
        Long articleId = params.get("articleId");
        if (articleId == null) {
            return Result.error("文章ID不能为空");
        }

        boolean isFavoritedBefore = favoriteService.isFavorited(articleId);
        boolean success = favoriteService.toggleFavorite(articleId);
        
        if (!success) {
            return Result.error("操作失败");
        }

        boolean isFavoritedAfter = !isFavoritedBefore;
        Long count = favoriteService.getFavoriteCount(articleId);

        Map<String, Object> result = new HashMap<>();
        result.put("isFavorited", isFavoritedAfter);
        result.put("count", count);

        return Result.success(result);
    }

    // 前台接口 - 检查是否已收藏
    @GetMapping("/check/{articleId}")
    public Result<Map<String, Object>> checkFavorite(@PathVariable Long articleId) {
        boolean isFavorited = favoriteService.isFavorited(articleId);
        Long count = favoriteService.getFavoriteCount(articleId);

        Map<String, Object> result = new HashMap<>();
        result.put("isFavorited", isFavorited);
        result.put("count", count);

        return Result.success(result);
    }

    // 后台接口 - 分页获取收藏列表
    @GetMapping("/admin/list")
    public Result<Page<Favorite>> getAdminFavoriteList(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        if (!validateToken(token)) {
            return Result.unauthorized();
        }

        Page<Favorite> page = favoriteService.getFavoritePage(pageNum, pageSize);
        return Result.success(page);
    }

    private boolean validateToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return false;
        }
        String actualToken = token.substring(7);
        return jwtUtil.validateToken(actualToken);
    }
}