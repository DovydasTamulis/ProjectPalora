package com.pal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pal_characters")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PalCharacters {
    @Id
    private String id;
    private String userId; // Reference to the user's ID
    private String type;
    private Integer level;
    private Integer xp;

}