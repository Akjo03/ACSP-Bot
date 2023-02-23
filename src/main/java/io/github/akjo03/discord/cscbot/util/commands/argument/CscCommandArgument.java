package io.github.akjo03.discord.cscbot.util.commands.argument;

import io.github.akjo03.discord.cscbot.constants.CscCommandArgumentTypes;
import io.github.akjo03.discord.cscbot.data.config.command.argument.data.CscBotCommandArgumentData;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public class CscCommandArgument<T, D extends CscBotCommandArgumentData<T>> {
	private final String name;
	private final T value;
	private final D data;

	public CscCommandArgument(String name, T value, D data) {
		this.name = name;
		this.value = value;
		this.data = data;
	}

	@SuppressWarnings("unchecked")
	public static <T, D extends CscBotCommandArgumentData<T>> @Nullable CscCommandArgument<T, D> of(String name, CscCommandArgumentTypes type, String value) {
		if (value == null) {
			return new CscCommandArgument<>(name, null, null);
		}
		try {
			return new CscCommandArgument<>(name, (T) type.parse(value, type.getType()), null);
		} catch (Exception e) {
			return null;
		}
	}

	public static <T, D extends CscBotCommandArgumentData<T>> @Nullable CscCommandArgument<T, D> of(String name, T value) {
		if (value == null) {
			return new CscCommandArgument<>(name, null, null);
		}
		try {
			return new CscCommandArgument<>(name, value, null);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String toString() {
		return "CscCommandArgument[" + name + "](" + value + ")";
	}
}