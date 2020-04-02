package com.vencent.transclouduser.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vencent.transclouduser.entity.User;
import com.vencent.transclouduser.mapper.UserMapper;
import com.vencent.transclouduser.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author vencent
 * @since 2020-03-22
 */
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User getOneByUserAccount(String userAccount){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        User user = this.getOne(queryWrapper);
        return user;
    }
}
