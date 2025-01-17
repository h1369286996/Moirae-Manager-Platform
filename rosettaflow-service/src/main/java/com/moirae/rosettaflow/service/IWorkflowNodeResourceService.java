package com.moirae.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moirae.rosettaflow.mapper.domain.WorkflowNodeResource;

import java.util.List;

/**
 * @author hudenian
 * @date 2021/8/18
 * @description 工作流节点资源接口
 */
public interface IWorkflowNodeResourceService extends IService<WorkflowNodeResource> {
    /**
     * 根据工作流节点id获取工作流节点资源
     *
     * @param workflowNodeId 工作流节点id
     * @return 工作流节点资源
     */
    WorkflowNodeResource getByWorkflowNodeId(Long workflowNodeId);

    /**
     * 物物理批量删除工作流节点环境资源，根据节点id
     *
     * @param workflowNodeIdList 工作流节点id列表
     */
    void deleteByWorkflowNodeId(List<Long> workflowNodeIdList);

    /**
     * 逻辑删除工作流节点资源表, 根据工作流节点id
     *
     * @param workflowNodeId 工作流节点id
     */
    void deleteLogicByWorkflowNodeId(Long workflowNodeId);

    /**
     * 复制工作流节点环境资源
     *
     * @param newNodeId 新节点id
     * @param oldNodeId 旧节点id
     * @return 工作流节点资源
     */
    WorkflowNodeResource copyWorkflowNodeResource(Long newNodeId, Long oldNodeId);

    /**
     * 批量保存节点资源
     *
     * @param workflowNodeResourceList 节点资源列表
     */
    void batchInsert(List<WorkflowNodeResource> workflowNodeResourceList);
}
