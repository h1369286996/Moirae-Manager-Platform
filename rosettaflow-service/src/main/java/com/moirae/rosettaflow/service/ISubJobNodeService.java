package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.dto.SubJobNodeDto;
import com.moirae.rosettaflow.mapper.domain.SubJobNode;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/27
 * @description 子作业节点管理服务接口
 */
public interface ISubJobNodeService extends IService<SubJobNode> {

    /**
     *  修改子作业节点运行状态
     * @param id 子作业节点主键id
     * @param runStatus 运行状态
     * @return boolean 更新是否成功
     */
    boolean updateRunStatus(Long id, Byte runStatus);

    /**
     * 批量修改子作业节点状态
     *
     * @param ids 子作业节点i主键ds
     * @param runStatus  运行状态
     * @param runMsg  运行msg
     */
    void updateBatchRunStatus(Object[] ids, Byte runStatus, String runMsg);


    /**
     * 批量修改子作业节点有效状态
     *
     * @param ids 子作业ids
     * @param status  有效状态
     */
    void updateBatchStatus(Object[] ids, Byte status);


    /**
     * 获取查询子作业节点信息
     * @param subJobId 子作业id
     * @return List<SubJobNode> 子作业节点集合
     */
    List<SubJobNode> querySubJobNodeListBySubJobId(Long subJobId);

    /**
     * 批量查询子作业节点信息
     * @param subJobId 子作业id集合
     * @return SubJobNode 子作业节点集合
     */
    List<SubJobNode> queryBatchSubJobListNodeByJobId(Object[] subJobId);


    /**
     * 获取查询子作业节点信息
     * @param subJobId 子作业id
     * @param nodeStep 节点在工作流中序号
     * @return SubJobNode 子作业节点
     */
    SubJobNode querySubJobNodeByJobIdAndNodeStep(Long subJobId,Integer nodeStep);


    /**
     * 获取所有子工作流运行中的节点
     *
     * @return 运行中节点列表
     */
    List<SubJobNodeDto> getRunningNodeWithWorkIdAndNodeNum();



}
