package io.github.akjo03.discord.cscbot.util.commands.argument;

import lombok.Getter;

@Getter
public class CscCommandArgumentValidationError {
	private final String argumentName;
	private final String message;

	public CscCommandArgumentValidationError(String argumentName, String message) {
		this.argumentName = argumentName;
		this.message = message;
	}
}