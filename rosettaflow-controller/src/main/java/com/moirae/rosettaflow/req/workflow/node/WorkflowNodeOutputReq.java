package com.moirae.rosettaflow.req.workflow.node;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 输出请求对象
 * @author hudenian
 * @date 2021/9/28
 */
@Data
@ApiModel(value = "工作流节点输出请求对象")
public class WorkflowNodeOutputReq {

    @ApiModelProperty(value = "组织的身份标识Id", required = true)
    @NotBlank(message = "{node.identity.id.NotBlank}")
    private String identityId;

    @ApiModelProperty(value = "存储形式: 1-明文，2:密文")
    private Integer storePattern;

    @ApiModelProperty(value = "存储路径")
    private String storePath;

    @ApiModelProperty(value = "是否发起方: 0-否, 1-是", required = true)
    @NotNull(message = "{node.sender.flag.notNull}")
    private Integer senderFlag;

}
