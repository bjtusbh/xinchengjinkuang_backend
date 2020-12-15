package com.software.ruanjian.controller;

import com.github.pagehelper.PageInfo;
import com.software.ruanjian.mapper.user.PermissionMapper;
import com.software.ruanjian.mapper.user.UserMapper;
import com.software.ruanjian.model.User.Role;
import com.software.ruanjian.model.User.User;
import com.software.ruanjian.model.User.UserPermission;

import com.software.ruanjian.utils.result.ExceptionMsg;
import com.software.ruanjian.utils.result.ResponseData;
import com.software.ruanjian.utils.user.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.software.ruanjian.utils.Jwt.JWTUtil;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.Set;

@Api("用户权限信息")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired(required = false)
    UserMapper userMapper;
    @Autowired(required = false)
    PermissionMapper permissionMapper;

    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "String",
            name = "token", value = "登录token", required = true) })
    @ApiOperation(value = "根据用户不同角色获取一类用户的信息",notes = "根据登录用户的角色获取用户集的信息; " +
            "其中包含角色：系统管理员[admin, role_id=1]、联络员[contact, role_id=2]、一般用户[guest, role_id=3]; " +
            "目前联络员仅能查询同班同学信息！")
    @GetMapping(value = "/queryUsers")
    @RequiresAuthentication
    public ResponseData queryUsers(@RequestParam(value="pageNum",defaultValue="1")int pageNum,
                                 @RequestParam(value = "pageSize", defaultValue = "5") int pageSize){
        Subject currentUser = SecurityUtils.getSubject();
        String username = JWTUtil.getUsername(currentUser.getPrincipal().toString());

        Page<User> users = new Page<>();
        if (currentUser.hasRole("admin")) {
            PageHelper.startPage(pageNum, pageSize);
            users = userMapper.queryAll();
        } else if (currentUser.hasRole("contact")) {
            /* 目前仅通过相同班级进行查询 */
            PageHelper.startPage(pageNum, pageSize);
            users = userMapper.findUserByClass(username);
        } else {
            users.add(userMapper.findByUsername(username));
        }
        PageInfo pageInfo = new PageInfo(users);
        return new ResponseData(ExceptionMsg.SUCCESS, pageInfo);
    }

    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "String",
            name = "token", value = "登录token", required = true) })
    @ApiOperation(value = "获取用户所拥有的角色", notes = "获取用户的角色信息，参数username为用户学号！" +
            "其中包含角色：系统管理员[admin, role_id=1]、联络员[contact, role_id=2]、一般用户[guest, role_id=3]")
    @GetMapping("/getUserRole")
    public ResponseData getUserRole(@RequestParam("username") String username) {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isAuthenticated()) {
            Role role = permissionMapper.getUserRole(username);
            return new ResponseData(ExceptionMsg.SUCCESS, role != null ? role : "用户不存在，请检查用户名！");
        } else {
            return new ResponseData(ExceptionMsg.FAILED, "请先登录在进行查询！");
        }
    }

    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "String",
            name = "token", value = "登录token", required = true) })
    @ApiOperation(value = "获取用户所拥有的权限", notes = "获取用户的权限信息，参数username为用户学号！" +
            "其中包含权限：user:list[查看权限]，user:add[添加权限]，user:delete[删除权限]，user:update[更新权限]")
    @GetMapping("/getUserPermission")
    public ResponseData getUserPermission(@RequestParam("username") String username) {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isAuthenticated()) {
            Set<String> userPermission = permissionMapper.getUserPermission(username);
            return new ResponseData(ExceptionMsg.SUCCESS, userPermission != null ? userPermission : "用户不存在，请检查用户名！");
        } else {
            return new ResponseData(ExceptionMsg.FAILED, "请先登录在进行查询！");
        }
    }

    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "String",
            name = "token", value = "登录token", required = true) })
    @ApiOperation(value = "获取用户所拥有的角色与权限", notes = "获取用户所拥有角色及权限更详细的信息！")
    @GetMapping("/getRolePermission")
    public ResponseData getRolePermission(@RequestParam("username") String username) {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isAuthenticated()) {
            UserPermission userPermission = permissionMapper.getRolePermission(username);
            return new ResponseData(ExceptionMsg.SUCCESS, userPermission != null ? userPermission : "用户不存在，请检查用户名！");
        } else {
            return new ResponseData(ExceptionMsg.FAILED, "请先登录在进行查询！");
        }
    }

    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "String",
            name = "token", value = "登录token", required = true) })
    @ApiOperation(value = "更新用户角色", notes = "目前默认系统管理员可更新用户角色！" +
            "其中参数为：待更新用户学号，待更新角色名[枚举：admin, contact, guest]")
    @PostMapping("/updateUserRole")
    public ResponseData updateUserRole(@RequestParam("username") String username, @RequestParam("roleCode") String roleCode) {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.hasRole("admin")) {
            String roleId = permissionMapper.getRIdFromRCode(roleCode);
            if (roleId == null)
                return new ResponseData(ExceptionMsg.FAILED, "传入参数角色名有误！");
            else
                return new ResponseData(ExceptionMsg.SUCCESS, permissionMapper.updateUserPole(username, roleId));
        } else {
            return new ResponseData(ExceptionMsg.FAILED, "系统管理员可更新用户角色哦！");
        }
    }

    @ApiOperation(value = "用户登录", notes = "用户名为用户学号[数据库包含一个默认用户名admin，可登录之后查看数据库包含哪些用户]！所有密码目前默认为111111")
    @PostMapping("/login")
    public ResponseData login(@RequestParam("username") String username,
                           @RequestParam("password") String password) {

        String realPassword = permissionMapper.getPassword(username);
        if (realPassword == null) {
            return new ResponseData(ExceptionMsg.FAILED,"用户名错误");
        }

        String encrypt = UserUtil.encrypt(password, permissionMapper.getSalt(username));
        if (!realPassword.equals(encrypt)) {
            return new ResponseData(ExceptionMsg.FAILED,"密码错误：" + password);
        } else {
            return new ResponseData("20001","登录成功", JWTUtil.createToken(username));
        }
    }

    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "String",
            name = "token", value = "登录token", required = true) })
    @ApiOperation(value = "用户登出", notes = "当前登录用户退出登录！")
    @PostMapping("/logout")
    public ResponseData logout() {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        return new ResponseData(ExceptionMsg.SUCCESS);
    }

    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "String",
            name = "token", value = "登录token", required = true) })
    @ApiOperation(value = "修改密码", notes = "目前使用身份证号来验证修改密码！")
    @PostMapping("/changePassword")
    public ResponseData changePassword(@RequestParam("userIdNumber") String userIdNumber,
                                       @RequestParam("newPassword") String newPassword,
                                       @RequestParam("confirmPassword") String confirmPassword) {
        if (!newPassword.equals(confirmPassword))
            return new ResponseData(ExceptionMsg.FAILED, "密码不一致");

        Subject subject = SecurityUtils.getSubject();
        String username = JWTUtil.getUsername(subject.getPrincipal().toString());
        User user = userMapper.findByUsername(username);
        if (user == null)
            return new ResponseData(ExceptionMsg.FAILED, "用户名错误");
        else if (!userIdNumber.equals(user.getId_number()))
            return new ResponseData(ExceptionMsg.FAILED, "身份证号错误");
        else {
            String salt = UserUtil.RandomString(16);
            userMapper.updatePassword(username, UserUtil.encrypt(newPassword, salt), salt);
            return new ResponseData("20002", "密码修改成功", JWTUtil.createToken(username));
        }
    }

    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "String",
            name = "token", value = "登录token", required = true) })
    @ApiOperation(value = "更新权限表用户的信息", notes = "根据用户username修改用户信息，需要登录用户拥有更新用户的权限！")
    @PostMapping("/updateUser")
    @RequiresPermissions("user:update")
    public ResponseData updateUser(@RequestBody User user) {
        User currentUser = userMapper.findByUsername(user.getUsername());
        if (currentUser == null)
            return new ResponseData(ExceptionMsg.FAILED, "用户不存在: " + user.getUsername());
        else {
            userMapper.updateUser(user);
            return new ResponseData(ExceptionMsg.SUCCESS, user);
        }
    }

    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "String",
            name = "token", value = "登录token", required = true) })
    @ApiOperation(value = "在权限表添加一个用户信息", notes = "需要登录用户拥有添加用户的权限！" +
            "role_id：枚举 { 系统管理员[admin, role_id=1]、联络员[contact, role_id=2]、一般用户[guest, role_id=3 } ；" +
            "前端辅助确认密码！")
    @PostMapping("/addUser")
    @RequiresPermissions("user:add")
    public ResponseData addUser(@RequestParam("username") String username, @RequestParam("id_number") String id_number,
                                @RequestParam("stu_class") String stu_class, @RequestParam("password") String password,
                                @RequestParam(value = "role_id", defaultValue = "3") String role_id) {
        User currentUser = userMapper.findByUsername(username);
        if (currentUser != null)
            return new ResponseData(ExceptionMsg.FAILED, "用户已经存在");
        else {
            String salt = UserUtil.RandomString(16);
            String encrypt = UserUtil.encrypt(password, salt);
            int user = userMapper.addUser(username, id_number, stu_class, encrypt, salt, role_id);
            return new ResponseData(ExceptionMsg.SUCCESS, user);
        }
    }

    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "String",
            name = "token", value = "登录token", required = true) })
    @ApiOperation(value = "删除权限表用户信息", notes = "根据用户username[学号]删除用户，需要登录用户拥有删除用户的权限！")
    @PostMapping("/deleteUser")
    @RequiresPermissions("user:delete")
    public ResponseData deleteUser(@RequestParam("username") String username) {
        User currentUser = userMapper.findByUsername(username);
        if (currentUser == null)
            return new ResponseData("30002", "用户不存在", username);
        else {
            int res = userMapper.deleteUser(username);
            return new ResponseData(ExceptionMsg.SUCCESS, res);
        }
    }

    @ApiOperation(value = "测试测试", notes = "测试测试！")
    @GetMapping("/util")
    public ResponseData util() {
        return new ResponseData("20002"+"", "信息修改成功", "测试测试！");
    }

}







