package io.github.akjo03.discord.cscbot.data.config.command;

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
public class CscBotSubcommands {
	@JsonSerialize
	@JsonDeserialize
	private boolean available;

	@JsonSerialize
	@JsonDeserialize
	private List<CscBotSubcommand> definitions;

	@JsonSerialize
	@JsonDeserialize
	private boolean required;

	@JsonCreator
	public CscBotSubcommands(
			@JsonProperty("available") boolean available,
			@JsonProperty("definitions") List<CscBotSubcommand> definitions,
			@JsonProperty("required") boolean required
	) {
		this.available = available;
		this.definitions = definitions;
		this.required = required;
	}

	public String toListString() {
		if (!available) {
			return "N/A";
		}

		StringBuilder sb = new StringBuilder();
		for (CscBotSubcommand subcommand : definitions) {
			sb.append("- `").append(subcommand.getName()).append("`");
		}
		return sb.toString();
	}
}