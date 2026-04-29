package com.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.myblog.entity.User;

public interface UserService extends IService<User> {

    User login(String username, String password);

    User getByUsername(String username);
}