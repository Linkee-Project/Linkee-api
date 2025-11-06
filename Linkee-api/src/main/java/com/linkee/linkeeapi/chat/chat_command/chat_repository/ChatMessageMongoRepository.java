package com.linkee.linkeeapi.chat.chat_command.chat_repository;

import com.linkee.linkeeapi.chat.chat_command.chat_domain.entity.ChatMessageMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageMongoRepository extends MongoRepository<ChatMessageMongo, String> {

    List<ChatMessageMongo> findByRoomIdOrderBySentAtAsc(Long roomId);

    List<ChatMessageMongo> findAllByRoomIdOrderBySentAtAsc(Long roomId);
}
