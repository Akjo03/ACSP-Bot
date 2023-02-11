package io.github.akjo03.discord.cscbot.data.config.command.permission;

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
public class CscBotCommandPermission {
	@JsonSerialize
	@JsonDeserialize
	private List<String> channels;

	@JsonSerialize
	@JsonDeserialize
	private List<String> roles;

	@JsonCreator
	public CscBotCommandPermission(
			@JsonProperty("channels") List<String> channels,
			@JsonProperty("roles") List<String> roles
	) {
		this.channels = channels;
		this.roles = roles;
	}
}