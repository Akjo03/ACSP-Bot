package io.github.akjo03.discord.cscbot.util.command.argument.serialization;

import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.akjo03.discord.cscbot.data.config.command.argument.CscBotCommandArgument;

public class CscBotCommandArgumentModule extends SimpleModule {
	public CscBotCommandArgumentModule() {
		addDeserializer(CscBotCommandArgument.class, new CscBotCommandArgumentDeserializer());
	}
}