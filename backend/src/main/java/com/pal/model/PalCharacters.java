package com.pal.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(collection = "pal_characters")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PalCharacters {
    @Id
    private String id;
    private String userId;
    private String type;
    private Integer level;
    private Integer xp;

}