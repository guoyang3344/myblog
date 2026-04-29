-- 创建数据库
CREATE DATABASE IF NOT EXISTS myblog DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE myblog;

-- 用户表（管理员）
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（加密存储）',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `status` TINYINT(1) DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) DEFAULT 0 COMMENT '逻辑删除：1-删除，0-未删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 文章表
CREATE TABLE IF NOT EXISTS `article` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '文章ID',
    `title` VARCHAR(200) NOT NULL COMMENT '文章标题',
    `summary` VARCHAR(500) DEFAULT NULL COMMENT '文章摘要',
    `content` TEXT COMMENT '文章内容',
    `cover_image` VARCHAR(255) DEFAULT NULL COMMENT '封面图片URL',
    `category` VARCHAR(50) DEFAULT NULL COMMENT '文章分类',
    `tags` VARCHAR(200) DEFAULT NULL COMMENT '标签，逗号分隔',
    `view_count` BIGINT(20) DEFAULT 0 COMMENT '浏览量',
    `like_count` BIGINT(20) DEFAULT 0 COMMENT '点赞数',
    `comment_count` BIGINT(20) DEFAULT 0 COMMENT '评论数',
    `status` TINYINT(1) DEFAULT 1 COMMENT '状态：1-发布，0-草稿',
    `is_top` TINYINT(1) DEFAULT 0 COMMENT '是否置顶：1-是，0-否',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) DEFAULT 0 COMMENT '逻辑删除：1-删除，0-未删除',
    PRIMARY KEY (`id`),
    KEY `idx_category` (`category`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章表';

-- 评论表
CREATE TABLE IF NOT EXISTS `comment` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '评论ID',
    `article_id` BIGINT(20) NOT NULL COMMENT '文章ID',
    `parent_id` BIGINT(20) DEFAULT 0 COMMENT '父评论ID，0表示一级评论',
    `nickname` VARCHAR(50) DEFAULT '匿名用户' COMMENT '评论者昵称',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '评论者邮箱',
    `content` TEXT NOT NULL COMMENT '评论内容',
    `ip` VARCHAR(50) DEFAULT NULL COMMENT '评论者IP',
    `status` TINYINT(1) DEFAULT 1 COMMENT '状态：1-正常，0-待审核',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) DEFAULT 0 COMMENT '逻辑删除：1-删除，0-未删除',
    PRIMARY KEY (`id`),
    KEY `idx_article_id` (`article_id`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 收藏表
CREATE TABLE IF NOT EXISTS `favorite` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
    `article_id` BIGINT(20) NOT NULL COMMENT '文章ID',
    `ip` VARCHAR(50) NOT NULL COMMENT '收藏者IP',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT(1) DEFAULT 0 COMMENT '逻辑删除：1-删除，0-未删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_article_ip` (`article_id`, `ip`),
    KEY `idx_article_id` (`article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- 插入默认管理员用户（密码：admin123，使用BCrypt加密）
-- 注意：如果登录失败，请运行 PasswordGenerator 类生成新的密码hash
INSERT INTO `user` (`username`, `password`, `nickname`, `email`, `status`) 
VALUES ('admin', '$2a$10$Eqvk68D0.Q3D.2WcP.3IeK.O6zKzKzKzKzKzKzKzKzKzKzKzK', '管理员', 'admin@example.com', 1);

-- 插入一些测试文章
INSERT INTO `article` (`title`, `summary`, `content`, `category`, `tags`, `view_count`, `like_count`, `comment_count`, `status`, `is_top`) 
VALUES 
('欢迎来到我的博客', '这是我的第一篇博客文章，欢迎大家来访！', '大家好，欢迎来到我的个人博客！在这里我会分享我的技术心得、生活感悟和一些有趣的事情。希望大家能够喜欢！', '生活', '生活,博客,欢迎', 100, 10, 2, 1, 1),
('Vue3入门教程', '本文将介绍Vue3的基本概念和使用方法，帮助初学者快速上手Vue3开发。', 'Vue3是Vue.js的最新版本，带来了许多新特性和改进。本文将从基础开始，介绍Vue3的核心概念、Composition API、响应式系统等内容。', '技术', 'Vue3,前端,JavaScript', 200, 25, 5, 1, 0),
('Spring Boot最佳实践', '分享Spring Boot开发中的一些最佳实践和经验总结。', 'Spring Boot是Java开发中最流行的框架之一。本文将分享我在使用Spring Boot开发项目时的一些最佳实践，包括项目结构、配置管理、异常处理等方面。', '技术', 'Spring Boot,Java,后端', 150, 15, 3, 1, 0);

-- 插入一些测试评论
INSERT INTO `comment` (`article_id`, `parent_id`, `nickname`, `email`, `content`, `ip`, `status`) 
VALUES 
(1, 0, '访客A', 'visitorA@example.com', '写得很好，支持一下！', '192.168.1.100', 1),
(1, 0, '访客B', 'visitorB@example.com', '期待更多精彩内容！', '192.168.1.101', 1),
(2, 0, '学习者', 'learner@example.com', '非常实用的教程，谢谢分享！', '192.168.1.102', 1),
(2, 3, '访客C', 'visitorC@example.com', '确实很实用，我已经收藏了。', '192.168.1.103', 1),
(3, 0, 'Java开发者', 'java_dev@example.com', '这些最佳实践很有价值，学习了！', '192.168.1.104', 1);

-- 插入一些测试收藏
INSERT INTO `favorite` (`article_id`, `ip`) 
VALUES 
(1, '192.168.1.100'),
(2, '192.168.1.102'),
(2, '192.168.1.103'),
(3, '192.168.1.104');