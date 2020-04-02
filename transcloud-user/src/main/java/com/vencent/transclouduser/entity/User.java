package com.vencent.transclouduser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author vencent
 * @since 2020-03-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户标识
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
