package io.github.akjo03.discord.cscbot.constants;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@Getter
public enum CscCommandArgumentTypes {
	STRING("STRING"),
	INTEGER("INTEGER"),
	CHOICE("CHOICE");

	private final String type;

	CscCommandArgumentTypes(String type) {
		this.type = type;
	}

	public static @Nullable CscCommandArgumentTypes fromString(String type) {
		return Arrays.stream(CscCommandArgumentTypes.values())
				.filter(t -> t.getType().equals(type))
				.findFirst()
				.orElse(null);
	}
}