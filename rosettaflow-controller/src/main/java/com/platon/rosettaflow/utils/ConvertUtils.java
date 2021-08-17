package com.platon.rosettaflow.utils;

import com.platon.rosettaflow.dto.ProjectTemplateDto;
import com.platon.rosettaflow.dto.UserDto;
import com.platon.rosettaflow.vo.projectTemplate.ProjectTemplateVo;
import com.platon.rosettaflow.vo.user.UserVo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 * @date 2021/8/16
 * @description 功能描述
 */
public class ConvertUtils {

    public static UserVo convert2Vo(UserDto userDto) {
        UserVo userVo = new UserVo();
        userVo.setUserName(userDto.getUserName());
        userVo.setAddress(userDto.getAddress());
        userVo.setStatus(userDto.getStatus());
        userVo.setToken(userDto.getToken());
        return userVo;
    }

    public static ProjectTemplateVo convert2Vo(ProjectTemplateDto projectTemplateDto) {
        ProjectTemplateVo projectTemplateVo = new ProjectTemplateVo();
        projectTemplateVo.setId(projectTemplateDto.getId());
        projectTemplateVo.setProjectName(projectTemplateDto.getProjectName());
        projectTemplateVo.setProjectDesc(projectTemplateDto.getProjectDesc());
        return projectTemplateVo;
    }

    public static List<ProjectTemplateVo> convert2Vo(List<ProjectTemplateDto> projectTemplateDtoList) {
        List<ProjectTemplateVo> list = new ArrayList<>();
        if (null != projectTemplateDtoList) {
            projectTemplateDtoList.forEach(projectTemplateDto -> list.add(convert2Vo(projectTemplateDto)));
        }
        return list;
    }
}
