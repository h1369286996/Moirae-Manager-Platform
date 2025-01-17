package com.moirae.rosettaflow.mapper.domain;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * t_job
 *
 * @author admin
 */
@Data
@TableName(value = "t_job")
public class Job implements Serializable {
    /**
     * 任务计划表ID(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 工作流id
     */
    private Long workflowId;

    /**
     * 作业名称
     */
    private String name;

    /**
     * 作业描述
     */
    @TableField(value = "`desc`")
    private String desc;

    /**
     * 是否重复：0-否,1-是
     */
    private Byte repeatFlag;

    /**
     * 重复间隔，单位分钟
     */
    private Integer repeatInterval;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 是否限制结束时间：0-否,1-是
     */
    private Byte endTimeFlag;

    /**
     * 状态: 0-未开始，1-运行中，2-已停止，3-已结束
     */
    private Byte jobStatus;

    /**
     * 有效状态: 0-无效，1- 有效
     */
    @TableField(value = "`status`")
    private Byte status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Job other = (Job) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getWorkflowId() == null ? other.getWorkflowId() == null : this.getWorkflowId().equals(other.getWorkflowId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getDesc() == null ? other.getDesc() == null : this.getDesc().equals(other.getDesc()))
            && (this.getRepeatFlag() == null ? other.getRepeatFlag() == null : this.getRepeatFlag().equals(other.getRepeatFlag()))
            && (this.getRepeatInterval() == null ? other.getRepeatInterval() == null : this.getRepeatInterval().equals(other.getRepeatInterval()))
            && (this.getBeginTime() == null ? other.getBeginTime() == null : this.getBeginTime().equals(other.getBeginTime()))
            && (this.getEndTime() == null ? other.getEndTime() == null : this.getEndTime().equals(other.getEndTime()))
            && (this.getJobStatus() == null ? other.getJobStatus() == null : this.getJobStatus().equals(other.getJobStatus()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getWorkflowId() == null) ? 0 : getWorkflowId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getDesc() == null) ? 0 : getDesc().hashCode());
        result = prime * result + ((getRepeatFlag() == null) ? 0 : getRepeatFlag().hashCode());
        result = prime * result + ((getRepeatInterval() == null) ? 0 : getRepeatInterval().hashCode());
        result = prime * result + ((getBeginTime() == null) ? 0 : getBeginTime().hashCode());
        result = prime * result + ((getEndTime() == null) ? 0 : getEndTime().hashCode());
        result = prime * result + ((getJobStatus() == null) ? 0 : getJobStatus().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", id=" + id +
                ", workflowId=" + workflowId +
                ", name=" + name +
                ", desc=" + desc +
                ", repeatFlag=" + repeatFlag +
                ", repeatInterval=" + repeatInterval +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", jobStatus=" + jobStatus +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }
}