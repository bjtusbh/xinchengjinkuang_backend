package com.software.ruanjian.model.User;

import lombok.Data;

import java.util.Set;

/**
 * @Author lj
 * @Date 2020/10/30
 * msg 用户权限类
 */

@Data
public class UserPermission {
    private int userId;
    private String username;
    private String role_code;
    private String role_name;
    private Set<Permission> permissions;
}
