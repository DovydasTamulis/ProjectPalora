package com.pal.controller;
import com.pal.model.PalCharacters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.pal.service.PalCharacterService;

import java.util.List;


@RestController
@RequestMapping("/api/characters")
public class PalCharacterController {

    @Autowired
    private PalCharacterService characterService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<PalCharacters>> getCharactersByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(characterService.getCharactersByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<PalCharacters> createCharacter(@RequestBody PalCharacters character) {
        return ResponseEntity.ok(characterService.createCharacter(character));
    }

    @PostMapping("/{characterId}/level-up")
    public ResponseEntity<Void> levelUpCharacter(@PathVariable String characterId, @RequestParam int xpToAdd) {
        characterService.levelUpCharacter(characterId, xpToAdd);
        return ResponseEntity.ok().build();
    }
}