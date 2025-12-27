package org.r2learning.project.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

import java.util.List;

@FeignClient(name = "task-service")
public interface TaskFeignClient {

    @GetMapping("/tasks")
    List<Map<String, Object>> getTasksByProjectId(@RequestParam("projectId") Long projectId);

    @PostMapping("/tasks")
    Map<String, Object> createTask(@RequestBody Map<String, Object> taskData);
}
