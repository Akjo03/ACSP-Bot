package io.github.akjo03.discord.cscbot.services.help;

import io.github.akjo03.discord.cscbot.CscBot;
import io.github.akjo03.discord.cscbot.constants.CscCommandArgumentTypes;
import io.github.akjo03.discord.cscbot.constants.CscComponentTypes;
import io.github.akjo03.discord.cscbot.data.CscBotHelpMessage;
import io.github.akjo03.discord.cscbot.data.config.command.CscBotCommand;
import io.github.akjo03.discord.cscbot.data.config.command.CscBotSubcommand;
import io.github.akjo03.discord.cscbot.data.config.command.argument.CscBotCommandArgument;
import io.github.akjo03.discord.cscbot.data.config.components.CscBotConfigActionRowComponent;
import io.github.akjo03.discord.cscbot.data.config.components.CscBotConfigInteractionButtonComponent;
import io.github.akjo03.discord.cscbot.data.config.message.CscBotConfigMessage;
import io.github.akjo03.discord.cscbot.data.config.message.CscBotConfigMessageEmbedField;
import io.github.akjo03.discord.cscbot.services.*;
import io.github.akjo03.lib.logging.EnableLogger;
import io.github.akjo03.lib.logging.Logger;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@EnableLogger
public class HelpCommandService {
	private Logger logger;

	private final BotConfigService botConfigService;
	private final BotDataService botDataService;
	private final StringsResourceService stringsResourceService;
	private final ErrorMessageService errorMessageService;
	private final DiscordMessageService discordMessageService;

	public CscBotConfigMessage getCommandHelpMessage(CscBotCommand command) {
		CscBotConfigMessage helpMessage = botConfigService.getMessage(
				"COMMAND_HELP_MESSAGE",
				Optional.empty(),
				command.getCommand(),
				command.getDescription(),
				getArgumentsList(command),
				getSubcommandsList(command),
				CscBot.getBotName(),
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
						.withZone(ZoneId.of("Europe/Zurich"))
						.format(Instant.now())
		);

		return helpMessage;
	}

	private String getArgumentsList(CscBotCommand command) {
		if (command.getArguments().isEmpty()) {
			return stringsResourceService.getString(
					"command.help.command.no_arguments",
					Optional.empty()
			);
		}

		return command.getArguments().stream()
				.map(CscBotCommandArgument::getName)
				.reduce("", (a, b) -> a + "\n-  " + b);
	}

	private String getSubcommandsList(CscBotCommand command) {
		if (!command.getSubcommands().isAvailable()) {
			return stringsResourceService.getString(
					"command.help.command.no_subcommands",
					Optional.empty()
			);
		}

		return command.getSubcommands().getDefinitions().stream()
				.map(CscBotSubcommand::getName)
				.reduce("", (a, b) -> a + "\n-  " + b);
	}

	public CscBotConfigMessage getCommandArgumentsHelpMessage(CscBotCommand command, Message message) {
		List<CscBotConfigMessageEmbedField> fields = command.getArguments().stream()
				.map(argument -> botConfigService.getField(
						"COMMAND_ARGUMENT_HELP_FIELD",
						Optional.empty(),
						argument.getName(),
						argument.getDescription() + "\n\n" + getArgumentLines(argument, message)
				)).toList();

		CscBotConfigMessage helpMessage = botConfigService.getMessage(
				"COMMAND_ARGUMENTS_HELP_MESSAGE",
				Optional.empty(),
				command.getCommand(),
				CscBot.getBotName(),
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
						.withZone(ZoneId.of("Europe/Zurich"))
						.format(Instant.now())
		);
		helpMessage.getEmbeds().get(0).setFields(fields);

		return helpMessage;
	}

	private String getArgumentLines(CscBotCommandArgument<?> argument, Message message) {
		String typeLine = stringsResourceService.getString(
				"command.help.command.argument.type_line",
				Optional.empty(),
				stringsResourceService.getString(
						CscCommandArgumentTypes.fromString(argument.getType()).getKey(),
						Optional.empty()
				),
				message.getGuild().getId(),
				message.getChannel().getId(),
				stringsResourceService.getString(
						CscCommandArgumentTypes.fromString(argument.getType()).getTooltipKey(),
						Optional.empty()
				)
		);

		String optionalLine = stringsResourceService.getString(
				"command.help.command.argument.optional_line",
				Optional.empty(),
				!argument.isRequired() ? stringsResourceService.getString(
						"general.yes",
						Optional.empty()
				) : stringsResourceService.getString(
						"general.no",
						Optional.empty()
				)
		);

		String defaultsLine = stringsResourceService.getString(
				"command.help.command.argument.default_line",
				Optional.empty(),
				argument.getData().getDefaultValue() != null ? argument.getData().getDefaultValue().toString() : "-"
		);

		return Stream.of(typeLine, optionalLine, defaultsLine)
				.reduce("", (a, b) -> a + "\n" + b);
	}

	public CscBotConfigMessage getCommandSubcommandsHelpMessage(CscBotCommand command, Message message) {
		List<CscBotConfigMessageEmbedField> fields = command.getSubcommands().getDefinitions().stream()
				.map(subcommand -> botConfigService.getField(
						"COMMAND_SUBCOMMAND_HELP_FIELD",
						Optional.empty(),
						subcommand.getName(),
						subcommand.getDescription(),
						getSubcommandArgumentList(subcommand)
				)).toList();

		CscBotConfigMessage helpMessage = botConfigService.getMessage(
				"COMMAND_SUBCOMMANDS_HELP_MESSAGE",
				Optional.empty(),
				command.getCommand(),
				CscBot.getBotName(),
				DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
						.withZone(ZoneId.of("Europe/Zurich"))
						.format(Instant.now())
		);
		helpMessage.getEmbeds().get(0).setFields(fields);

		return helpMessage;
	}

	private String getSubcommandArgumentList(CscBotSubcommand subcommand) {
		return subcommand.getArguments().stream()
				.map(CscBotCommandArgument::getName)
				.reduce("", (a, b) -> a + "\n-  " + b);
	}

	public void closeHelp(ButtonInteractionEvent event) {
		botDataService.removeHelpMessage(event.getMessageId());
		event.getMessage().delete().queue();
	}

	public void backToCommand(@NotNull ButtonInteractionEvent event, CscBotHelpMessage helpMessage) {
		String commandName = getCommandNameFromPath(helpMessage.getPath());

		CscBotCommand command = getCommandByName(commandName, event.getChannel());
		if (command == null) { return; }

		CscBotConfigActionRowComponent commandHelpComponent = getCommandHelpComponent(command);
		if (commandHelpComponent == null) { return; }

		event.editMessage(
				discordMessageService.addComponentsToMessage(
						getCommandHelpMessage(command).toMessageEditData(),
						commandHelpComponent
				)
		).queue(sentMessage -> botDataService.setHelpMessagePath(
				event.getMessageId(),
				"/help/command/" + commandName
		));
	}

	public String getCommandNameFromPath(String path) {
		return StringUtils.replace(path, "/help/command/", "").split("/")[0];
	}

	public @Nullable CscBotCommand getCommandByName(String commandName, MessageChannelUnion eventChannel) {
		CscBotCommand command = botConfigService.getCommand(commandName, Optional.empty());
		if (command == null) {
			eventChannel.sendMessage(errorMessageService.getErrorMessage(
					"errors.command.help.command_not_found.title",
					"errors.command.help.command_not_found.description",
					List.of(),
					List.of(
							commandName
					),
					Optional.empty()
			).toMessageCreateData()).queue();
			return null;
		}

		return command;
	}

	public @Nullable CscBotConfigActionRowComponent getCommandHelpComponent(CscBotCommand command) {
		CscBotConfigActionRowComponent commandHelpComponent = botConfigService.getComponent(
				"COMMAND_HELP_COMPONENT",
				CscComponentTypes.ACTION_ROW
		);
		if (commandHelpComponent == null) {
			logger.error("Action row component not found: COMMAND_HELP_COMPONENT");
			return null;
		}

		commandHelpComponent.getComponents().stream()
				.map(CscBotConfigInteractionButtonComponent.class::cast)
				.forEach(button -> {
					button.setSource("command_help");

					if (button.getInteractionId().equals("show_arguments:command_help")) {
						button.setDisabled(command.getArguments().isEmpty());
					}
					if (button.getInteractionId().equals("show_subcommands:command_help")) {
						button.setDisabled(!command.getSubcommands().isAvailable());
					}
				});

		return commandHelpComponent;
	}

	public @Nullable CscBotConfigActionRowComponent getCommandArgumentsHelpComponent() {
		CscBotConfigActionRowComponent commandArgumentsHelpComponent = botConfigService.getComponent(
				"COMMAND_ARGUMENTS_HELP_COMPONENT",
				CscComponentTypes.ACTION_ROW
		);
		if (commandArgumentsHelpComponent == null) {
			logger.error("Action row component not found: COMMAND_ARGUMENTS_HELP_COMPONENT");
			return null;
		}

		commandArgumentsHelpComponent.getComponents().stream()
				.map(CscBotConfigInteractionButtonComponent.class::cast)
				.forEach(button -> button.setSource("command_arguments_help"));

		return commandArgumentsHelpComponent;
	}

	public @Nullable CscBotConfigActionRowComponent getCommandSubcommandsHelpComponent() {
		CscBotConfigActionRowComponent commandSubcommandsHelpComponent = botConfigService.getComponent(
				"COMMAND_SUBCOMMANDS_HELP_COMPONENT",
				CscComponentTypes.ACTION_ROW
		);
		if (commandSubcommandsHelpComponent == null) {
			logger.error("Action row component not found: COMMAND_SUBCOMMANDS_HELP_COMPONENT");
			return null;
		}

		commandSubcommandsHelpComponent.getComponents().stream()
				.map(CscBotConfigInteractionButtonComponent.class::cast)
				.forEach(button -> button.setSource("command_subcommands_help"));

		return commandSubcommandsHelpComponent;
	}
}