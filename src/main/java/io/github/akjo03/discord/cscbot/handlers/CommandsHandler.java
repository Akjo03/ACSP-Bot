package io.github.akjo03.discord.cscbot.handlers;

import io.github.akjo03.discord.cscbot.constants.Languages;
import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.discord.cscbot.services.ErrorMessageService;
import io.github.akjo03.discord.cscbot.services.JsonService;
import io.github.akjo03.discord.cscbot.services.StringsResourceService;
import io.github.akjo03.discord.cscbot.util.commands.CscCommand;
import io.github.akjo03.lib.logging.EnableLogger;
import io.github.akjo03.lib.logging.Logger;
import io.github.akjo03.lib.logging.LoggerManager;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@EnableLogger
public class CommandsHandler extends ListenerAdapter {
	private Logger logger;

	private static final List<CscCommand> availableCommands = new ArrayList<>();

	private final BotConfigService botConfigService;
	private final StringsResourceService stringsResourceService;
	private final ErrorMessageService errorMessageService;
	private final JsonService jsonService;

	public static void setAvailableCommands(List<CscCommand> availableCommands) {
		availableCommands.forEach(cscCommand -> LoggerManager.getLogger(CommandsHandler.class).info("Registered command " + cscCommand.getName()));
		CommandsHandler.availableCommands.addAll(availableCommands);
	}

	public static List<CscCommand> getAvailableCommands() {
		return availableCommands;
	}

	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		}
		if (!event.isFromGuild()) {
			return;
		}

		String commandPrefix = botConfigService.getBotConfig().getCommandPrefix();

		if (!event.getMessage().getContentRaw().startsWith(commandPrefix)) {
			return;
		}
		botConfigService.loadBotConfig();

		String command = event.getMessage().getContentRaw().substring(commandPrefix.length());
		List<String> commandParts = Arrays.stream(command.split(" ")).toList();

		if (commandParts.isEmpty()) {
				return;
		}

		String commandName = commandParts.get(0);
		if (commandName.equals(commandPrefix)) {
			return;
		}

		String commandArgStr = commandParts.stream().skip(1).reduce((a, b) -> a + " " + b).orElse("");

		CscCommand cscCommand = availableCommands.stream()
				.filter(c -> c.getName().equals(commandName))
				.findFirst()
				.orElse(null);

		if (cscCommand == null) {
			logger.info("User " + event.getAuthor().getAsTag() + " tried to execute command \"" + commandName + "\" but it was not found!");

			String closestCommand = botConfigService.closestCommand(commandName);

			event.getChannel().sendMessage(errorMessageService.getErrorMessage(
					"errors.unknown_command.title",
					"errors.unknown_command.description",
					"CommandsHandler.onMessageReceived",
					Instant.now(),
					Optional.empty(),
					List.of(),
					List.of(
							commandName,
							closestCommand != null ? stringsResourceService.getString("errors.special.similar_command", Optional.of(Languages.ENGLISH), closestCommand) : ""
					)
			).toMessageCreateData()).queue();

			return;
		}

		cscCommand.executeInternal(event, errorMessageService);
	}
}