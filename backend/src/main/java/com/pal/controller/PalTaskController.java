package com.pal.controller;

import com.pal.model.PalTask;
import com.pal.service.PalTaskService;
import com.pal.util.ApiResponse;
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
    public ResponseEntity<ApiResponse<List<PalTask>>> getTasksByUserId() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<PalTask> tasks = taskService.getTasksByUserId(userId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Tasks retrieved successfully", tasks));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PalTask>> createTask(@RequestBody PalTask task, @RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
        task.setUserId(email);
        PalTask createdTask = taskService.createTask(task);
        return ResponseEntity.ok(new ApiResponse<>(201, "Task created successfully", createdTask));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable String taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Task deleted successfully", null));
    }

    @PostMapping("/{taskId}/start")
    public ResponseEntity<ApiResponse<PalTask>> startTask(@PathVariable String taskId) {
        PalTask updatedTask = taskService.startTask(taskId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Task started successfully", updatedTask));
    }

    @PostMapping("/{taskId}/pause")
    public ResponseEntity<ApiResponse<PalTask>> pauseTask(@PathVariable String taskId) {
        PalTask updatedTask = taskService.pauseTask(taskId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Task paused successfully", updatedTask));
    }

    @PostMapping("/{taskId}/reset")
    public ResponseEntity<ApiResponse<PalTask>> resetTask(@PathVariable String taskId) {
        PalTask updatedTask = taskService.resetTask(taskId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Task reset successfully", updatedTask));
    }

    @PostMapping("/{taskId}/tick")
    public ResponseEntity<ApiResponse<PalTask>> tickOffTime(@PathVariable String taskId, @RequestParam long elapsedTime) {
        PalTask updatedTask = taskService.tickOffTime(taskId, elapsedTime);
        return ResponseEntity.ok(new ApiResponse<>(200, "Task ticked off successfully", updatedTask));
    }
}