package com.myblog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myblog.common.Result;
import com.myblog.entity.Comment;
import com.myblog.service.CommentService;
import com.myblog.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private JwtUtil jwtUtil;

    // 前台接口 - 获取文章的评论列表
    @GetMapping("/list/{articleId}")
    public Result<List<Comment>> getCommentsByArticleId(@PathVariable Long articleId) {
        List<Comment> comments = commentService.getCommentsByArticleId(articleId);
        return Result.success(comments);
    }

    // 前台接口 - 添加评论
    @PostMapping("/add")
    public Result<Boolean> addComment(@RequestBody Comment comment) {
        if (comment.getArticleId() == null) {
            return Result.error("文章ID不能为空");
        }
        if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
            return Result.error("评论内容不能为空");
        }

        boolean success = commentService.addComment(comment);
        if (success) {
            return Result.success(true);
        } else {
            return Result.error("评论失败");
        }
    }

    // 后台接口 - 分页获取评论列表
    @GetMapping("/admin/list")
    public Result<List<Comment>> getAdminCommentList(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long articleId
    ) {
        if (!validateToken(token)) {
            return Result.unauthorized();
        }

        Page<Comment> page = commentService.getCommentPage(pageNum, pageSize, articleId);
        return Result.success(page.getRecords(), page.getTotal());
    }

    // 后台接口 - 删除评论
    @DeleteMapping("/admin/{id}")
    public Result<Boolean> deleteComment(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long id
    ) {
        if (!validateToken(token)) {
            return Result.unauthorized();
        }

        boolean success = commentService.deleteComment(id);
        if (success) {
            return Result.success(true);
        } else {
            return Result.error("删除失败");
        }
    }

    // 后台接口 - 审核评论
    @PostMapping("/admin/approve/{id}")
    public Result<Boolean> approveComment(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long id
    ) {
        if (!validateToken(token)) {
            return Result.unauthorized();
        }

        boolean success = commentService.approveComment(id);
        if (success) {
            return Result.success(true);
        } else {
            return Result.error("审核失败");
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