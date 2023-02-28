package io.github.akjo03.discord.cscbot.services.welcome;

import io.github.akjo03.discord.cscbot.CscBot;
import io.github.akjo03.discord.cscbot.constants.CscChannels;
import io.github.akjo03.discord.cscbot.constants.Languages;
import io.github.akjo03.discord.cscbot.data.CscBotMessage;
import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.discord.cscbot.services.BotDataService;
import io.github.akjo03.lib.logging.EnableLogger;
import io.github.akjo03.lib.logging.Logger;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@EnableLogger
public class WelcomeMessageService {
	private Logger logger;

	private final BotDataService botDataService;
	private final BotConfigService botConfigService;
	private final WelcomeMessageProvider welcomeMessageProvider;

	public void updateWelcomeMessages(Optional<Languages> language) {
		long welcomeChannelId = CscChannels.WELCOME_CHANNEL.getId();
		TextChannel welcomeChannel = CscBot.getJdaInstance().getTextChannelById(welcomeChannelId);
		if (welcomeChannel == null) {
			logger.error("Welcome channel not found!");
			return;
		}

		logger.info("Synchronizing welcome messages...");

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

		logger.success("Synchronization of welcome messages complete!");

		logger.info("Updating welcome messages if changed...");

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
										logger.info("Updating welcome message in " + botMessage.getLanguage() + "...");
										message.editMessage(configMessage.getMessage().toMessageEditData()).queue();
									}
								});
							});
				});

		logger.success("Update of welcome messages complete!");

		logger.info("Creating welcome messages if they don't exist...");

		boolean updateEnglish = language.isEmpty() || language.get().equals(Languages.ENGLISH);
		boolean updateGerman = language.isEmpty() || language.get().equals(Languages.GERMAN);

		if (updateEnglish) {
			if (botDataService.getBotData().getMessages().stream()
					.filter(message -> message.getLabel().equals("WELCOME_MESSAGE"))
					.filter(message -> message.getLanguage().equals(Languages.ENGLISH.toString()))
					.findAny().isEmpty()) {
				logger.info("Creating welcome message in English...");
				MessageCreateData messageCreateData = welcomeMessageProvider.getWelcomeMessageCreateData(Optional.of(Languages.ENGLISH));
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
		}

		if (updateGerman) {
			if (botDataService.getBotData().getMessages().stream()
					.filter(message -> message.getLabel().equals("WELCOME_MESSAGE"))
					.filter(message -> message.getLanguage().equals(Languages.GERMAN.toString()))
					.findAny().isEmpty()) {
				logger.info("Creating welcome message in German...");
				MessageCreateData messageCreateData = welcomeMessageProvider.getWelcomeMessageCreateData(Optional.of(Languages.GERMAN));
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
		}

		logger.success("Creation of welcome messages complete!");
	}
}