package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.dto.MetaDataDto;
import com.moirae.rosettaflow.mapper.domain.MetaData;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/24
 * @description 元数据服务
 */
public interface IMetaDataService extends IService<MetaData> {
    /**
     * 清空元数据摘要表
     */
    void truncate();

    /**
     * 获取元数据摘要列表
     *
     * @param current  当前页
     * @param size     每页大小
     * @param dataName 元数据名称
     * @return 分页数据
     */
    IPage<MetaDataDto> list(Long current, Long size, String dataName);

    /**
     * 获取元数据详情
     *
     * @param userMetaDataId 用户授权数据表Id
     * @param metaDataPkId 元数据表Id
     * @return 元数据详情
     */
    MetaDataDto detail(String userMetaDataId, String metaDataPkId);

    /**
     * 获取元数据详情
     *
     * @param metaDataId 元数据id
     * @return 元数据详情
     */
    MetaData getMetaDataByMetaDataId(String metaDataId);

    /**
     * 获取全部元数据数量
     * @return 元数据集合数量
     */
    Integer getAllMetaDataCount();

    /**
     * 根据identityId查询元数据列表
     *
     * @param identityId identityId
     * @return 元数据列表
     */
    List<MetaDataDto> getAllAuthTables(String identityId);

    /**
     * 批量更新数据
     * @param metaDataList 批量插入列表
     */
    void batchInsert(List<MetaData> metaDataList);
}
