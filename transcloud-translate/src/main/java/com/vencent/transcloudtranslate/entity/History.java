package com.vencent.transcloudtranslate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

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
public class History implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 历史记录标识
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
     * 所属用户标识
     */
    private String userId;

    /**
     * 识别源内容
     */
    private String transSource;

    /**
     * 识别目标内容
     */
    private String transTarget;

    /**
     * 识别语音
     */
    private String transSpeech;

    /**
     * 识别时间
     */
    private LocalDateTime transTime;


}
