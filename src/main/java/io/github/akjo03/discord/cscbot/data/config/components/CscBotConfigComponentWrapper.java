package io.github.akjo03.discord.cscbot.data.config.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuppressWarnings("unused")
public class CscBotConfigComponentWrapper {
	@JsonSerialize
	@JsonDeserialize
	@EqualsAndHashCode.Include
	private String label;

	@JsonSerialize
	@JsonDeserialize
	private CscBotConfigComponent component;

	@JsonCreator
	public CscBotConfigComponentWrapper(
			@JsonProperty("label") String label,
			@JsonProperty("component") CscBotConfigComponent component
	) {
		this.label = label;
		this.component = component;
	}
}