package io.github.akjo03.discord.cscbot.util.command.argument.conversion;

public class CscCommandArgumentIntegerConverter extends CscCommandArgumentConverter<Integer> {
	protected CscCommandArgumentIntegerConverter() {
		super(Integer::parseInt, Object::toString);
	}
}