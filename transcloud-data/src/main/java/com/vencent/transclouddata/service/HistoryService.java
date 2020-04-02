package com.vencent.transclouddata.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vencent.transclouddata.entity.History;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author vencent
 * @since 2020-03-22
 */
public interface HistoryService extends IService<History> {
    Page<History> listByUserId(String userId, Page page);
}
