package io.github.akjo03.discord.cscbot.data;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CscBotHelpMessageRepository extends MongoRepository<CscBotHelpMessage, String> {}