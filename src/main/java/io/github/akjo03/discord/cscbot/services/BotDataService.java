package io.github.akjo03.discord.cscbot.services;

import io.github.akjo03.discord.cscbot.config.LocaleConfiguration;
import io.github.akjo03.discord.cscbot.data.*;
import io.github.akjo03.lib.logging.EnableLogger;
import io.github.akjo03.lib.logging.Logger;
import io.github.akjo03.lib.path.ProjectDirectory;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@EnableLogger
public class BotDataService {
	private Logger logger;

	private final JsonService jsonService;
	private final ProjectDirectory projectDirectory;
	private final LocaleConfiguration localeConfiguration;

	private final CscBotLocalizedMessageRepository localizedMessageRepository;
	private final CscBotPaginatedMessageRepository paginatedMessageRepository;
	private final CscBotHelpMessageRepository helpMessageRepository;

	public List<CscBotLocalizedMessage> getAllLocalizedMessages() {
		return localizedMessageRepository.findAll();
	}

	public CscBotLocalizedMessage getLocalizedMessage(String messageId, String label) {
		return localizedMessageRepository.findAll().stream()
				.filter(botMessage -> botMessage.getMessageId().equals(messageId))
				.filter(botMessage -> botMessage.getLabel().equals(label))
				.findAny().orElse(null);
	}

	public void addLocalizedMessage(CscBotLocalizedMessage localizedMessage) {
		localizedMessageRepository.insert(localizedMessage);
	}

	public void removeLocalizedMessageByLabel(String label) {
		localizedMessageRepository.deleteAll(localizedMessageRepository.findAll().stream()
				.filter(localizedMessage -> localizedMessage.getLabel().equals(label))
				.toList()
		);
	}

	public void removeLocalizedMessages(List<CscBotLocalizedMessage> localizedMessages) {
		localizedMessageRepository.deleteAll(localizedMessages);
	}

	public @Nullable CscBotPaginatedMessage getPaginatedMessage(String messageId) {
		return paginatedMessageRepository.findAll().stream()
				.filter(paginatedMessage -> paginatedMessage.getMessageId().equals(messageId))
				.findFirst()
				.orElse(null);
	}

	public void addPaginatedMessage(CscBotPaginatedMessage paginatedMessage) {
		paginatedMessageRepository.insert(paginatedMessage);
	}

	public void setPaginatedMessagePage(String messageId, int page) {
		paginatedMessageRepository.findAll().stream()
				.filter(paginatedMessage -> paginatedMessage.getMessageId().equals(messageId))
				.findFirst()
				.ifPresent(paginatedMessage -> {
					paginatedMessage.setPage(page);
					paginatedMessageRepository.save(paginatedMessage);
				});
	}

	public void removePaginatedMessage(String messageId) {
		paginatedMessageRepository.findAll().stream()
				.filter(paginatedMessage -> paginatedMessage.getMessageId().equals(messageId))
				.findFirst()
				.ifPresent(paginatedMessageRepository::delete);
	}

	public @Nullable CscBotHelpMessage getHelpMessage(String messageId) {
		return helpMessageRepository.findAll().stream()
				.filter(helpMessage -> helpMessage.getMessageId().equals(messageId))
				.findFirst()
				.orElse(null);
	}

	public void addHelpMessage(CscBotHelpMessage helpMessage) {
		helpMessageRepository.insert(helpMessage);
	}

	public void setHelpMessagePath(String messageId, String path) {
		helpMessageRepository.findAll().stream()
				.filter(helpMessage -> helpMessage.getMessageId().equals(messageId))
				.findFirst()
				.ifPresent(helpMessage -> {
					helpMessage.setPath(path);
					helpMessageRepository.save(helpMessage);
				});
	}

	public void removeHelpMessage(String messageId) {
		helpMessageRepository.findAll().stream()
				.filter(helpMessage -> helpMessage.getMessageId().equals(messageId))
				.findFirst()
				.ifPresent(helpMessageRepository::delete);
	}
}