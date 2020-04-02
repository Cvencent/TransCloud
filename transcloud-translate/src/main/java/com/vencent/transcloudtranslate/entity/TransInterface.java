package com.vencent.transcloudtranslate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
public class TransInterface implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 翻译接口标识
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
     * 所属用户
     */
    private String userId;

    /**
     * 翻译接口类型 调用哪一家的接口
     */
    private String interfaceType;

    /**
     * 翻译产品ID 根据接口需要，没有则不需要填写
     */
    private String appId;

    /**
     * 翻译接口区域 根据接口需要，没有则不需要填写
     */
    private String region;

    /**
     * 翻译用户ID 接口身份验证ID
     */
    private String secretId;

    /**
     * 翻译用户密钥 接口身份验证密钥
     */
    private String secretKey;
    /**
     * 是否为当前使用的接口
     * 1是，0否
     */
    private String currentInterface;

}
