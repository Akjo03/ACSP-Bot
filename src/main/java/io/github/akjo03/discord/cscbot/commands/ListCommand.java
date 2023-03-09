package io.github.akjo03.discord.cscbot.commands;

import io.github.akjo03.discord.cscbot.constants.CscCommandArgumentTypes;
import io.github.akjo03.discord.cscbot.constants.CscComponentTypes;
import io.github.akjo03.discord.cscbot.data.CscBotPaginatedMessage;
import io.github.akjo03.discord.cscbot.data.config.command.CscBotCommand;
import io.github.akjo03.discord.cscbot.data.config.components.CscBotConfigActionRowComponent;
import io.github.akjo03.discord.cscbot.data.config.components.CscBotConfigInteractionButtonComponent;
import io.github.akjo03.discord.cscbot.handlers.list.ListCommandInteractionHandler;
import io.github.akjo03.discord.cscbot.services.BotDataService;
import io.github.akjo03.discord.cscbot.services.DiscordMessageService;
import io.github.akjo03.discord.cscbot.services.list.CommandListService;
import io.github.akjo03.discord.cscbot.util.command.CommandInitializer;
import io.github.akjo03.discord.cscbot.util.command.CscCommand;
import io.github.akjo03.discord.cscbot.util.command.argument.CscCommandArguments;
import io.github.akjo03.lib.logging.EnableLogger;
import io.github.akjo03.lib.logging.Logger;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@EnableLogger
public class ListCommand extends CscCommand {
	private Logger logger;

	public static final int COMMANDS_PER_PAGE = 5;

	private CommandListService commandListService;
	private DiscordMessageService discordMessageService;
	private BotDataService botDataService;

	@Autowired
	protected void setCommandListService(CommandListService commandListService) {
		this.commandListService = commandListService;
	}

	@Autowired
	protected void setDiscordMessageService(DiscordMessageService discordMessageService) {
		this.discordMessageService = discordMessageService;
	}

	@Autowired
	protected void setBotDataService(BotDataService botDataService) {
		this.botDataService = botDataService;
	}

	protected ListCommand() {
		super("list");
	}

	@Override
	public void initialize(@NotNull CommandInitializer initializer) {
		initializer.registerInteractionHandler(ListCommandInteractionHandler.class);
	}

	@Override
	public void execute(@NotNull MessageReceivedEvent event, @NotNull CscCommandArguments arguments) {
		logger.info("Executing list command...");

		Integer page = arguments.getCommandArgument("page", CscCommandArgumentTypes.INTEGER);
		if (page == null) {
			logger.error("Page argument is null!");
			return;
		}

		List<CscBotCommand> commands = getBotConfigService().getPaginatedCommands(page, COMMANDS_PER_PAGE);
		if (commands.isEmpty()) {
			event.getChannel().sendMessage(getErrorMessageService().getErrorMessage(
					"errors.command.list.page_not_found.title",
					"errors.command.list.page_not_found.description",
					List.of(),
					List.of(
							String.valueOf(page)
					),
					Optional.empty()
			).toMessageCreateData()).queue();
			return;
		}

		CscBotConfigActionRowComponent paginationActionsRow = getBotConfigService().getComponent("PAGINATED_MESSAGE_COMPONENT", CscComponentTypes.ACTION_ROW);
		if (paginationActionsRow == null) {
			logger.error("Pagination action row is null!");
			return;
		}
		paginationActionsRow.getComponents().stream()
				.map(CscBotConfigInteractionButtonComponent.class::cast)
				.forEach(button -> {
					button.setSource("list_command");

					if (button.getInteractionId().equals("previous_page:list_command")) {
						button.setDisabled(page == 1);
					} else if (button.getInteractionId().equals("next_page:list_command")) {
						button.setDisabled(page == getBotConfigService().getCommandsPageCount(COMMANDS_PER_PAGE));
					}
				});

		event.getChannel().sendMessage(
				discordMessageService.addComponentsToMessage(
						commandListService.getListMessage(
								commands, page, COMMANDS_PER_PAGE
						).toMessageCreateData(),
						paginationActionsRow
				)
		).queue(sentMessage -> botDataService.addPaginatedMessage(
				CscBotPaginatedMessage.Builder.create()
						.setMessageId(sentMessage.getId())
						.setLabel("LIST_MESSAGE")
						.setPage(page)
						.build()
		));

		logger.success("Command list successfully executed!");
	}
}