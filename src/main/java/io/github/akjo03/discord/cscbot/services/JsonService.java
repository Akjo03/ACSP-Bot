package io.github.akjo03.discord.cscbot.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.akjo03.discord.cscbot.util.command.argument.serialization.CscBotCommandArgumentModule;
import io.github.akjo03.lib.json.JsonPrettyPrinter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JsonService {
	private final JsonPrettyPrinter jsonPrettyPrinter;

	public ObjectMapper objectMapper() {
		return new ObjectMapper()
				.registerModule(new JavaTimeModule())
				.registerModule(new Jdk8Module())
				.registerModule(new CscBotCommandArgumentModule())
				.setDefaultPrettyPrinter(jsonPrettyPrinter)
				.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
				.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}
}