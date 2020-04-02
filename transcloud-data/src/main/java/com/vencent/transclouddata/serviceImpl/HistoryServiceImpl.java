package com.vencent.transclouddata.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vencent.transclouddata.entity.History;
import com.vencent.transclouddata.mapper.HistoryMapper;
import com.vencent.transclouddata.service.HistoryService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author vencent
 * @since 2020-03-22
 */
@Service
public class HistoryServiceImpl extends ServiceImpl<HistoryMapper, History> implements HistoryService {
    @Override
    public Page<History> listByUserId(String userId, Page page){
        QueryWrapper<History> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        return this.baseMapper.selectPage(page,queryWrapper);
    }
}
