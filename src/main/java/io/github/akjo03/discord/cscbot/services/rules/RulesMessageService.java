package io.github.akjo03.discord.cscbot.services.rules;

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
public class RulesMessageService {
	private Logger logger;

	private final BotDataService botDataService;
	private final BotConfigService botConfigService;
	private final RulesMessageProvider rulesMessageProvider;

	public void updateRulesMessages(Optional<Languages> language) {
		long rulesChannelId = CscChannels.RULES_CHANNEL.getId();
		TextChannel rulesChannel = CscBot.getJdaInstance().getTextChannelById(rulesChannelId);
		if (rulesChannel == null) {
			logger.error("Rules channel not found!");
			return;
		}

		logger.info("Synchronizing rules messages...");

		// Delete all messages in the rules channel that are not in bot data
		MessageHistory history = MessageHistory.getHistoryFromBeginning(rulesChannel).complete();
		history.getRetrievedHistory().forEach(message -> {
			if (botDataService.getBotData().getMessages().stream()
					.filter(botMessage -> botMessage.getId().equals(message.getId()))
					.filter(botMessage -> botMessage.getLabel().equals("RULES_MESSAGE"))
					.findAny().isEmpty()) {
				message.delete().queue();
			}
		});
		// Delete all messages in bot data that are not in the welcome channel
		List<CscBotMessage> messagesToDelete = botDataService.getBotData().getMessages().stream()
				.filter(botMessage -> botMessage.getLabel().equals("RULES_MESSAGE"))
				.filter(botMessage -> history.getRetrievedHistory().stream().noneMatch(message -> message.getId().equals(botMessage.getId())))
				.toList();
		botDataService.getBotData().getMessages().removeAll(messagesToDelete);
		botDataService.saveBotData();

		logger.success("Synchronization of rules messages complete!");

		logger.info("Updating rules messages if changed...");

		// Update all messages that changed in config data
		botDataService.getBotData().getMessages().stream()
				.filter(botMessage -> botMessage.getLabel().equals("RULES_MESSAGE"))
				.forEach(botMessage -> {
					botConfigService.getBotConfig().getMessages().stream()
							.filter(configMessage -> configMessage.getLabel().equals("RULES_MESSAGE"))
							.filter(configMessage -> configMessage.getLanguage().equals(botMessage.getLanguage()))
							.forEach(configMessage -> {
								rulesChannel.retrieveMessageById(botMessage.getId()).queue(message -> {
									if (!configMessage.getMessage().equalsToMessage(message)) {
										logger.info("Updating rules message in " + botMessage.getLanguage() + "...");
										message.editMessage(configMessage.getMessage().toMessageEditData()).queue();
									}
								});
							});
				});

		logger.success("Update of rules messages complete!");

		logger.info("Creating rules messages if they don't exist...");

		boolean updateEnglish = language.isEmpty() || language.get().equals(Languages.ENGLISH);
		boolean updateGerman = language.isEmpty() || language.get().equals(Languages.GERMAN);

		if (updateEnglish) {
			if (botDataService.getBotData().getMessages().stream()
					.filter(message -> message.getLabel().equals("RULES_MESSAGE"))
					.filter(message -> message.getLanguage().equals(Languages.ENGLISH.toString()))
					.findAny().isEmpty()) {
				logger.info("Creating rules message in English...");
				MessageCreateData messageCreateData = rulesMessageProvider.getRulesMessageCreateData(Optional.of(Languages.ENGLISH));
				if (messageCreateData == null) {
					return;
				}
				rulesChannel.sendMessage(messageCreateData).queue(sentMessage -> {
					CscBotMessage botRulesMessage = CscBotMessage.Builder.create()
							.setId(sentMessage.getId())
							.setLabel("RULES_MESSAGE")
							.setLanguage(Languages.ENGLISH)
							.build();

					botDataService.getBotData().getMessages().add(botRulesMessage);
					botDataService.saveBotData();
				});
			}
		}

		if (updateGerman) {
			if (botDataService.getBotData().getMessages().stream()
					.filter(message -> message.getLabel().equals("RULES_MESSAGE"))
					.filter(message -> message.getLanguage().equals(Languages.GERMAN.toString()))
					.findAny().isEmpty()) {
				logger.info("Creating rules message in German...");
				MessageCreateData messageCreateData = rulesMessageProvider.getRulesMessageCreateData(Optional.of(Languages.GERMAN));
				if (messageCreateData == null) {
					return;
				}
				rulesChannel.sendMessage(messageCreateData).queue(sentMessage -> {
					CscBotMessage botRulesMessage = CscBotMessage.Builder.create()
							.setId(sentMessage.getId())
							.setLabel("RULES_MESSAGE")
							.setLanguage(Languages.GERMAN)
							.build();

					botDataService.getBotData().getMessages().add(botRulesMessage);
					botDataService.saveBotData();
				});
			}
		}

		logger.success("Creation of rules messages complete!");
	}
}