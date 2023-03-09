package io.github.akjo03.discord.cscbot.services;

import io.github.akjo03.discord.cscbot.config.LocaleConfiguration;
import io.github.akjo03.discord.cscbot.constants.CscComponentTypes;
import io.github.akjo03.discord.cscbot.constants.Languages;
import io.github.akjo03.discord.cscbot.data.config.CscBotConfig;
import io.github.akjo03.discord.cscbot.data.config.command.CscBotCommand;
import io.github.akjo03.discord.cscbot.data.config.command.CscBotSubcommand;
import io.github.akjo03.discord.cscbot.data.config.command.CscBotSubcommands;
import io.github.akjo03.discord.cscbot.data.config.command.argument.CscBotCommandArgument;
import io.github.akjo03.discord.cscbot.data.config.components.CscBotConfigComponent;
import io.github.akjo03.discord.cscbot.data.config.components.CscBotConfigComponentWrapper;
import io.github.akjo03.discord.cscbot.data.config.field.CscBotConfigFieldWrapper;
import io.github.akjo03.discord.cscbot.data.config.message.CscBotConfigMessage;
import io.github.akjo03.discord.cscbot.data.config.message.CscBotConfigMessageEmbed;
import io.github.akjo03.discord.cscbot.data.config.message.CscBotConfigMessageEmbedField;
import io.github.akjo03.discord.cscbot.data.config.message.CscBotConfigMessageWrapper;
import io.github.akjo03.discord.cscbot.handlers.CommandsHandler;
import io.github.akjo03.discord.cscbot.util.command.CscCommand;
import io.github.akjo03.lib.logging.EnableLogger;
import io.github.akjo03.lib.logging.Logger;
import io.github.akjo03.lib.path.ProjectDirectory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@EnableLogger
public class BotConfigService {
	private Logger logger;

	@Getter
	private Path botConfigPath;
	@Getter
	private CscBotConfig botConfig;

	private final JsonService jsonService;
	private final StringPlaceholderService stringPlaceholderService;
	private final ProjectDirectory projectDirectory;
	private final StringsResourceService stringsResourceService;

	private final LocaleConfiguration localeConfiguration;

	public void loadBotConfig() {
		if (botConfigPath == null) {
			botConfigPath = projectDirectory.getProjectRootDirectory().resolve("data").resolve("bot_config.json");
		}

		try {
			botConfig = jsonService.objectMapper().readValue(botConfigPath.toFile(), CscBotConfig.class);
		} catch (IOException e) {
			logger.error("Could not load bot config!", e);
			System.exit(1);
		}
	}

	public CscBotConfigMessage getMessage(String label, Optional<Languages> language, String... placeholders) {
		loadBotConfig();
		CscBotConfigMessageWrapper messageWrapper = botConfig.getMessages().stream()
				.filter(message -> message.getLabel().equals(label))
				.filter(message -> message.getLanguage().equals(language.orElse(Languages.fromCode(localeConfiguration.getDefaultLocale())).toString()))
				.findFirst()
				.orElse(null);

		if (messageWrapper == null) {
			logger.error("Could not find message with label " + label + " and language " + language.toString() + "!");
			return null;
		}

		// Make a copy of the message
		CscBotConfigMessage result = CscBotConfigMessage.copy(messageWrapper.getMessage());

		// Replace placeholders with actual values
		result.setContent(stringPlaceholderService.replacePlaceholders(result.getContent(), placeholders));
		for (CscBotConfigMessageEmbed embed : result.getEmbeds()) {
			embed.getAuthor().setName(stringPlaceholderService.replacePlaceholders(embed.getAuthor().getName(), placeholders));
			embed.getAuthor().setUrl(stringPlaceholderService.replacePlaceholders(embed.getAuthor().getUrl(), placeholders));
			embed.getAuthor().setIconURL(stringPlaceholderService.replacePlaceholders(embed.getAuthor().getIconURL(), placeholders));
			embed.setTitle(stringPlaceholderService.replacePlaceholders(embed.getTitle(), placeholders));
			embed.setDescription(stringPlaceholderService.replacePlaceholders(embed.getDescription(), placeholders));
			embed.setUrl(stringPlaceholderService.replacePlaceholders(embed.getUrl(), placeholders));
			embed.setColor(stringPlaceholderService.replacePlaceholders(embed.getColor(), placeholders));
			for (CscBotConfigMessageEmbedField field : embed.getFields()) {
				field.setName(stringPlaceholderService.replacePlaceholders(field.getName(), placeholders));
				field.setValue(stringPlaceholderService.replacePlaceholders(field.getValue(), placeholders));
			}
			embed.setImageURL(stringPlaceholderService.replacePlaceholders(embed.getImageURL(), placeholders));
			embed.setThumbnailURL(stringPlaceholderService.replacePlaceholders(embed.getThumbnailURL(), placeholders));
			embed.getFooter().setText(stringPlaceholderService.replacePlaceholders(embed.getFooter().getText(), placeholders));
			embed.getFooter().setTimestamp(stringPlaceholderService.replacePlaceholders(embed.getFooter().getTimestamp(), placeholders));
			embed.getFooter().setIconURL(stringPlaceholderService.replacePlaceholders(embed.getFooter().getIconURL(), placeholders));
		}

		return result;
	}

	public CscBotConfigMessageEmbedField getField(String label, Optional<Languages> language, String... placeholders) {
		loadBotConfig();
		CscBotConfigFieldWrapper fieldWrapper = botConfig.getFields().stream()
				.filter(message -> message.getLabel().equals(label))
				.filter(message -> message.getLanguage().equals(language.orElse(Languages.fromCode(localeConfiguration.getDefaultLocale())).toString()))
				.findFirst()
				.orElse(null);

		if (fieldWrapper == null) {
			logger.error("Could not find field with label " + label + " and language " + language.toString() + "!");
			return null;
		}

		// Make a copy of the field
		CscBotConfigMessageEmbedField result = CscBotConfigMessageEmbedField.copy(fieldWrapper.getField());

		// Replace placeholders with actual values
		result.setName(stringPlaceholderService.replacePlaceholders(result.getName(), placeholders));
		result.setValue(stringPlaceholderService.replacePlaceholders(result.getValue(), placeholders));

		return result;
	}

	@SuppressWarnings("unchecked")
	public <T extends CscBotConfigComponent> @Nullable T getComponent(String label, CscComponentTypes type) {
		loadBotConfig();
		CscBotConfigComponentWrapper componentWrapper = botConfig.getComponents().stream()
				.filter(component -> component.getLabel().equals(label))
				.findFirst()
				.orElse(null);

		if (componentWrapper == null) {
			logger.error("Could not find component with label " + label + "!");
			return null;
		}

		CscBotConfigComponent component = componentWrapper.getComponent();
		CscComponentTypes componentType = CscComponentTypes.fromString(component.getType());
		if (componentType == null) {
			logger.error("Could not find component type " + component.getType() + " for component with label " + label + "!");
			return null;
		}
		if (componentType != type && type != CscComponentTypes.ANY) {
			logger.error("Component with label " + label + " is not of type " + type.toString() + "!");
			return null;
		}

		return (T) componentWrapper.getComponent();
	}

	public CscBotCommand getCommand(String name, Optional<Languages> language, String... placeholders) {
		loadBotConfig();
		CscBotCommand command = botConfig.getCommands().stream()
				.filter(commandP -> commandP.getCommand().equals(name))
				.findFirst()
				.orElse(null);

		if (command == null) {
			logger.error("Could not find command with name " + name + "!");
			return null;
		}

		CscBotCommand result = CscBotCommand.copy(command);
		result.setDescription(replaceString(result.getDescription(), language, placeholders));
		for (CscBotCommandArgument<?> argument : result.getArguments()) {
			argument.setDescription(replaceString(argument.getDescription(), language, placeholders));
		}
		if (result.getSubcommands().isAvailable()) {
			for (CscBotSubcommand subcommand : result.getSubcommands().getDefinitions()) {
				subcommand.setDescription(replaceString(subcommand.getDescription(), language, placeholders));
				for (CscBotCommandArgument<?> argument : subcommand.getArguments()) {
					argument.setDescription(replaceString(argument.getDescription(), language, placeholders));
				}
			}
		}

		return result;
	}

	private String replaceString(@NotNull String strName, Optional<Languages> strLanguage, String[] strPlaceholders) {
		if (!strName.startsWith("$")) {
			return strName;
		}
		String strLabel = strName.substring(1);

		String str = stringsResourceService.getString(strLabel, strLanguage, strPlaceholders);
		if (str == null) {
			return strName;
		}

		return str;
	}

	public String closestCommand(String commandName) {
		String closest = null;
		double highest = 0;
		for (String name : CommandsHandler.getAvailableCommands().stream().map(CscCommand::getName).toList()) {
			double current = new LevenshteinDistance(10).apply(commandName, name);
			if (current > highest) {
				highest = current;
				closest = name;
			}
		}
		if (highest < 5) {
			return null;
		}

		return closest;
	}

	public String closestSubcommand(String commandName, String subcommandName) {
		String closest = null;
		double highest = 0;
		for (String name : CommandsHandler.getAvailableCommands().stream()
				.filter(command -> command.getName().equals(commandName))
				.map(command -> command.getDefinition().getSubcommands())
				.filter(CscBotSubcommands::isAvailable)
				.map(CscBotSubcommands::getDefinitions)
				.flatMap(Collection::stream)
				.map(CscBotSubcommand::getName)
				.toList()) {
			double current = new LevenshteinDistance(10).apply(subcommandName, name);
			if (current > highest) {
				highest = current;
				closest = name;
			}
		}
		if (highest < 5) {
			return null;
		}

		return closest;
	}

	public List<CscBotCommand> getPaginatedCommands(int page, int pageSize) {
		loadBotConfig();
		return botConfig.getCommands().stream()
				.skip((long) (page-1) * pageSize)
				.limit(pageSize)
				.collect(Collectors.toList());
	}

	public int getCommandsPageCount(int pageSize) {
		loadBotConfig();
		return (int) Math.ceil((double) botConfig.getCommands().size() / pageSize);
	}

	public int getCommandNumber(String commandName) {
		loadBotConfig();
		return botConfig.getCommands().stream()
				.filter(command -> command.getCommand().equals(commandName))
				.mapToInt(command -> botConfig.getCommands().indexOf(command) + 1)
				.findFirst()
				.orElse(-1);
	}
}