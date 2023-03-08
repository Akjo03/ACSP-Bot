package io.github.akjo03.discord.cscbot.data.config.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.discord.cscbot.services.StringsResourceService;
import lombok.*;
import net.dv8tion.jda.api.interactions.components.ItemComponent;

import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class CscBotConfigActionRowComponent extends CscBotConfigComponent {
	@JsonSerialize
	@JsonDeserialize
	private List<CscBotConfigComponent> components;

	@JsonCreator
	public CscBotConfigActionRowComponent(
			@JsonProperty("components") List<CscBotConfigComponent> components
	) {
		this.components = components;
	}

	@Override
	public ItemComponent toComponent(StringsResourceService stringsResourceService, BotConfigService botConfigService) {
		return null;
	}

	public List<ItemComponent> toActionRow(StringsResourceService stringsResourceService, BotConfigService botConfigService) {
		return components.stream()
				.map(component -> component.toComponent(stringsResourceService, botConfigService))
				.filter(Objects::nonNull)
				.toList();
	}
}