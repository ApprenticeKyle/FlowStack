package org.r2learning.project.infrastructure.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.r2learning.project.infrastructure.db.dataobject.ProjectTagDO;
import org.r2learning.project.infrastructure.db.repository.JpaProjectTagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectTagService {

    private final JpaProjectTagRepository projectTagRepository;

    @Transactional
    public void saveProjectTags(Long projectId, List<String> tags) {
        // 先删除旧的标签
        projectTagRepository.deleteByProjectId(projectId);
        
        // 如果tags不为null且不为空，创建新的标签
        if (tags != null && !tags.isEmpty()) {
            // 去重并过滤空值
            List<String> uniqueTags = tags.stream()
                .distinct()
                .filter(tag -> tag != null && !tag.trim().isEmpty())
                .collect(Collectors.toList());
            
            if (!uniqueTags.isEmpty()) {
                List<ProjectTagDO> projectTags = uniqueTags.stream()
                    .map(tag -> {
                        ProjectTagDO projectTag = new ProjectTagDO();
                        projectTag.setProjectId(projectId);
                        projectTag.setTag(tag.trim());
                        projectTag.setColor(getDefaultColorForTag(tag));
                        projectTag.setCreatedAt(LocalDateTime.now());
                        return projectTag;
                    })
                    .collect(Collectors.toList());
                
                projectTagRepository.saveAll(projectTags);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<String> getTagsByProjectId(Long projectId) {
        return projectTagRepository.findByProjectId(projectId).stream()
            .map(ProjectTagDO::getTag)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteProjectTags(Long projectId) {
        projectTagRepository.deleteByProjectId(projectId);
    }

    /**
     * 根据标签名称返回默认颜色
     */
    private String getDefaultColorForTag(String tag) {
        // 简单的颜色分配逻辑，可以根据需要扩展
        String[] colors = {"#3B82F6", "#10B981", "#F59E0B", "#EF4444", "#8B5CF6", "#EC4899"};
        int index = Math.abs(tag.hashCode()) % colors.length;
        return colors[index];
    }
}

