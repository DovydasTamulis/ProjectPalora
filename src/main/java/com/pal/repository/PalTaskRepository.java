package com.pal.repository;

import com.pal.model.PalTask;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PalTaskRepository extends MongoRepository<PalTask, String> {
    List<PalTask> findByUserId(String userId);
}