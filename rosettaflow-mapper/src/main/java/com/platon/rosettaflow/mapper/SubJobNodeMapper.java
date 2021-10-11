package com.platon.rosettaflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platon.rosettaflow.dto.SubJobNodeDto;
import com.platon.rosettaflow.mapper.domain.SubJobNode;

import java.util.List;

/**
 * t_sub_job_node
 *
 * @author admin
 */
public interface SubJobNodeMapper extends BaseMapper<SubJobNode> {

    /**
     * 获取所有子工作流运行中的节点
     *
     * @return 运行中节点列表
     */
    List<SubJobNodeDto> getRunningNodeWithWorkIdAndNodeNum();

}