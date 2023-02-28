package io.github.akjo03.discord.cscbot.util.command.argument;

import io.github.akjo03.discord.cscbot.constants.CscCommandArgumentTypes;
import io.github.akjo03.discord.cscbot.data.config.command.argument.data.CscBotCommandArgumentData;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class CscCommandArgument<T> {
	private final String name;
	private final String description;
	private final T value;
	private final CscBotCommandArgumentData<T> data;
	private final CscCommandArgumentTypes type;

	private CscCommandArgument(String name, String description, T value, CscBotCommandArgumentData<T> data, CscCommandArgumentTypes type) {
		this.name = name;
		this.description = description;
		this.value = value;
		this.data = data;
		this.type = type;
	}

	public static <T> @NotNull CscCommandArgument<T> of(String name, String description, T value, CscBotCommandArgumentData<T> data, CscCommandArgumentTypes type) {
		return new CscCommandArgument<>(name, description, value, data, type);
	}
}