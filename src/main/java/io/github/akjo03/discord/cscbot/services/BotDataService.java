package io.github.akjo03.discord.cscbot.services;

import io.github.akjo03.discord.cscbot.config.LocaleConfiguration;
import io.github.akjo03.discord.cscbot.constants.Languages;
import io.github.akjo03.discord.cscbot.data.CscBotData;
import io.github.akjo03.discord.cscbot.data.CscBotHelpMessage;
import io.github.akjo03.discord.cscbot.data.CscBotLocalizedMessage;
import io.github.akjo03.discord.cscbot.data.CscBotPaginatedMessage;
import io.github.akjo03.lib.logging.EnableLogger;
import io.github.akjo03.lib.logging.Logger;
import io.github.akjo03.lib.path.ProjectDirectory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@EnableLogger
public class BotDataService {
	private Logger logger;

	@Getter
	private Path botDataPath;
	@Getter
	private CscBotData botData;

	private final JsonService jsonService;
	private final ProjectDirectory projectDirectory;
	private final LocaleConfiguration localeConfiguration;

	private void createBotDataFileIfNotExists(Runnable onCreated, Runnable onExists) throws IOException {
		if (botDataPath == null) {
			botDataPath = projectDirectory.getProjectRootDirectory().resolve("data").resolve("bot_data.json");
		}

		if (Files.exists(botDataPath)) {
			onExists.run();
			return;
		}

		Files.createDirectories(botDataPath.getParent());
		Files.createFile(botDataPath);
		onCreated.run();
	}

	public void loadBotData() {
		try {
			createBotDataFileIfNotExists(() -> {
				CscBotData newBotData = new CscBotData();

				try {
					jsonService.objectMapper().writeValue(botDataPath.toFile(), newBotData);
				} catch (IOException e) {
					logger.error("Failed to write default values to bot data file!");
					System.exit(1);
					return;
				}

				this.botData = newBotData;
			}, () -> {
				try {
					this.botData = jsonService.objectMapper().readValue(botDataPath.toFile(), CscBotData.class);
				} catch (IOException e) {
					logger.error("Failed to read bot data file!");
					System.exit(1);
				}
			});
		} catch (IOException e) {
			logger.error("Failed to create bot data file!");
			System.exit(1);
		}
	}

	public void saveBotData() {
		try {
			jsonService.objectMapper().writeValue(botDataPath.toFile(), botData);
		} catch (IOException e) {
			logger.error("Failed to write bot data file!");
		}
	}

	public @Nullable CscBotLocalizedMessage getLocalizedMessage(String label, Optional<Languages> language) {
		loadBotData();
		String languageStr = language.orElse(Languages.fromCode(localeConfiguration.getDefaultLocale())).toString();

		return botData.getLocalizedMessages().stream()
				.filter(localizedMessage -> localizedMessage.getLabel().equals(label))
				.filter(localizedMessage -> localizedMessage.getLanguage().equals(languageStr))
				.findFirst()
				.orElse(null);
	}

	public void addLocalizedMessage(CscBotLocalizedMessage localizedMessage) {
		loadBotData();
		botData.getLocalizedMessages().add(localizedMessage);
		saveBotData();
	}

	public @Nullable CscBotPaginatedMessage getPaginatedMessage(String id) {
		loadBotData();
		return botData.getPaginatedMessages().stream()
				.filter(paginatedMessage -> paginatedMessage.getId().equals(id))
				.findFirst()
				.orElse(null);
	}

	public void addPaginatedMessage(CscBotPaginatedMessage paginatedMessage) {
		loadBotData();
		botData.getPaginatedMessages().add(paginatedMessage);
		saveBotData();
	}

	public void setPaginatedMessagePage(String id, int page) {
		loadBotData();
		botData.getPaginatedMessages().stream()
				.filter(paginatedMessage -> paginatedMessage.getId().equals(id))
				.findFirst()
				.ifPresent(paginatedMessage -> paginatedMessage.setPage(page));
		saveBotData();
	}

	public void removePaginatedMessage(String id) {
		loadBotData();
		botData.getPaginatedMessages().removeIf(paginatedMessage -> paginatedMessage.getId().equals(id));
		saveBotData();
	}

	public @Nullable CscBotHelpMessage getHelpMessage(String id) {
		loadBotData();
		return botData.getHelpMessages().stream()
				.filter(helpMessage -> helpMessage.getId().equals(id))
				.findFirst()
				.orElse(null);
	}

	public void addHelpMessage(CscBotHelpMessage helpMessage) {
		loadBotData();
		botData.getHelpMessages().add(helpMessage);
		saveBotData();
	}

	public void setHelpMessagePath(String id, String path) {
		loadBotData();
		botData.getHelpMessages().stream()
				.filter(helpMessage -> helpMessage.getId().equals(id))
				.findFirst()
				.ifPresent(helpMessage -> helpMessage.setPath(path));
		saveBotData();
	}

	public void removeHelpMessage(String id) {
		loadBotData();
		botData.getHelpMessages().removeIf(helpMessage -> helpMessage.getId().equals(id));
		saveBotData();
	}
}