package com.myblog.util;

import cn.hutool.crypto.digest.BCrypt;

public class PasswordGenerator {
    public static void main(String[] args) {
        String rawPassword = "admin123";
        String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt(10));
        
        System.out.println("原始密码: " + rawPassword);
        System.out.println("加密后密码: " + hashedPassword);
        System.out.println("验证结果: " + BCrypt.checkpw(rawPassword, hashedPassword));
        
        // 输出一个示例，方便复制到SQL
        System.out.println("\nSQL中使用:");
        System.out.println("INSERT INTO user (username, password, nickname, email, status) VALUES ('admin', '" + hashedPassword + "', '管理员', 'admin@example.com', 1);");
    }
}