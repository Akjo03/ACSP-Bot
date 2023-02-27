package io.github.akjo03.discord.cscbot.util.exception;

import io.github.akjo03.discord.cscbot.constants.Languages;
import io.github.akjo03.discord.cscbot.services.ErrorMessageService;

import java.util.List;

public class CscCommandArgumentParseException extends CscException {
	public CscCommandArgumentParseException(
			String titleLabel,
			String descriptionLabel,
			List<String> titlePlaceholders,
			List<String> descriptionPlaceholders,
			Languages language,
			ErrorMessageService errorMessageService
	) {
		super(titleLabel, descriptionLabel, titlePlaceholders, descriptionPlaceholders, language, errorMessageService);
	}
}