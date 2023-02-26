package io.github.akjo03.discord.cscbot.util.commands.arguments;

import io.github.akjo03.discord.cscbot.data.config.command.CscBotCommand;
import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.discord.cscbot.services.ErrorMessageService;
import io.github.akjo03.discord.cscbot.services.JsonService;
import io.github.akjo03.discord.cscbot.services.StringsResourceService;
import io.github.akjo03.lib.logging.Logger;
import io.github.akjo03.lib.logging.LoggerManager;

import java.util.List;

public class CscCommandArgumentParser {
	private static final Logger LOGGER = LoggerManager.getLogger(CscCommandArgumentParser.class);

	private final CscBotCommand commandDefinition;
	private final List<String> args;
	private final String subcommand;
	private final List<String> subcommandArgs;

	private BotConfigService botConfigService;
	private StringsResourceService stringsResourceService;
	private ErrorMessageService errorMessageService;
	private JsonService jsonService;

	private CscCommandArgumentParser(CscBotCommand commandDefinition, List<String> args) {
		this.commandDefinition = commandDefinition;
		this.args = args;
		this.subcommand = null;
		this.subcommandArgs = null;
	}

	private CscCommandArgumentParser(CscBotCommand commandDefinition, List<String> args, String subcommand, List<String> subcommandArgs) {
		this.commandDefinition = commandDefinition;
		this.args = args;
		this.subcommand = subcommand;
		this.subcommandArgs = subcommandArgs;
	}

	public static CscCommandArgumentParser forCommand(CscBotCommand commandDefinition, String argStr) {
		return new CscCommandArgumentParser(commandDefinition, List.of());
	}

	public void setupServices(
			BotConfigService botConfigService,
			StringsResourceService stringsResourceService,
			ErrorMessageService errorMessageService,
			JsonService jsonService
	) {
		this.botConfigService = botConfigService;
		this.stringsResourceService = stringsResourceService;
		this.errorMessageService = errorMessageService;
		this.jsonService = jsonService;
	}

	public List<CscCommandArgument<?, ?>> parse() {
		return List.of();
	}
}