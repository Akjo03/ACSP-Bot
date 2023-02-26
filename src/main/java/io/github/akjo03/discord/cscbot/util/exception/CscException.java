package io.github.akjo03.discord.cscbot.util.exception;

import io.github.akjo03.discord.cscbot.constants.Languages;
import io.github.akjo03.discord.cscbot.data.config.message.CscBotConfigMessage;
import io.github.akjo03.discord.cscbot.services.ErrorMessageService;
import net.dv8tion.jda.api.entities.channel.unions.GuildMessageChannelUnion;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public abstract class CscException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private final CscBotConfigMessage message;

	protected CscException(
			String titleLabel,
			String descriptionLabel,
			List<String> titlePlaceholders,
			List<String> descriptionPlaceholders,
			Languages language,
			ErrorMessageService errorMessageService
	) {
		super();
		this.message = errorMessageService.getErrorMessage(
				titleLabel,
				descriptionLabel,
				titlePlaceholders,
				descriptionPlaceholders,
				Optional.ofNullable(language)
		);
	}

	public void sendMessage(@NotNull GuildMessageChannelUnion channel) {
		channel.sendMessage(message.toMessageCreateData()).queue();
	}
}
