package io.github.akjo03.discord.cscbot.util.exception;

import io.github.akjo03.discord.cscbot.constants.Languages;
import io.github.akjo03.discord.cscbot.data.config.message.CscBotConfigMessage;
import io.github.akjo03.discord.cscbot.services.ErrorMessageService;
import net.dv8tion.jda.api.entities.channel.unions.GuildMessageChannelUnion;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public class CscException extends RuntimeException {
	@Serial private static final long serialVersionUID = 1L;

	private final CscBotConfigMessage message;

	public CscException(
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

	public CscBotConfigMessage getErrorMessage() {
		return message;
	}
}
