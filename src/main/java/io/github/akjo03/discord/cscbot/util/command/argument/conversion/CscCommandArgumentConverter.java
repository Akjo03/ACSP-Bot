package io.github.akjo03.discord.cscbot.util.command.argument.conversion;

import io.github.akjo03.lib.converter.Converter;

import java.util.function.Function;

public abstract class CscCommandArgumentConverter<T> extends Converter<String, T> {
	protected CscCommandArgumentConverter(Function<String, T> forward, Function<T, String> backward) {
		super(forward, backward);
	}
}
