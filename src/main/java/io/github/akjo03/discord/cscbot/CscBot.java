package io.github.akjo03.discord.cscbot;

import io.github.akjo03.discord.cscbot.constants.CscDeployMode;
import io.github.akjo03.discord.cscbot.handlers.CommandsHandler;
import io.github.akjo03.discord.cscbot.handlers.RulesMessageHandler;
import io.github.akjo03.discord.cscbot.handlers.WelcomeMessageHandler;
import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.discord.cscbot.services.BotDataService;
import io.github.akjo03.discord.cscbot.util.commands.CscCommand;
import io.github.akjo03.lib.config.AkjoLibSpringAutoConfiguration;
import io.github.akjo03.lib.logging.Logger;
import io.github.akjo03.lib.logging.LoggerHandler;
import io.github.akjo03.lib.logging.LoggerManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@RequiredArgsConstructor
@Import(AkjoLibSpringAutoConfiguration.class)
public class CscBot {
	private static final Logger LOGGER = LoggerManager.getLogger(CscBot.class);

	private static JDA jdaInstance;

	private final LoggerHandler loggerHandler;

	private final BotConfigService botConfigService;
	private final BotDataService botDataService;

	@Getter
	private static CscDeployMode deployMode;

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
			deployMode = CscDeployMode.getDeployMode(
					System.getenv("CSC_DEPLOY_MODE")
			);
			LOGGER.info("Initializing CscBot in " + deployMode + " mode...");

			loggerHandler.initialize(ctx);

			botConfigService.loadBotConfig();
			botDataService.createBotData();

			JDA jda = JDABuilder.create(
					System.getenv("CSC_TOKEN"),
					GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS)
			).build();
			CscBot.jdaInstance = jda;

			CommandsHandler.setAvailableCommands(ctx.getBeansOfType(CscCommand.class).values().stream().toList());
			CommandsHandler.getAvailableCommands().forEach(command -> command.initialize(botConfigService));
			jda.addEventListener(ctx.getBean(CommandsHandler.class));

			jda.addEventListener(ctx.getBean(WelcomeMessageHandler.class));
			jda.addEventListener(ctx.getBean(RulesMessageHandler.class));

			jda.awaitReady();

			LOGGER.info("CscBot is ready!");
		};
	}

	public static JDA getJdaInstance() {
		return jdaInstance;
	}
}