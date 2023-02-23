package io.github.akjo03.discord.cscbot.constants;

import io.github.akjo03.discord.cscbot.data.config.command.argument.data.CscBotCommandArgumentData;
import io.github.akjo03.discord.cscbot.data.config.command.argument.data.CscBotCommandArgumentIntegerData;
import io.github.akjo03.discord.cscbot.data.config.command.argument.data.CscBotCommandArgumentStringData;
import io.github.akjo03.discord.cscbot.data.config.command.argument.data.choice.CscBotCommandArgumentChoiceData;
import lombok.Getter;

import java.math.BigInteger;

@Getter
public enum CscCommandArgumentTypes {
	INTEGER(BigInteger.class, CscBotCommandArgumentIntegerData.class, "command.arguments.type.integer", "command.arguments.type.integer.tooltip"),
	STRING(String.class, CscBotCommandArgumentStringData.class, "command.arguments.type.string", "command.arguments.type.string.tooltip"),
	CHOICE(String.class, CscBotCommandArgumentChoiceData.class, "command.arguments.type.choice", "command.arguments.type.choice.tooltip");

	private final Class<?> type;
	private final Class<? extends CscBotCommandArgumentData> dataClass;
	private final String nameLabel;
	private final String tooltipLabel;

	CscCommandArgumentTypes(Class<?> type, Class<? extends CscBotCommandArgumentData> dataClass, String nameLabel, String tooltipLabel) {
		this.type = type;
		this.dataClass = dataClass;
		this.nameLabel = nameLabel;
		this.tooltipLabel = tooltipLabel;
	}

	public <T> T parse(String value, Class<T> type) {
		return switch (this) {
			case INTEGER -> type.cast(new BigInteger(value));
			case STRING, CHOICE -> type.cast(value);
		};
	}

	public static CscCommandArgumentTypes getTypeByName(String name) {
		for (CscCommandArgumentTypes type : CscCommandArgumentTypes.values()) {
			if (type.name().equals(name)) {
				return type;
			}
		}
		return null;
	}
}