package io.github.akjo03.discord.cscbot.util.command.argument.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import io.github.akjo03.discord.cscbot.constants.CscCommandArgumentTypes;
import io.github.akjo03.discord.cscbot.data.config.command.argument.CscBotCommandArgument;
import io.github.akjo03.discord.cscbot.data.config.command.argument.data.CscBotCommandArgumentData;
import io.github.akjo03.discord.cscbot.data.config.command.argument.data.CscBotCommandArgumentIntegerData;
import io.github.akjo03.discord.cscbot.data.config.command.argument.data.CscBotCommandArgumentStringData;
import io.github.akjo03.discord.cscbot.data.config.command.argument.data.choice.CscBotCommandArgumentChoiceData;

import java.io.IOException;

public class CscBotCommandArgumentDeserializer extends StdDeserializer<CscBotCommandArgument<?>> {
	public CscBotCommandArgumentDeserializer() {
		this(null);
	}

	public CscBotCommandArgumentDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public CscBotCommandArgument<?> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
		JsonNode node = jsonParser.getCodec().readTree(jsonParser);
		String name = node.get("name").asText();
		String type = node.get("type").asText();
		String description = node.get("description").asText();
		boolean required = node.get("required").asBoolean();
		JsonNode dataNode = node.get("data");

		CscCommandArgumentTypes argumentType = CscCommandArgumentTypes.fromString(type);
		if (argumentType == null) {
			throw MismatchedInputException.from(jsonParser, CscBotCommandArgument.class, "Invalid argument type: " + type + "!");
		}
		CscBotCommandArgumentData<?> data = getDataObject(jsonParser, dataNode, argumentType);
		return new CscBotCommandArgument<>(name, type, description, required, data);
	}

	private CscBotCommandArgumentData<?> getDataObject(JsonParser jsonParser, JsonNode dataNode, CscCommandArgumentTypes type) throws IOException {
		return switch (type) {
			case INTEGER -> jsonParser.getCodec().treeToValue(dataNode, CscBotCommandArgumentIntegerData.class);
			case STRING -> jsonParser.getCodec().treeToValue(dataNode, CscBotCommandArgumentStringData.class);
			case CHOICE -> jsonParser.getCodec().treeToValue(dataNode, CscBotCommandArgumentChoiceData.class);
		};
	}
}
