package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.mapper.domain.WorkflowNode;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeCode;
import com.platon.rosettaflow.mapper.domain.WorkflowNodeResource;

import java.util.List;

/**
 * 工作流节点服务
 * @author hudenian
 * @date 2021/8/16
 */
public interface IWorkflowNodeService extends IService<WorkflowNode> {

    /**
     * 添加工作流节点
     * @param workflowNode  工作流节点信息
     */
    void addWorkflowNode(WorkflowNode workflowNode);

    /**
     * 复制工作流节点
     * @param workflowNode  工作流节点信息
     */
    void copyWorkflowNode(WorkflowNode workflowNode);

    /**
     * 工作流节点重命名
     * @param nodeId 工作流节点id
     * @param nodeName 工作流节点名称
     */
    void renameWorkflowNode(Long nodeId, String nodeName);

    /**
     * 删除工作流中的节点
     * @param id 工作流节点id
     */
    void deleteWorkflowNode(Long id);

    /**
     * 根据工作流id及节点序号获取工作流节点
     * @param workflowId 工作流id
     * @param startNode  节点序号
     * @return 工作流节点
     */
    WorkflowNode getByWorkflowIdAndStep(Long workflowId, Integer startNode);

    /**
     * 根据工作流id获取工作流节点列表
     * @param workflowId 工作流主键id
     * @return 工作流节点列表
     */
    List<WorkflowNode> getWorkflowNodeList(Long workflowId);

    /**
     * 添加工作流节点代码
     * @param workflowNodeCode  工作流节点代码
     */
    void addWorkflowNodeCode(WorkflowNodeCode workflowNodeCode);

    /**
     * 添加工作流节点资源
     * @param workflowNodeResource  工作流节点资源
     */
    void addWorkflowNodeResource(WorkflowNodeResource workflowNodeResource);

}
