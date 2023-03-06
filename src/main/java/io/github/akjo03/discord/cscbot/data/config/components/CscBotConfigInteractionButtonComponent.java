package io.github.akjo03.discord.cscbot.data.config.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.akjo03.discord.cscbot.constants.CscInteractionButtonComponentStyles;
import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.discord.cscbot.services.StringsResourceService;
import lombok.*;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class CscBotConfigInteractionButtonComponent extends CscBotConfigComponent {
	@JsonSerialize
	@JsonDeserialize
	private String interactionId;

	@JsonSerialize
	@JsonDeserialize
	private String style;

	@JsonSerialize
	@JsonDeserialize
	private String text;

	@JsonSerialize
	@JsonDeserialize
	private CscBotConfigButtonComponentEmoji emoji;

	@JsonSerialize
	@JsonDeserialize
	private boolean disabled;

	@JsonCreator
	public CscBotConfigInteractionButtonComponent(
			@JsonProperty("interaction_id") String interactionId,
			@JsonProperty("style") String style,
			@JsonProperty("text") String text,
			@JsonProperty("emoji") CscBotConfigButtonComponentEmoji emoji,
			@JsonProperty("disabled") boolean disabled
	) {
		this.interactionId = interactionId;
		this.style = style;
		this.text = text;
		this.emoji = emoji;
		this.disabled = disabled;
	}

	@Override
	public ItemComponent toComponent(StringsResourceService stringsResourceService, BotConfigService botConfigService) {
		CscInteractionButtonComponentStyles buttonStyle = CscInteractionButtonComponentStyles.fromString(style);
		if (buttonStyle == null) {
			buttonStyle = CscInteractionButtonComponentStyles.SECONDARY;
		}
		ButtonStyle style = buttonStyle.getStyle();

		String textStr = stringsResourceService.getString(
				StringUtils.stripStart(text, "$"),
				Optional.empty()
		);

		Button button = emoji != null
				? Button.of(style, interactionId, textStr, emoji.toEmoji())
				: Button.of(style, interactionId, textStr);

		return button.withDisabled(disabled);
	}
}