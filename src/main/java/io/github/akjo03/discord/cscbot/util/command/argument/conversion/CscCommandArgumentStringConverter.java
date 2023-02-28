package io.github.akjo03.discord.cscbot.util.command.argument.conversion;

import java.util.function.Function;

public class CscCommandArgumentStringConverter extends CscCommandArgumentConverter<String> {
	protected CscCommandArgumentStringConverter() {
		super(Function.identity(), Function.identity());
	}
}