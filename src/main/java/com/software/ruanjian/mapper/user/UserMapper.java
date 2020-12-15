package com.software.ruanjian.mapper.user;

import com.github.pagehelper.Page;
import com.software.ruanjian.model.User.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user ")
    Page<User> queryAll();

    @Select("SELECT * " +
            "       FROM user WHERE username = #{username}")
    User findByUsername(String username);

    @Select("SELECT * FROM user " +
            "       WHERE stu_class = #{stu_class}")
    Page<User> findByClass(String stu_class);

    @Select("SELECT *\n" +
            "       FROM user " +
            "       WHERE stu_class in (" +
            "SELECT stu_class FROM user " +
            "       WHERE username = #{username})")
    Page<User> findUserByClass(String username);

    @Update("UPDATE user SET " +
            "       password = #{password}\n" +
            "     , salt = #{salt}" +
            "       WHERE username = #{username}")
    int updatePassword(@Param("username") String username,
                       @Param("password") String password,
                       @Param("salt") String salt);

    @Update("UPDATE user SET\n" +
            "       id_number = #{id_number}\n" +
            "     , stu_class = #{stu_class}\n" +
            "       WHERE username = #{username}")
    int updateUser(User user);

    @Insert("INSERT INTO user\n" +
            "       (username, id_number, stu_class, password, salt, role_id)" +
            "       VALUES" +
            "       (#{username}, #{id_number}, #{stu_class}, " +
            "       #{password}, #{salt}, #{role_id})")
    int addUser(@Param("username") String username, @Param("id_number") String id_number,
                @Param("stu_class") String stu_class, @Param("password") String password,
                @Param("salt") String salt, @Param("role_id") String role_id);

    @Delete("DELETE FROM user\n" +
            "       WHERE username = #{username}")
    int deleteUser(String username);

    @Update("UPDATE user SET" +
            "       password = #{password}" +
            "       WHERE id = #{id}")
    int util(@Param("id") int id, @Param("password") String password);


}
