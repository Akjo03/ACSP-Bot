package io.github.akjo03.discord.cscbot.data.config.command.argument.data;

import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.discord.cscbot.services.StringsResourceService;
import io.github.akjo03.lib.result.Result;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface CscBotCommandArgumentData<T> {
	T getDefaultValue();
	Result<T> parse(String commandName, String argumentName, String value, MessageReceivedEvent event, BotConfigService botConfigService, StringsResourceService stringsResourceService);
}