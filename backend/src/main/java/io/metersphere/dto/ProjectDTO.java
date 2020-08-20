package io.metersphere.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectDTO {

    private String id;
    private String name;
    private String workspaceId;
    private String workspaceName;
    private String description;
    private Long createTime;
    private Long updateTime;
    private String tapdId;
    private String jiraKey;

}
