package io.github.akjo03.discord.cscbot.constants;

import lombok.Getter;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@Getter
public enum CscInteractionButtonComponentStyles {
	PRIMARY(ButtonStyle.PRIMARY),
	SECONDARY(ButtonStyle.SECONDARY),
	SUCCESS(ButtonStyle.SUCCESS),
	DANGER(ButtonStyle.DANGER);

	private final ButtonStyle style;

	CscInteractionButtonComponentStyles(ButtonStyle style) {
		this.style = style;
	}

	public static @Nullable CscInteractionButtonComponentStyles fromString(String style) {
		return Arrays.stream(values())
				.filter(s -> s.name().equals(style))
				.findFirst()
				.orElse(null);
	}
}