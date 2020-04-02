package com.vencent.transcloudtranslate.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vencent.transcloudtranslate.entity.History;
import com.vencent.transcloudtranslate.entity.TransInterface;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author vencent
 * @since 2020-03-22
 */
public interface TransInterfaceService extends IService<TransInterface> {
    Page<TransInterface> listByUserId(String userId, Page page);
    TransInterface selectByUserIdAndCurrent(String userId);
    History translate (String userId,byte[] bytes,String fileName) throws Exception;
    Boolean UpdateTwoByCurrentInterface(String changeOne,String changeTwo);
    List<TransInterface> listByType(String type);
}
