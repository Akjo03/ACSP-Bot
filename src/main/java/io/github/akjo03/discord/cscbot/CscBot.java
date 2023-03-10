package io.github.akjo03.discord.cscbot;

import io.github.akjo03.discord.cscbot.constants.CscDeployMode;
import io.github.akjo03.discord.cscbot.handlers.CommandsHandler;
import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.discord.cscbot.util.command.CscCommand;
import io.github.akjo03.lib.config.AkjoLibSpringAutoConfiguration;
import io.github.akjo03.lib.logging.Logger;
import io.github.akjo03.lib.logging.LoggerHandler;
import io.github.akjo03.lib.logging.LoggerManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@RequiredArgsConstructor
@Import(AkjoLibSpringAutoConfiguration.class)
public class CscBot {
	private static final Logger LOGGER = LoggerManager.getLogger(CscBot.class);

	private static ConfigurableApplicationContext applicationContext;
	private static JDA jdaInstance;

	private final LoggerHandler loggerHandler;

	private final BotConfigService botConfigService;

	@Getter
	private static CscDeployMode deployMode;

	@Getter
	private static String botName;

	public static void main(String[] args) {
		try {
			SpringApplication.run(CscBot.class, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Bean
	public CommandLineRunner run(ApplicationContext ctx) {
		return args -> {
			applicationContext = (ConfigurableApplicationContext) ctx;

			deployMode = CscDeployMode.getDeployMode(
					System.getenv("CSC_DEPLOY_MODE")
			);
			LOGGER.info("Initializing CscBot in " + deployMode + " mode...");

			loggerHandler.initialize(ctx);

			botConfigService.loadBotConfig();

			JDA jda = null;
			try {
				jda = JDABuilder.create(
						System.getenv("CSC_TOKEN"),
						GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS)
				).build();
			} catch (Exception e) {
				LOGGER.error("Failed to initialize JDA instance! Maybe you have no internet connection, or your token is invalid?");
				e.printStackTrace();
				System.exit(1);
			}
			CscBot.jdaInstance = jda;

			CommandsHandler.setAvailableCommands(ctx.getBeansOfType(CscCommand.class).values().stream().toList());
			CommandsHandler.getAvailableCommands().forEach(command -> command.initializeInternal(applicationContext, jdaInstance, botConfigService));
			jda.addEventListener(ctx.getBean(CommandsHandler.class));

			jda.awaitReady();

			botName = jda.getSelfUser().getName();

			LOGGER.info("CscBot is ready!");
		};
	}

	public static JDA getJdaInstance() {
		return jdaInstance;
	}

	public static void shutdown() {
		jdaInstance.shutdownNow();
		applicationContext.close();
		System.exit(0);
	}

	public static void restart() {
		jdaInstance.shutdownNow();
		ApplicationArguments args = applicationContext.getBean(ApplicationArguments.class);
		Thread restartThread = new Thread(() -> {
			applicationContext.close();
			applicationContext = SpringApplication.run(CscBot.class, args.getSourceArgs());
		});
		restartThread.setDaemon(false);
		restartThread.start();
	}
}