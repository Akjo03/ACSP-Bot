package io.github.akjo03.discord.cscbot.handlers;

import io.github.akjo03.discord.cscbot.constants.CscComponentTypes;
import io.github.akjo03.discord.cscbot.data.CscBotPaginatedMessage;
import io.github.akjo03.discord.cscbot.data.config.command.CscBotCommand;
import io.github.akjo03.discord.cscbot.data.config.components.CscBotConfigActionRowComponent;
import io.github.akjo03.discord.cscbot.data.config.components.CscBotConfigInteractionButtonComponent;
import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.discord.cscbot.services.BotDataService;
import io.github.akjo03.discord.cscbot.services.DiscordMessageService;
import io.github.akjo03.discord.cscbot.services.list.CommandListService;
import io.github.akjo03.discord.cscbot.util.interactions.CscButtonInteractionHandler;
import io.github.akjo03.lib.logging.EnableLogger;
import io.github.akjo03.lib.logging.Logger;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static io.github.akjo03.discord.cscbot.commands.ListCommand.COMMANDS_PER_PAGE;

@Component
@EnableLogger
public class ListCommandInteractionHandler extends CscButtonInteractionHandler {
	private Logger logger;

	private BotDataService botDataService;
	private BotConfigService botConfigService;
	private DiscordMessageService discordMessageService;
	private CommandListService commandListService;

	@Autowired
	protected void setBotDataService(BotDataService botDataService) {
		this.botDataService = botDataService;
	}

	@Autowired
	protected void setBotConfigService(BotConfigService botConfigService) {
		this.botConfigService = botConfigService;
	}

	@Autowired
	protected void setDiscordMessageService(DiscordMessageService discordMessageService) {
		this.discordMessageService = discordMessageService;
	}

	@Autowired
	protected void setCommandListService(CommandListService commandListService) {
		this.commandListService = commandListService;
	}

	protected ListCommandInteractionHandler() {
		super(List.of(
				"previous_page",
				"next_page",
				"close"
		));
	}

	@Override
	public void onExecute(@NotNull ButtonInteractionEvent event) {
		CscBotPaginatedMessage paginatedMessage = botDataService.getPaginatedMessage(event.getMessageId());
		if (paginatedMessage == null) {
			logger.warn("Paginated message not found for id: " + event.getMessageId());
			return;
		}

		switch (event.getComponentId()) {
			case "previous_page" -> updateMessage(event, paginatedMessage.getPage() - 1);
			case "next_page" -> updateMessage(event, paginatedMessage.getPage() + 1);
			case "close" -> closeMessage(event);
		}
	}

	private void updateMessage(ButtonInteractionEvent event, int newPage) {
		List<CscBotCommand> commands = botConfigService.getPaginatedCommands(newPage, COMMANDS_PER_PAGE);
		if (commands.isEmpty()) {
			logger.warn("No commands found for page " + newPage + "!");
			return;
		}

		CscBotConfigActionRowComponent paginationActionsRow = botConfigService.getComponent("PAGINATED_MESSAGE_COMPONENT", CscComponentTypes.ACTION_ROW);
		if (paginationActionsRow == null) {
			logger.error("Pagination action row is null!");
			return;
		}
		paginationActionsRow.getComponents().stream()
				.map(CscBotConfigInteractionButtonComponent.class::cast)
				.forEach(button -> {
					if (button.getInteractionId().equals("previous_page")) {
						button.setDisabled(newPage == 1);
					} else if (button.getInteractionId().equals("next_page")) {
						button.setDisabled(newPage == botConfigService.getCommandsPageCount(COMMANDS_PER_PAGE));
					}
				});

		event.editMessage(
				discordMessageService.addComponentsToMessage(
						commandListService.getListMessage(
								commands, newPage, COMMANDS_PER_PAGE
						).toMessageEditData(),
						paginationActionsRow
				)
		).queue(sentMessage -> botDataService.setPaginatedMessagePage(event.getMessageId(), newPage));
	}

	private void closeMessage(ButtonInteractionEvent event) {
		botDataService.removePaginatedMessage(event.getMessageId());
		event.getMessage().delete().queue();
	}
}