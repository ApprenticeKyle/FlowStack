package org.r2learning.project.application.cmd;

import lombok.Data;

@Data
public class ListProjectCmd {
    private String name;
    private String status;
    private Long ownerId;
    private Boolean archived; // 是否归档
    private Boolean starred; // 是否收藏
    private String priority; // 优先级筛选
    private String tag; // 标签筛选
    private String sortBy; // 排序字段: createdAt, updatedAt, name, progress
    private String sortOrder; // 排序方向: ASC, DESC
    private Integer page = 0;
    private Integer size = 20;
}