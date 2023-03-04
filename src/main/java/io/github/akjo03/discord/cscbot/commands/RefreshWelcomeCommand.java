package io.github.akjo03.discord.cscbot.commands;

import io.github.akjo03.discord.cscbot.config.LocaleConfiguration;
import io.github.akjo03.discord.cscbot.constants.CscCommandArgumentTypes;
import io.github.akjo03.discord.cscbot.constants.Languages;
import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.discord.cscbot.services.welcome.WelcomeMessageService;
import io.github.akjo03.discord.cscbot.util.command.CscCommand;
import io.github.akjo03.discord.cscbot.util.command.argument.CscCommandArguments;
import io.github.akjo03.lib.logging.EnableLogger;
import io.github.akjo03.lib.logging.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@EnableLogger
public class RefreshWelcomeCommand extends CscCommand {
	private Logger logger;

	private BotConfigService botConfigService;
	private WelcomeMessageService welcomeMessageService;
	private LocaleConfiguration localeConfiguration;

	@Autowired
	public void setBotConfigService(BotConfigService botConfigService) {
		this.botConfigService = botConfigService;
	}

	@Autowired
	public void setWelcomeMessageService(WelcomeMessageService welcomeMessageService) {
		this.welcomeMessageService = welcomeMessageService;
	}

	@Autowired
	public void setLocaleConfiguration(LocaleConfiguration localeConfiguration) {
		this.localeConfiguration = localeConfiguration;
	}

	protected RefreshWelcomeCommand() {
		super("refreshWelcome");
	}

	@Override
	public void initialize(@NotNull ApplicationContext applicationContext, @NotNull JDA jdaInstance) {}

	@Override
	public void execute(@NotNull MessageReceivedEvent event, @NotNull CscCommandArguments arguments) {
		logger.info("Executing refreshWelcome command...");

		botConfigService.loadBotConfig();

		String languageChoice = arguments.getCommandArgument(name, "language", CscCommandArgumentTypes.CHOICE);
		if (languageChoice == null) {
			languageChoice = localeConfiguration.getDefaultLocale();
		}
		Languages language = Languages.fromCode(languageChoice);
		welcomeMessageService.updateWelcomeMessages(Optional.of(language));

		logger.success("Command refreshWelcome successfully executed!");
	}
}