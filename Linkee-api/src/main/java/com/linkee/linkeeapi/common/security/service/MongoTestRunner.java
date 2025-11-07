package com.linkee.linkeeapi.common.security.service;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MongoTestRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        String uri = "mongodb+srv://kimsangjaemongodb:kimsangjaemongodb@cluster0.o9c574k.mongodb.net/linkee?retryWrites=true&w=majority";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("linkee");
            // 컬렉션 이름 하나 조회해보기
            boolean exists = database.listCollectionNames().into(new java.util.ArrayList<>()).contains("chat_messages");
            System.out.println("MongoDB 연결 성공! chat_messages 컬렉션 존재 여부: " + exists);
        } catch (Exception e) {
            System.err.println("MongoDB 연결 실패!");
            e.printStackTrace();
        }
    }
}
