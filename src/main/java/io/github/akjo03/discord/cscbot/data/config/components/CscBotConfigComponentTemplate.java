package io.github.akjo03.discord.cscbot.data.config.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.akjo03.discord.cscbot.constants.CscComponentTypes;
import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.discord.cscbot.services.StringsResourceService;
import lombok.*;
import net.dv8tion.jda.api.interactions.components.ItemComponent;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class CscBotConfigComponentTemplate extends CscBotConfigComponent {
	@JsonSerialize
	@JsonDeserialize
	private String componentLabel;

	@JsonCreator
	public CscBotConfigComponentTemplate(
			@JsonProperty("component") String component
	) {
		this.componentLabel = component;
	}

	@Override
	public ItemComponent toComponent(StringsResourceService stringsResourceService, BotConfigService botConfigService) {
		CscBotConfigComponent component = botConfigService.getComponent(componentLabel, CscComponentTypes.ANY);
		if (component == null) {
			return null;
		} else {
			return component.toComponent(stringsResourceService, botConfigService);
		}
	}
}