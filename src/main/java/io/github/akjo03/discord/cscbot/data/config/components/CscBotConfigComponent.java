package io.github.akjo03.discord.cscbot.data.config.components;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.discord.cscbot.services.StringsResourceService;
import lombok.*;
import net.dv8tion.jda.api.interactions.components.ItemComponent;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
		@JsonSubTypes.Type(value = CscBotConfigActionRowComponent.class, name = "ACTION_ROW"),
		@JsonSubTypes.Type(value = CscBotConfigInteractionButtonComponent.class, name = "INTERACTION_BUTTON"),
		@JsonSubTypes.Type(value = CscBotConfigComponentTemplate.class, name = "CSC_COMPONENT")
})
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@SuppressWarnings("unused")
public abstract class CscBotConfigComponent {
	@JsonSerialize
	@JsonDeserialize
	private String type;

	@JsonCreator
	protected CscBotConfigComponent(
			@JsonProperty("type") String type
	) {
		this.type = type;
	}

	public abstract ItemComponent toComponent(StringsResourceService stringsResourceService, BotConfigService botConfigService);
}