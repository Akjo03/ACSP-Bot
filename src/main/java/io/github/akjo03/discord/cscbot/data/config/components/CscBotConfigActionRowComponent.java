package io.github.akjo03.discord.cscbot.data.config.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@SuppressWarnings("unused")
public class CscBotConfigActionRowComponent implements CscBotConfigComponent {
	@JsonSerialize
	@JsonDeserialize
	private List<CscBotConfigComponent> components;

	@JsonCreator
	public CscBotConfigActionRowComponent(
			@JsonProperty("components") List<CscBotConfigComponent> components
	) {
		this.components = components;
	}
}