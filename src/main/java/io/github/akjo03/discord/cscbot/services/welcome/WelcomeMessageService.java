package io.github.akjo03.discord.cscbot.services.welcome;

import io.github.akjo03.discord.cscbot.CscBot;
import io.github.akjo03.discord.cscbot.constants.CscChannels;
import io.github.akjo03.discord.cscbot.constants.Languages;
import io.github.akjo03.discord.cscbot.data.CscBotMessage;
import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.discord.cscbot.services.BotDataService;
import io.github.akjo03.util.logging.v2.Logger;
import io.github.akjo03.util.logging.v2.LoggerManager;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WelcomeMessageService {
	public static final Logger LOGGER = LoggerManager.getLogger(WelcomeMessageService.class);

	private final BotDataService botDataService;
	private final BotConfigService botConfigService;
	private final WelcomeMessageProvider welcomeMessageProvider;

	public void updateWelcomeMessages() {
		long welcomeChannelId = CscChannels.WELCOME_CHANNEL.getId();
		TextChannel welcomeChannel = CscBot.getJdaInstance().getTextChannelById(welcomeChannelId);
		if (welcomeChannel == null) {
			LOGGER.error("Welcome channel not found!");
			return;
		}

		LOGGER.info("Synchronizing welcome messages...");

		// Delete all messages in the welcome channel that are not in bot data
		MessageHistory history = MessageHistory.getHistoryFromBeginning(welcomeChannel).complete();
		history.getRetrievedHistory().forEach(message -> {
			if (botDataService.getBotData().getMessages().stream()
					.filter(botMessage -> botMessage.getId().equals(message.getId()))
					.filter(botMessage -> botMessage.getLabel().equals("WELCOME_MESSAGE"))
					.findAny().isEmpty()) {
				message.delete().queue();
			}
		});
		// Delete all messages in bot data that are not in the welcome channel
		List<CscBotMessage> messagesToDelete = botDataService.getBotData().getMessages().stream()
				.filter(botMessage -> botMessage.getLabel().equals("WELCOME_MESSAGE"))
				.filter(botMessage -> history.getRetrievedHistory().stream().noneMatch(message -> message.getId().equals(botMessage.getId())))
				.toList();
		botDataService.getBotData().getMessages().removeAll(messagesToDelete);
		botDataService.saveBotData();

		LOGGER.success("Synchronization of welcome messages complete!");

		LOGGER.info("Updating welcome messages if changed...");

		// Update all messages that changed in config data
		botDataService.getBotData().getMessages().stream()
				.filter(botMessage -> botMessage.getLabel().equals("WELCOME_MESSAGE"))
				.forEach(botMessage -> {
					botConfigService.getBotConfig().getMessages().stream()
							.filter(configMessage -> configMessage.getLabel().equals("WELCOME_MESSAGE"))
							.filter(configMessage -> configMessage.getLanguage().equals(botMessage.getLanguage()))
							.forEach(configMessage -> {
								welcomeChannel.retrieveMessageById(botMessage.getId()).queue(message -> {
									if (!configMessage.getMessage().equalsToMessage(message)) {
										LOGGER.info("Updating welcome message in " + botMessage.getLanguage() + "...");
										message.editMessage(configMessage.getMessage().toMessageEditData()).queue();
									}
								});
							});
				});

		LOGGER.success("Update of welcome messages complete!");

		LOGGER.info("Creating welcome messages if they don't exist...");

		if (botDataService.getBotData().getMessages().stream()
				.filter(message -> message.getLabel().equals("WELCOME_MESSAGE"))
				.filter(message -> message.getLanguage().equals(Languages.ENGLISH.toString()))
				.findAny().isEmpty()) {
			LOGGER.info("Creating welcome message in English...");
			MessageCreateData messageCreateData = welcomeMessageProvider.getWelcomeMessageCreateData(Languages.ENGLISH);
			if (messageCreateData == null) {
				return;
			}
			welcomeChannel.sendMessage(messageCreateData).queue(sentMessage -> {
				CscBotMessage botWelcomeMessage = CscBotMessage.Builder.create()
						.setId(sentMessage.getId())
						.setLabel("WELCOME_MESSAGE")
						.setLanguage(Languages.ENGLISH)
						.build();

				botDataService.getBotData().getMessages().add(botWelcomeMessage);
				botDataService.saveBotData();
			});
		}

		if (botDataService.getBotData().getMessages().stream()
				.filter(message -> message.getLabel().equals("WELCOME_MESSAGE"))
				.filter(message -> message.getLanguage().equals(Languages.GERMAN.toString()))
				.findAny().isEmpty()) {
			LOGGER.info("Creating welcome message in German...");
			MessageCreateData messageCreateData = welcomeMessageProvider.getWelcomeMessageCreateData(Languages.GERMAN);
			if (messageCreateData == null) {
				return;
			}
			welcomeChannel.sendMessage(messageCreateData).queue(sentMessage -> {
				CscBotMessage botWelcomeMessage = CscBotMessage.Builder.create()
						.setId(sentMessage.getId())
						.setLabel("WELCOME_MESSAGE")
						.setLanguage(Languages.GERMAN)
						.build();

				botDataService.getBotData().getMessages().add(botWelcomeMessage);
				botDataService.saveBotData();
			});
		}

		LOGGER.success("Creation of welcome messages complete!");
	}
}