package com.pal.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Setter
@Getter
@Document(collection = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    @Id
    private String id;
    private String username;
    private String email;
    private String passwordHash;
    private boolean subscription;

}