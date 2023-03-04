package io.github.akjo03.discord.cscbot.services;

import io.github.akjo03.discord.cscbot.data.config.components.CscBotConfigActionRowComponent;
import io.github.akjo03.lib.logging.EnableLogger;
import io.github.akjo03.lib.logging.Logger;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@EnableLogger
public class DiscordMessageService {
	private Logger logger;

	private final StringsResourceService stringsResourceService;

	public MessageCreateData addComponentsToMessage(MessageCreateData message, CscBotConfigActionRowComponent actionRow) {
		MessageCreateData newMessage;
		try {
			newMessage = MessageCreateBuilder.from(message)
					.setActionRow(actionRow.toActionRow(stringsResourceService))
					.build();
		} catch (Exception e) {
			newMessage = message;
		}
		return newMessage;
	}
}
