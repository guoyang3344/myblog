package com.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.entity.Comment;
import com.myblog.mapper.CommentMapper;
import com.myblog.service.ArticleService;
import com.myblog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private HttpServletRequest request;

    @Override
    public List<Comment> getCommentsByArticleId(Long articleId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getArticleId, articleId)
                .eq(Comment::getStatus, 1)
                .orderByAsc(Comment::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public Page<Comment> getCommentPage(Integer pageNum, Integer pageSize, Long articleId) {
        Page<Comment> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        
        if (articleId != null) {
            wrapper.eq(Comment::getArticleId, articleId);
        }
        
        wrapper.orderByDesc(Comment::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addComment(Comment comment) {
        // 获取IP地址
        String ip = getClientIp();
        comment.setIp(ip);
        
        // 默认状态为1（已审核），如果需要审核可以设置为0
        if (comment.getStatus() == null) {
            comment.setStatus(1);
        }
        
        // 父评论ID默认为0
        if (comment.getParentId() == null) {
            comment.setParentId(0L);
        }
        
        boolean success = this.save(comment);
        
        if (success && comment.getStatus() == 1) {
            // 更新文章评论数
            updateArticleCommentCount(comment.getArticleId(), 1);
        }
        
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteComment(Long id) {
        Comment comment = this.getById(id);
        if (comment == null) {
            return false;
        }
        
        boolean success = this.removeById(id);
        
        if (success && comment.getStatus() == 1) {
            // 更新文章评论数
            updateArticleCommentCount(comment.getArticleId(), -1);
        }
        
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approveComment(Long id) {
        Comment comment = this.getById(id);
        if (comment == null || comment.getStatus() == 1) {
            return false;
        }
        
        LambdaUpdateWrapper<Comment> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Comment::getId, id)
                .set(Comment::getStatus, 1);
        boolean success = this.update(wrapper);
        
        if (success) {
            // 更新文章评论数
            updateArticleCommentCount(comment.getArticleId(), 1);
        }
        
        return success;
    }

    private void updateArticleCommentCount(Long articleId, int delta) {
        // 这里可以优化为直接更新数据库，而不是先查询
        // 为了简单起见，这里不实现复杂的逻辑
        // 实际项目中应该考虑并发问题
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