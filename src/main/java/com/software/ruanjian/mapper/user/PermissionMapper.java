package com.software.ruanjian.mapper.user;

import com.software.ruanjian.model.User.Permission;
import com.software.ruanjian.model.User.Role;
import com.software.ruanjian.model.User.UserPermission;
import org.apache.ibatis.annotations.*;

import java.util.Set;

/**
 * @Author lj
 * @Date 2020/10/30
 * msg
 */
@Mapper
public interface PermissionMapper {

    @Select("SELECT password\n" +
            "        FROM user\n" +
            "        WHERE username = #{username}")
    String getPassword(String username);

    @Select("SELECT salt FROM user WHERE username = #{username}")
    String getSalt(String username);

    @Select("SELECT role_code " +
            "       FROM role r " +
            "       WHERE r.id in ( " +
            "SELECT role_id " +
            "       FROM user where username = #{username})")
    String getRoleCode(String username);

    @Select("SELECT * " +
            "       FROM role r " +
            "       WHERE r.id in ( " +
            "SELECT role_id " +
            "       FROM user where username = #{username})")
    Role getUserRole(String username);

    @Select("SELECT p.permission_code\n" +
            "FROM user u\n" +
            "       LEFT JOIN role_permission rp ON u.role_id = rp.role_id\n" +
            "       LEFT JOIN permission p ON rp.permission_id = p.id\n" +
            "WHERE u.username = #{username}")
    Set<String> getUserPermission(String username);

    @Select("SELECT * \n" +
            "FROM user u\n" +
            "       LEFT JOIN role r ON r.id = u.role_id\n" +
            "WHERE u.username = #{username}")
    @Results(id = "RolePermission", value = {
            @Result(property = "userId", column = "id"),
            @Result(property = "permissions", column = "role_id",
                    many = @Many(select = "com.software.ruanjian.mapper.user.PermissionMapper.getPermission"))
    })
    UserPermission getRolePermission(String username);

    @Select("SELECT * FROM permission p\n" +
            "       WHERE p.id in (" +
            "SELECT permission_id FROM role_permission rp\n" +
            "       WHERE rp.role_id = #{role_id})")
    Set<Permission> getPermission(String role_id);

    @Select("SELECT role_code\n" +
            "       FROM role\n" +
            "       WHERE role_name = #{roleName}")
    String getRCodeFromRName(String roleName);

    @Select("SELECT id\n" +
            "       FROM role\n" +
            "       WHERE role_code = #{roleCode}")
    String getRIdFromRCode(String roleCode);

    @Update("UPDATE user SET\n" +
            "       role_id = #{roleId}\n" +
            "       WHERE username = #{username}")
    int updateUserPole(@Param("username") String username, @Param("roleId") String roleId);


}

