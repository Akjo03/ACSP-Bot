package io.github.akjo03.discord.cscbot.constants;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public enum CscCommandArgumentTypes {
	STRING("STRING", "command.arguments.type.string"),
	INTEGER("INTEGER", "command.arguments.type.integer"),
	CHOICE("CHOICE", "command.arguments.type.choice");

	private final String type;
	private final String translationKey;

	CscCommandArgumentTypes(String type, String translationKey) {
		this.type = type;
		this.translationKey = translationKey;
	}

	public static @Nullable CscCommandArgumentTypes fromString(String type) {
		return Arrays.stream(CscCommandArgumentTypes.values())
				.filter(t -> t.getType().equals(type))
				.findFirst()
				.orElse(null);
	}

	public String getType() {
		return type;
	}

	public String getKey() {
		return translationKey;
	}

	public String getTooltipKey() {
		return translationKey + ".tooltip";
	}
}