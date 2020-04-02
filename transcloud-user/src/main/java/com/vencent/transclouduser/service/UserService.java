package com.vencent.transclouduser.service;

import com.vencent.transclouduser.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author vencent
 * @since 2020-03-22
 */
public interface UserService extends IService<User> {
    User getOneByUserAccount(String userAccount);
}
