package io.github.akjo03.discord.cscbot.services;

import io.github.akjo03.discord.cscbot.constants.Languages;
import io.github.akjo03.discord.cscbot.data.config.CscBotConfig;
import io.github.akjo03.discord.cscbot.data.config.command.CscBotCommand;
import io.github.akjo03.discord.cscbot.data.config.message.CscBotConfigMessage;
import io.github.akjo03.discord.cscbot.data.config.message.CscBotConfigMessageWrapper;
import io.github.akjo03.discord.cscbot.data.config.string.CscBotConfigString;
import io.github.akjo03.util.ProjectDirectory;
import io.github.akjo03.util.logging.v2.Logger;
import io.github.akjo03.util.logging.v2.LoggerManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class BotConfigService {
	private static final Logger LOGGER = LoggerManager.getLogger(BotConfigService.class);

	@Getter
	private final Path botConfigPath = Path.of(String.valueOf(ProjectDirectory.getUsersProjectRootDirectory()), "data", "bot_config.json");

	@Getter
	private CscBotConfig botConfig;

	private final JsonService jsonService;

	public void loadBotConfig() {
		try {
			botConfig = jsonService.objectMapper().readValue(botConfigPath.toFile(), CscBotConfig.class);
		} catch (IOException e) {
			LOGGER.error("Could not load bot config!", e);
			System.exit(1);
		}
	}

	public CscBotConfigMessage getMessage(String label, Languages language) {
		CscBotConfigMessageWrapper messageWrapper = botConfig.getMessages().stream()
				.filter(message -> message.getLabel().equals(label))
				.filter(message -> message.getLanguage().equals(language.toString()))
				.findFirst()
				.orElse(null);

		if (messageWrapper == null) {
			LOGGER.error("Could not find message with label " + label + " and language " + language.toString() + "!");
			return null;
		}

		return messageWrapper.getMessage();
	}

	public CscBotConfigString getString(String label, Languages language) {
		CscBotConfigString result = botConfig.getStrings().stream()
				.filter(str -> str.getLabel().equals(label))
				.filter(str -> str.getLanguage().equals(language.toString()))
				.findFirst()
				.orElse(null);

		if (result == null) {
			LOGGER.error("Could not find string with label " + label + " and language " + language.toString() + "!");
			return null;
		}

		return result;
	}

	public CscBotCommand getCommand(String name) {
		CscBotCommand result = botConfig.getCommands().stream()
				.filter(command -> command.getCommand().equals(name))
				.findFirst()
				.orElse(null);

		if (result == null) {
			LOGGER.error("Could not find command with name " + name + "!");
			return null;
		}

		return result;
	}
}