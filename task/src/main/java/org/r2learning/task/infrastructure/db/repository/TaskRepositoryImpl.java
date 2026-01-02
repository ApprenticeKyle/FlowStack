package org.r2learning.task.infrastructure.db.repository;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.r2learning.task.domain.task.Task;
import org.r2learning.task.domain.task.gateway.TaskGateway;
import org.r2learning.task.infrastructure.db.dataobject.TaskDO;
import org.r2learning.task.infrastructure.db.mapper.TaskMapper;
import org.springframework.stereotype.Component;

/**
 * Task Repository Implementation
 * 在Infrastructure层实现Domain层的Gateway接口
 * 负责Entity和DO之间的转换
 */
@Component
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskGateway {

    private final JpaTaskRepository jpaTaskRepository;

    @Override
    public Task save(Task task) {
        TaskDO taskDO = TaskMapper.INSTANCE.toDO(task);
        TaskDO savedDO = jpaTaskRepository.save(taskDO);
        return TaskMapper.INSTANCE.toEntity(savedDO);
    }

    @Override
    public Task findById(Long id) {
        return jpaTaskRepository.findById(id).map(TaskMapper.INSTANCE::toEntity).orElse(null);
    }

    @Override
    public List<Task> findByProjectId(Long projectId) {
        return jpaTaskRepository.findByProjectId(projectId).stream()
            .map(TaskMapper.INSTANCE::toEntity)
            .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        jpaTaskRepository.deleteById(id);
    }
}
