package com.moirae.rosettaflow.req.job;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.moirae.rosettaflow.common.constants.SysConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;

/**
 * @author hudenian
 * @date 2021/8/26
 * @description 创建作业请求对象
 */
@Data
@ApiModel(value = "创建作业请求")
public class AddJobReq {

    @ApiModelProperty(value = "工作流ID", required = true)
    @NotNull(message = "{workflow.id.notNull}")
    @Positive(message = "{workflow.id.positive}")
    private Long workflowId;

    @ApiModelProperty(value = "作业名称", required = true)
    @NotBlank(message = "{job.name.notNull}")
    private String name;

    @ApiModelProperty(value = "作业描述")
    private String desc;

    @ApiModelProperty(value = "是否重复：0-否,1-是", required = true)
    @NotNull(message = "{job.repeatFlag.notNull}")
    @Range(min = 0, max = 1, message = "{job.repeatFlag.type.error}")
    private Byte repeatFlag;

    @ApiModelProperty(value = "是否限制结束时间：0-否,1-是", required = true)
    @NotNull(message = "{job.endTimeFlag.notNull}")
    @Range(min = 0, max = 1, message = "{job.endTimeFlag.type.error}")
    private Byte endTimeFlag;

    @ApiModelProperty(value = "重复间隔，单位分钟;当是否重复为是时，才必选；如果非必填则可以填空")
    @Positive(message = "{job.repeatInterval.positive}")
    private Integer repeatInterval;

    @ApiModelProperty(value = "开始时间，格式：yyyy-MM-dd HH:mm:ss", required = true)
    @NotNull(message = "{job.beginTime.notNull}")
    @JsonFormat(pattern = SysConstant.DEFAULT_TIME_PATTERN, timezone = SysConstant.DEFAULT_TIMEZONE)
    private Date beginTime;

    @ApiModelProperty(value = "结束时间，格式：yyyy-MM-dd HH:mm:ss;当是否重复为是时，才必选")
    @JsonFormat(pattern = SysConstant.DEFAULT_TIME_PATTERN, timezone = SysConstant.DEFAULT_TIMEZONE)
    private Date endTime;

}
