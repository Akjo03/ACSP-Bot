package io.github.akjo03.discord.cscbot.handlers;

import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.discord.cscbot.util.commands.CscCommand;
import io.github.akjo03.util.logging.v2.Logger;
import io.github.akjo03.util.logging.v2.LoggerManager;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
public class CommandsHandler extends ListenerAdapter {
	private static final Logger LOGGER = LoggerManager.getLogger(CommandsHandler.class);

	private static final List<CscCommand> availableCommands = new ArrayList<>();

	private final BotConfigService botConfigService;

	public static void setAvailableCommands(List<CscCommand> availableCommands) {
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

		String command = event.getMessage().getContentRaw().substring(commandPrefix.length());
		List<String> commandParts = Arrays.stream(command.split(" ")).toList();

		if (commandParts.isEmpty()) {
				return;
		}

		String commandName = commandParts.get(0);
		List<String> commandArgs = commandParts.subList(1, commandParts.size());

		CscCommand cscCommand = availableCommands.stream()
				.filter(c -> c.getName().equals(commandName))
				.findFirst()
				.orElse(null);

		if (cscCommand == null) {
			LOGGER.info("User " + event.getAuthor().getAsTag() + " tried to execute command " + commandName + " but it was not found!");
			// TODO: Send error message
			return;
		}

		cscCommand.executeInternal(event, commandArgs);
	}
}