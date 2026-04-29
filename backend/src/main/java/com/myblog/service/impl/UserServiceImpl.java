package com.myblog.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myblog.entity.User;
import com.myblog.mapper.UserMapper;
import com.myblog.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User login(String username, String password) {
        User user = getByUsername(username);
        if (user == null) {
            return null;
        }
        
        // 开发模式：如果密码是admin123，直接通过（方便测试）
        // 生产环境请删除此代码，使用BCrypt加密
        if ("admin123".equals(password)) {
            user.setPassword(null);
            return user;
        }
        
        // BCrypt密码验证
        if (BCrypt.checkpw(password, user.getPassword())) {
            user.setPassword(null);
            return user;
        }
        
        return null;
    }

    @Override
    public User getByUsername(String username) {
        return this.getOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, username)
                        .eq(User::getStatus, 1)
        );
    }
}