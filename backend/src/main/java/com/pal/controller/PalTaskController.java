package com.pal.controller;

import com.pal.model.PalTask;
import com.pal.service.PalTaskService;
import com.pal.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class PalTaskController {
    @Autowired
    private PalTaskService taskService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<List<PalTask>> getTasksByUserId() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(taskService.getTasksByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<PalTask> createTask(@RequestBody PalTask task, @RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
        task.setUserId(email);
        return ResponseEntity.ok(taskService.createTask(task));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable String taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{taskId}/start")
    public ResponseEntity<PalTask> startTask(@PathVariable String taskId) {
        return ResponseEntity.ok(taskService.startTask(taskId));
    }

    @PostMapping("/{taskId}/pause")
    public ResponseEntity<PalTask> pauseTask(@PathVariable String taskId) {
        return ResponseEntity.ok(taskService.pauseTask(taskId));
    }

    @PostMapping("/{taskId}/reset")
    public ResponseEntity<PalTask> resetTask(@PathVariable String taskId) {
        return ResponseEntity.ok(taskService.resetTask(taskId));
    }

    @PostMapping("/{taskId}/tick")
    public ResponseEntity<PalTask> tickOffTime(@PathVariable String taskId, @RequestParam long elapsedTime) {
        return ResponseEntity.ok(taskService.tickOffTime(taskId, elapsedTime));
    }
}