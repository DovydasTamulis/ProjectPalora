package com.pal.repository;

import com.pal.model.PalCharacters;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PalCharacterRepository extends MongoRepository<PalCharacters, String> {
    List<PalCharacters> findByUserId(String userId);
}