package com.vencent.transclouduser.controller;


import com.vencent.transclouduser.Utils.MD5Util;
import com.vencent.transclouduser.Utils.UUIDUtil;
import com.vencent.transclouduser.entity.User;
import com.vencent.transclouduser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author vencent
 * @since 2020-03-22
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /*
    注册
     */
    @PostMapping("/register")
    public @ResponseBody
    String register(@RequestBody(required=true) User user){
        User userNew =  userService.getOneByUserAccount(user.getUserAccount());
        if(userNew !=null){
            return "注册失败，用户账号重复";
        }
        user.setId(UUIDUtil.getUUID());
        LocalDateTime localDateTime = LocalDateTime.now();
        user.setCreateTime(localDateTime);
        //将密码进行加密
        user.setPassword(MD5Util.md5(user.getPassword()));
        if (userService.save(user)){
            return "注册成功";
        }
        return "注册失败，原因未知";

    }
    /*
    登录
     */
    @PostMapping("/login")
    public @ResponseBody
    String login(@RequestBody(required = true) User user,HttpSession session){
       if(user == null){
           return "登录失败";
       }
       User userNew =  userService.getOneByUserAccount(user.getUserAccount());
       if(userNew == null){
           return "用户名错误";
       }
       if(!userNew.getPassword().equals(MD5Util.md5(user.getPassword()))){
           return "密码错误";
       }
       session.setAttribute("userID",userNew.getId());
       session.setAttribute("username",userNew.getUsername());
       return "success";
    }
    @GetMapping("/exit")
    public @ResponseBody
    String exit(HttpSession session){
        session.removeAttribute("userID");
        session.removeAttribute("username");
        return "success";
    }

    /**
     *
     * @param username
     * @return
     */
    @GetMapping("/getUserName")
    public @ResponseBody
    String getUserName(@SessionAttribute String username){
        return username;
    }

    /**
     * 获取当前用户信息
     * @param userID
     * @return
     */
    @GetMapping("select")
    public @ResponseBody
    User getCurrentUser(@SessionAttribute String userID){
        User user = userService.getById(userID);
        return user;
    }

    /**
     * 修改用户
     * @param user
     * @return
     */
    @PostMapping("update")
    public @ResponseBody
    String updateUser(@RequestBody User user){
        user.setCreateTime(LocalDateTime.now());
        user.setPassword(MD5Util.md5(user.getPassword()));
        if(userService.updateById(user)){
            return "修改成功";
        }
        return "修改失败";
    }
}
