package com.platon.rosettaflow.req.algorithm;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author houz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "算法列表请求参数")
public class AlgListReq {

    @ApiModelProperty(value = "用户ID", required = true)
    @NotBlank(message = "{user.id.notBlank}")
    private Long userId;

    @ApiModelProperty(value = "算法名称")
    private String algorithmName;

}