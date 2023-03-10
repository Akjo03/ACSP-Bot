package io.github.akjo03.discord.cscbot.services;

import io.github.akjo03.discord.cscbot.data.config.components.CscBotConfigActionRowComponent;
import io.github.akjo03.lib.logging.EnableLogger;
import io.github.akjo03.lib.logging.Logger;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;
import net.dv8tion.jda.api.utils.messages.MessageEditData;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@EnableLogger
public class DiscordMessageService {
	private Logger logger;

	private final StringsResourceService stringsResourceService;
	private final BotConfigService botConfigService;

	public MessageCreateData addComponentsToMessage(MessageCreateData message, CscBotConfigActionRowComponent actionRow) {
		MessageCreateData newMessage;
		try {
			newMessage = MessageCreateBuilder.from(message)
					.setActionRow(actionRow.toActionRow(stringsResourceService, botConfigService))
					.build();
		} catch (Exception e) {
			newMessage = message;
		}

		return newMessage;
	}

	public MessageEditData addComponentsToMessage(MessageEditData message, CscBotConfigActionRowComponent actionRow) {
		MessageEditData newMessage;
		try {
			newMessage = MessageEditBuilder.from(message)
					.setActionRow(actionRow.toActionRow(stringsResourceService, botConfigService))
					.build();
		} catch (Exception e) {
			newMessage = message;
		}

		return newMessage;
	}
}
