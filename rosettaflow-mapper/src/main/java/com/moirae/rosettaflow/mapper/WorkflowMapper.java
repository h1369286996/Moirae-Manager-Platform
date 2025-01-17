package com.moirae.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.moirae.rosettaflow.dto.WorkflowDto;
import com.moirae.rosettaflow.mapper.domain.Workflow;
import org.apache.ibatis.annotations.Param;

/**
 * @author admin
 * @date 2021/8/16
 */
public interface WorkflowMapper extends BaseMapper<Workflow> {

    /**
     * 查询工作流列表
     *
     * @param projectId    项目id
     * @param workflowName 工作流名称
     * @param page         分页信息
     * @return 工作流列表
     */
    IPage<WorkflowDto> queryWorkFlowPageList(@Param("projectId") Long projectId,
                                             @Param("workflowName") String workflowName,
                                             IPage<WorkflowDto> page);

    /**
     * 删除与当前工作流相关所有节点数据
     * @param workflowId 工作流id
     */
    void deleteWorkflowAllNodeData(Long workflowId);
}