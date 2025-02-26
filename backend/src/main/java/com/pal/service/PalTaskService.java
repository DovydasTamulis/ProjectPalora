package com.pal.service;


import com.pal.model.PalTask;
import com.pal.repository.PalTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PalTaskService {

    @Autowired
    private PalTaskRepository taskRepository;

    private String getCurrentUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private PalTask getTaskForCurrentUser(String taskId) {
        String currentUserId = getCurrentUserId();
        PalTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        if (!task.getUserId().equals(currentUserId)) {
            throw new RuntimeException("Unauthorized access: You do not have permission to perform this action.");
        }
        return task;
    }

    public List<PalTask> getTasksByUserId(String userId) {
        return taskRepository.findByUserId(userId);
    }

    public PalTask createTask(PalTask task) {
        PalTask newTask = new PalTask(
                task.getUserId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getDuration()
        );
        return taskRepository.save(newTask);
    }

    public void deleteTask(String taskId) {
        PalTask task = getTaskForCurrentUser(taskId);
        taskRepository.delete(task);
    }

    public PalTask startTask(String taskId) {
        PalTask task = getTaskForCurrentUser(taskId);
        if (!task.isRunning()) {
            task.setRunning(true);
        }
        return taskRepository.save(task);
    }

    public PalTask pauseTask(String taskId) {
        PalTask task = getTaskForCurrentUser(taskId);
        if (task.isRunning()) {
            task.setRunning(false);
        }
        return taskRepository.save(task);
    }

    public PalTask resetTask(String taskId) {
        PalTask task = getTaskForCurrentUser(taskId);
        task.setElapsedTime(0);
        task.setRunning(false);
        task.setCompleted(false);
        return taskRepository.save(task);
    }

    public PalTask tickOffTime(String taskId, long elapsedTime) {
        PalTask task = getTaskForCurrentUser(taskId);
        task.setElapsedTime(elapsedTime);
        if (task.getElapsedTime() >= task.getDuration()) {
            task.setCompleted(true);
            task.setRunning(false);
        }
        return taskRepository.save(task);
    }
}
