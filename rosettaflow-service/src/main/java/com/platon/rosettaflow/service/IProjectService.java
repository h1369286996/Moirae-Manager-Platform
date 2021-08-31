package com.platon.rosettaflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platon.rosettaflow.dto.ProjMemberDto;
import com.platon.rosettaflow.dto.ProjectDto;
import com.platon.rosettaflow.mapper.domain.Project;
import com.platon.rosettaflow.mapper.domain.ProjectMember;
import com.platon.rosettaflow.mapper.domain.ProjectTemp;

import java.util.List;

/**
 * @author admin
 * @date 2021/8/16
 */
public interface IProjectService extends IService<Project> {

    /**
     * 新增项目
     * @param project 项目信息
     */
    void addProject(Project project);

    /**
     * 修改项目
     * @param project 项目信息
     */
    void updateProject(Project project);

    /**
     * 查询项目列表-分页
     * @param projectName 项目名称
     * @param current 当前页数
     * @param size 每页条数
     * @return ProjectDto
     */
    List<ProjectDto>  queryProjectList(String projectName, Integer current, Integer size);

    /**
     * 查询项目详情
     * @param id 项目id
     * @return Project
     */
    Project queryProjectDetails(Long id);

    /**
     * 删除项目
     * @param id 项目id
     */
    void deleteProject(Long id);

    /**
     * 查询项目模板列表
     * @return
     */
    List<ProjectTemp> queryProjectTempList();

    /**
     * 查询项目成员列表
     * @param projectId 项目id
     * @param userName 用户昵称
     * @return
     */
    List<ProjMemberDto> queryProjMemberList(Long projectId, String userName);

    /**
     * 新增项目成员
     * @param projectMember
     * @return
     */
    void addProjMember(ProjectMember projectMember);

    /**
     * 修改项目成员
     * @param projectMember
     * @return
     */
    void updateProjMember(ProjectMember projectMember);

    /**
     * 删除项目成员
     * @param projMemberId 项目成员id
     * @return
     */
    void deleteProjMember(Long projMemberId);

}
