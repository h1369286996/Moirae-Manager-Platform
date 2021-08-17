package com.platon.rosettaflow.req.workflow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * @author hudenian
 * @date 2021/8/17
 * @description 启动工作流请求实例
 */
@Data
@ApiModel
public class StartWorkflowReq {

    @ApiModelProperty(value = "工作流ID", required = true)
    @NotNull(message = "{workflow.id.notNull}")
    @Positive(message = "{workflow.id.positive}")
    private Long workflowId;

    @ApiModelProperty(value = "起始节点", required = true)
    @NotNull(message = "{workflow.startNode.notNull}")
    @Positive(message = "{workflow.startNode.positive}")
    private Integer startNode;

    @ApiModelProperty(value = "截止节点", required = true)
    @NotNull(message = "{workflow.endNode.notNull}")
    @Positive(message = "{workflow.endNode.positive}")
    private Integer endNode;

}
