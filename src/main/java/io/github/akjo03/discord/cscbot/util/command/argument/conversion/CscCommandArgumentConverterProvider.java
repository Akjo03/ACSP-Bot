package io.github.akjo03.discord.cscbot.util.command.argument.conversion;

@FunctionalInterface
@SuppressWarnings("unused")
public interface CscCommandArgumentConverterProvider<T, C extends CscCommandArgumentConverter<T>> {
	CscCommandArgumentConverterProvider<String, CscCommandArgumentStringConverter> STRING = CscCommandArgumentStringConverter::new;
	CscCommandArgumentConverterProvider<Integer, CscCommandArgumentIntegerConverter> INTEGER = CscCommandArgumentIntegerConverter::new;

	C provide();
}