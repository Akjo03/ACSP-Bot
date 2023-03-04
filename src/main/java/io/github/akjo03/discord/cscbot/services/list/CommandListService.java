package io.github.akjo03.discord.cscbot.services.list;

import io.github.akjo03.discord.cscbot.CscBot;
import io.github.akjo03.discord.cscbot.data.config.command.CscBotCommand;
import io.github.akjo03.discord.cscbot.data.config.message.CscBotConfigMessage;
import io.github.akjo03.discord.cscbot.data.config.message.CscBotConfigMessageEmbedField;
import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.discord.cscbot.services.StringsResourceService;
import io.github.akjo03.lib.logging.EnableLogger;
import io.github.akjo03.lib.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@EnableLogger
public class CommandListService {
	private Logger logger;

	private final BotConfigService botConfigService;
	private final StringsResourceService stringsResourceService;

	private static final int DESCRIPTION_SPACE_LENGTH = 60;
	private static final int DESCRIPTION_MAX_LENGTH = 1024;

	public CscBotConfigMessage getListMessage(List<CscBotCommand> commands, int page, int commandsPerPage) {
		int maxPage = botConfigService.getCommandsPageCount(commandsPerPage);

		CscBotConfigMessage message = botConfigService.getMessage(
				"LIST_MESSAGE",
				Optional.empty(),
				String.valueOf(page),
				String.valueOf(maxPage),
				CscBot.getBotName(),
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
						.withZone(ZoneId.of("Europe/Zurich"))
						.format(Instant.now())
		);

		List<String> commandFieldDescriptions = commands.stream()
				.map(command -> {
					String description = stringsResourceService.getString(
							StringUtils.stripStart(command.getDescription(), "$"),
							Optional.empty()
					);
					if (StringUtils.isBlank(description)) {
						description = "No description provided.";
					}
					if (description.length() > DESCRIPTION_MAX_LENGTH) {
						description = description.substring(0, DESCRIPTION_MAX_LENGTH -3) + "...";
					}
					if (description.length() < DESCRIPTION_SPACE_LENGTH) {
						description = description + "\u1cbc".repeat(DESCRIPTION_SPACE_LENGTH - description.length());
					}
					return description;
				}).toList();

		List<CscBotConfigMessageEmbedField> embedFields = new ArrayList<>();
		for (int commandIndex = 0; commandIndex < commands.size(); commandIndex++) {
			CscBotCommand command = commands.get(commandIndex);
			int commandNumber = botConfigService.getCommandNumber(command.getCommand());
			boolean hasSubcommands = command.getSubcommands().isAvailable();
			String commandName = command.getCommand();
			String commandDescription = commandFieldDescriptions.get(commandIndex);

			CscBotConfigMessageEmbedField commandNumberField = botConfigService.getField(
					"NUMBERED_PREFIX_FIELD",
					Optional.empty(),
					String.valueOf(commandNumber)
			);

			CscBotConfigMessageEmbedField commandField = botConfigService.getField(
					"LIST_COMMAND_FIELD",
					Optional.empty(),
					commandName,
					commandDescription
			);

			CscBotConfigMessageEmbedField commandLineBreakField = botConfigService.getField(
					"EMPTY_FIELD_INLINE",
					Optional.empty()
			);

			embedFields.add(commandNumberField);
			embedFields.add(commandField);
			embedFields.add(commandLineBreakField);

			if (hasSubcommands) {
				CscBotConfigMessageEmbedField subcommandsSpacerField = botConfigService.getField(
						"EMPTY_FIELD_INLINE",
						Optional.empty()
				);

				CscBotConfigMessageEmbedField subcommandsField = botConfigService.getField(
						"LIST_SUBCOMMANDS_FIELD",
						Optional.empty(),
						command.getSubcommands().toListString()
				);

				CscBotConfigMessageEmbedField subcommandsLineBreakField = botConfigService.getField(
						"EMPTY_FIELD_INLINE",
						Optional.empty()
				);

				embedFields.add(subcommandsSpacerField);
				embedFields.add(subcommandsField);
				embedFields.add(subcommandsLineBreakField);
			}
		}

		message.getEmbeds().get(0).setFields(embedFields);

		return message;
	}
}
