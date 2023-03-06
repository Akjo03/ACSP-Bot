package io.github.akjo03.discord.cscbot.constants;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@Getter
public enum CscComponentTypes {
	ACTION_ROW("ACTION_ROW"),
	INTERACTION_BUTTON("INTERACTION_BUTTON"),
	COMPONENT_TEMPLATE("CSC_COMPONENT"),
	ANY("ANY");

	private final String type;

	CscComponentTypes(String type) {
		this.type = type;
	}

	public static @Nullable CscComponentTypes fromString(String type) {
		return Arrays.stream(CscComponentTypes.values())
				.filter(typeP -> typeP.getType().equalsIgnoreCase(type))
				.findFirst()
				.orElse(null);
	}
}