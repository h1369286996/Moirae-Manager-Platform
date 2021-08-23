package com.platon.rosettaflow.mapper.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_workflow_node_resource
 *
 * @author admin
 */
@Data
@TableName(value = "t_workflow_node_resource")
public class WorkflowNodeResource implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 节点资源表ID(自增长)
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 工作流节点id
     */
    private Long workflowNodeId;
    /**
     * 所需的内存 (单位: byte)
     */
    private Long costMem;
    /**
     * 所需的核数 (单位: 个)
     */
    private Long costProcessor;
    /**
     * GPU核数(单位：核)
     */
    private Integer costGpu;
    /**
     * 所需的带宽 (单位: bps)
     */
    private Long costBandwidth;
    /**
     * 所需的运行时长 (单位: ms)
     */
    private Long duration;
    /**
     * 状态: 0-无效，1- 有效
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
        WorkflowNodeResource other = (WorkflowNodeResource) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getWorkflowNodeId() == null ? other.getWorkflowNodeId() == null : this.getWorkflowNodeId().equals(other.getWorkflowNodeId()))
                && (this.getCostMem() == null ? other.getCostMem() == null : this.getCostMem().equals(other.getCostMem()))
                && (this.getCostProcessor() == null ? other.getCostProcessor() == null : this.getCostProcessor().equals(other.getCostProcessor()))
                && (this.getCostGpu() == null ? other.getCostGpu() == null : this.getCostGpu().equals(other.getCostGpu()))
                && (this.getCostBandwidth() == null ? other.getCostBandwidth() == null : this.getCostBandwidth().equals(other.getCostBandwidth()))
                && (this.getDuration() == null ? other.getDuration() == null : this.getDuration().equals(other.getDuration()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getWorkflowNodeId() == null) ? 0 : getWorkflowNodeId().hashCode());
        result = prime * result + ((getCostMem() == null) ? 0 : getCostMem().hashCode());
        result = prime * result + ((getCostProcessor() == null) ? 0 : getCostProcessor().hashCode());
        result = prime * result + ((getCostGpu() == null) ? 0 : getCostGpu().hashCode());
        result = prime * result + ((getCostBandwidth() == null) ? 0 : getCostBandwidth().hashCode());
        result = prime * result + ((getDuration() == null) ? 0 : getDuration().hashCode());
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
                ", workflowNodeId=" + workflowNodeId +
                ", costMem=" + costMem +
                ", costProcessor=" + costProcessor +
                ", costGpu=" + costGpu +
                ", costBandwidth=" + costBandwidth +
                ", duration=" + duration +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", serialVersionUID=" + serialVersionUID +
                "]";
    }
}