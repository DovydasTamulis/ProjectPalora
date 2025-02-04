package com.pal.service;

import com.pal.model.PalTask;
import com.pal.repository.PalTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PalTaskService {
    @Autowired
    private PalTaskRepository taskRepository;

    public List<PalTask> getTasksByUserId(String userId) {
        return taskRepository.findByUserId(userId);
    }

    public PalTask createTask(PalTask task) {
        PalTask newTask = new PalTask(
                task.getUserId(),
                task.getTitle(),
                task.getDescription(),
                task.getDuration()
        );
        return taskRepository.save(newTask);
    }

    public void deleteTask(String taskId) {
        taskRepository.deleteById(taskId);
    }

    public PalTask startTask(String taskId) {
        PalTask task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
        if (!task.isRunning()) {
            task.setRunning(true);
        }
        return taskRepository.save(task);
    }

    public PalTask pauseTask(String taskId) {
        PalTask task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
        if (task.isRunning()) {
            task.setRunning(false);
        }
        return taskRepository.save(task);
    }

    public PalTask resetTask(String taskId) {
        PalTask task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
        task.setElapsedTime(0);
        task.setRunning(false);
        task.setCompleted(false);
        return taskRepository.save(task);
    }

    public PalTask tickOffTime(String taskId, long elapsedTime) {
        PalTask task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
        task.setElapsedTime(elapsedTime);
        if (task.getElapsedTime() >= task.getDuration()) {
            task.setCompleted(true);
            task.setRunning(false);
        }
        return taskRepository.save(task);
    }
}