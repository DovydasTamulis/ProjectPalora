package com.pal.service;

import com.pal.model.PalCharacters;
import com.pal.repository.PalCharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PalCharacterService {

    @Autowired
    private PalCharacterRepository palCharacterRepository;

    public List<PalCharacters> getCharactersByUserId(String userId) {
        return palCharacterRepository.findByUserId(userId);
    }

    public PalCharacters createCharacter(PalCharacters character) {
        return palCharacterRepository.save(character);
    }

    public void levelUpCharacter(String characterId, int xpToAdd) {
        PalCharacters character = palCharacterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("Character not found"));

        character.setXp(character.getXp() + xpToAdd);

        int newLevel = character.getXp() / 100;
        character.setLevel(newLevel);

        palCharacterRepository.save(character);
    }

}
