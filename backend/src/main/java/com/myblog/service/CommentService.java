package com.myblog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.entity.Comment;

import java.util.List;

public interface CommentService extends IService<Comment> {

    List<Comment> getCommentsByArticleId(Long articleId);

    Page<Comment> getCommentPage(Integer pageNum, Integer pageSize, Long articleId);

    boolean addComment(Comment comment);

    boolean deleteComment(Long id);

    boolean approveComment(Long id);
}