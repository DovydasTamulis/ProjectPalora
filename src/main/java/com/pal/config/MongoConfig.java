package com.pal.config;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import org.bson.Document;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {

    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDatabaseFactory) {
        return new MongoTemplate(mongoDatabaseFactory);
    }

    public void createTTLIndex(MongoTemplate mongoTemplate) {
        MongoCollection<Document> collection = mongoTemplate.getCollection("tasks");

        Document indexKey = new Document("expiresAt", 1);

        IndexOptions indexOptions = new IndexOptions().expireAfter(20L, java.util.concurrent.TimeUnit.SECONDS);

        collection.createIndex(indexKey, indexOptions);
    }
}