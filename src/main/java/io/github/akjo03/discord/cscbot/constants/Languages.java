package io.github.akjo03.discord.cscbot.constants;

import lombok.Getter;

import java.util.Arrays;
import java.util.Locale;

@Getter
public enum Languages {
	ENGLISH("en", Locale.ENGLISH),
	GERMAN("de", Locale.GERMAN);

	private final String code;
	private final Locale locale;

	Languages(String code, Locale locale) {
		this.code = code;
		this.locale = locale;
	}

	public static Languages fromString(String language) {
		return Arrays.stream(values())
				.filter(lang -> lang.getCode().equals(language))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Invalid language code: " + language));
	}
}