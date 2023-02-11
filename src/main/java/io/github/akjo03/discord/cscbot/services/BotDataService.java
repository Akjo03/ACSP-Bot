package io.github.akjo03.discord.cscbot.services;

import io.github.akjo03.discord.cscbot.data.CscBotData;
import io.github.akjo03.util.ProjectDirectory;
import io.github.akjo03.util.logging.v2.Logger;
import io.github.akjo03.util.logging.v2.LoggerManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class BotDataService {
	private static final Logger LOGGER = LoggerManager.getLogger(BotDataService.class);

	@Getter
	private final Path botDataPath = Path.of(String.valueOf(ProjectDirectory.getUsersProjectRootDirectory()), "data", "bot_data.json");

	@Getter
	private CscBotData botData;

	private final JsonService jsonService;

	private void createBotDataFileIfNotExists(Runnable onCreated, Runnable onExists) throws IOException {
		if (Files.exists(botDataPath)) {
			LOGGER.info("Bot data file already exists!");
			onExists.run();
			return;
		}

		LOGGER.info("Creating bot data file...");

		Files.createDirectories(botDataPath.getParent());
		Files.createFile(botDataPath);

		LOGGER.success("Bot data file created!");
		onCreated.run();
	}

	public void createBotData() {
		try {
			createBotDataFileIfNotExists(() -> {
				LOGGER.info("Filling bot data file with default values...");

				CscBotData newBotData = new CscBotData();

				try {
					jsonService.objectMapper().writeValue(botDataPath.toFile(), newBotData);
				} catch (IOException e) {
					LOGGER.error("Failed to write default values to bot data file!");
					System.exit(1);
					return;
				}

				this.botData = newBotData;

				LOGGER.success("Bot data file filled with default values!");
			}, () -> {
				LOGGER.info("Reading bot data file...");

				try {
					this.botData = jsonService.objectMapper().readValue(botDataPath.toFile(), CscBotData.class);
				} catch (IOException e) {
					LOGGER.error("Failed to read bot data file!");
					System.exit(1);
					return;
				}

				LOGGER.success("Bot data file read!");
			});
		} catch (IOException e) {
			LOGGER.error("Failed to create bot data file!");
			System.exit(1);
		}
	}

	public void saveBotData() {
		try {
			jsonService.objectMapper().writeValue(botDataPath.toFile(), botData);
		} catch (IOException e) {
			LOGGER.error("Failed to write bot data file!");
		}

		LOGGER.success("Bot data file saved!");
	}
}