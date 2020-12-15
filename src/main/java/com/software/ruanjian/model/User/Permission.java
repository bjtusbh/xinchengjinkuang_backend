package com.software.ruanjian.model.User;

import lombok.Data;

/**
 * @Author Lj
 * @Date 2020/10/31
 * msg
 */
@Data
public class Permission {
    private int id;
    private String menu_code;
    private String menu_name;
    private String permission_code;
    private String permission_name;
}
