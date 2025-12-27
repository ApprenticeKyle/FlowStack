package org.r2learning.project.service;

import java.util.List;
import java.util.Map;
import org.r2learning.project.client.TaskFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class TaskServiceClient {

    @Autowired
    private TaskFeignClient taskFeignClient;

    public Mono<List<Map<String, Object>>> getTasksByProjectId(Long projectId) {
        // Feign is blocking, so we wrap it in Mono.fromCallable and subscribe on a
        // bounded elastic scheduler
        return Mono.fromCallable(() -> taskFeignClient.getTasksByProjectId(projectId))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Map<String, Object>> createTaskForProject(Long projectId, Map<String, Object> taskData) {
        return Mono.fromCallable(() -> taskFeignClient.createTask(taskData))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
