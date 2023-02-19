package io.github.akjo03.discord.cscbot.services;

import io.github.akjo03.discord.cscbot.data.CscBotData;
import io.github.akjo03.lib.logging.EnableLogger;
import io.github.akjo03.lib.logging.Logger;
import io.github.akjo03.lib.path.ProjectDirectory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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

	private void createBotDataFileIfNotExists(Runnable onCreated, Runnable onExists) throws IOException {
		if (botDataPath == null) {
			botDataPath = projectDirectory.getProjectRootDirectory().resolve("data").resolve("bot_data.json");
		}

		if (Files.exists(botDataPath)) {
			logger.info("Bot data file already exists!");
			onExists.run();
			return;
		}

		logger.info("Creating bot data file...");

		Files.createDirectories(botDataPath.getParent());
		Files.createFile(botDataPath);

		logger.success("Bot data file created!");
		onCreated.run();
	}

	public void createBotData() {
		try {
			createBotDataFileIfNotExists(() -> {
				logger.info("Filling bot data file with default values...");

				CscBotData newBotData = new CscBotData();

				try {
					jsonService.objectMapper().writeValue(botDataPath.toFile(), newBotData);
				} catch (IOException e) {
					logger.error("Failed to write default values to bot data file!");
					System.exit(1);
					return;
				}

				this.botData = newBotData;

				logger.success("Bot data file filled with default values!");
			}, () -> {
				logger.info("Reading bot data file...");

				try {
					this.botData = jsonService.objectMapper().readValue(botDataPath.toFile(), CscBotData.class);
				} catch (IOException e) {
					logger.error("Failed to read bot data file!");
					System.exit(1);
					return;
				}

				logger.success("Bot data file read!");
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

		logger.success("Bot data file saved!");
	}
}