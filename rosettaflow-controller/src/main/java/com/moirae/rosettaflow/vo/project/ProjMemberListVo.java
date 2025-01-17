package com.moirae.rosettaflow.vo.project;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 项目成员列表返回参数
 * @author houz
 */
@Data
@ApiModel(value = "项目成员列表返回参数")
public class ProjMemberListVo {

    @ApiModelProperty(value = "项目成员id")
    private Long memberId;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "项目成员角色")
    private Byte role;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}
