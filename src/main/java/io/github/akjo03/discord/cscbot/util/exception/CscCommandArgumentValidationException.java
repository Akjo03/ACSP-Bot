package io.github.akjo03.discord.cscbot.util.exception;

import io.github.akjo03.discord.cscbot.constants.Languages;
import io.github.akjo03.discord.cscbot.services.ErrorMessageService;

import java.util.List;

// TODO: Add argument validation exceptions
public class CscCommandArgumentValidationException extends CscException {
	public CscCommandArgumentValidationException(String titleLabel, String descriptionLabel, List<String> titlePlaceholders, List<String> descriptionPlaceholders, Languages language, ErrorMessageService errorMessageService) {
		super(titleLabel, descriptionLabel, titlePlaceholders, descriptionPlaceholders, language, errorMessageService);
	}
}