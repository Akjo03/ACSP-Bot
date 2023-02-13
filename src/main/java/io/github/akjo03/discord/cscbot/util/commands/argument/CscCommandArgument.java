package io.github.akjo03.discord.cscbot.util.commands.argument;

import io.github.akjo03.discord.cscbot.constants.CscCommandArgumentTypes;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public class CscCommandArgument<T> {
	private final String name;
	private final T value;

	public CscCommandArgument(String name, T value) {
		this.name = name;
		this.value = value;
	}

	@SuppressWarnings("unchecked")
	public static <T> @Nullable CscCommandArgument<T> of(String name, CscCommandArgumentTypes type, String value) {
		try {
			return new CscCommandArgument<>(name, (T) type.parse(value, type.getType()));
		} catch (Exception e) {
			return null;
		}
	}
}