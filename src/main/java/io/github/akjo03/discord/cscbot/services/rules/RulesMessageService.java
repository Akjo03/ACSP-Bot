package io.github.akjo03.discord.cscbot.services.rules;

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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RulesMessageService {
	public static final Logger LOGGER = LoggerManager.getLogger(RulesMessageService.class);

	private final BotDataService botDataService;
	private final BotConfigService botConfigService;
	private final RulesMessageProvider rulesMessageProvider;

	public void updateRulesMessages() {
		long rulesChannelId = CscChannels.RULES_CHANNEL.getId();
		TextChannel rulesChannel = CscBot.getJdaInstance().getTextChannelById(rulesChannelId);
		if (rulesChannel == null) {
			LOGGER.error("Rules channel not found!");
			return;
		}

		LOGGER.info("Synchronizing rules messages...");

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

		LOGGER.success("Synchronization of rules messages complete!");

		LOGGER.info("Updating rules messages if changed...");

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
										LOGGER.info("Updating rules message in " + botMessage.getLanguage() + "...");
										message.editMessage(configMessage.getMessage().toMessageEditData()).queue();
									}
								});
							});
				});

		LOGGER.success("Update of rules messages complete!");

		LOGGER.info("Creating rules messages if they don't exist...");

		if (botDataService.getBotData().getMessages().stream()
				.filter(message -> message.getLabel().equals("RULES_MESSAGE"))
				.filter(message -> message.getLanguage().equals(Languages.ENGLISH.toString()))
				.findAny().isEmpty()) {
			LOGGER.info("Creating rules message in English...");
			rulesChannel.sendMessage(rulesMessageProvider.getRulesMessageCreateData(Languages.ENGLISH)).queue(sentMessage -> {
				CscBotMessage botRulesMessage = CscBotMessage.Builder.create()
						.setId(sentMessage.getId())
						.setLabel("RULES_MESSAGE")
						.setLanguage(Languages.ENGLISH)
						.build();

				botDataService.getBotData().getMessages().add(botRulesMessage);
				botDataService.saveBotData();
			});
		}

		if (botDataService.getBotData().getMessages().stream()
				.filter(message -> message.getLabel().equals("RULES_MESSAGE"))
				.filter(message -> message.getLanguage().equals(Languages.GERMAN.toString()))
				.findAny().isEmpty()) {
			LOGGER.info("Creating rules message in German...");
			rulesChannel.sendMessage(rulesMessageProvider.getRulesMessageCreateData(Languages.GERMAN)).queue(sentMessage -> {
				CscBotMessage botRulesMessage = CscBotMessage.Builder.create()
						.setId(sentMessage.getId())
						.setLabel("RULES_MESSAGE")
						.setLanguage(Languages.GERMAN)
						.build();

				botDataService.getBotData().getMessages().add(botRulesMessage);
				botDataService.saveBotData();
			});
		}

		LOGGER.success("Creation of rules messages complete!");
	}
}