package io.github.akjo03.discord.cscbot.util.commands.argument;

import lombok.Getter;

import javax.annotation.Nullable;
import java.util.List;

@Getter
public class CscCommandArguments {
	@Nullable private final String subcommand;
	private final List<String> subcommandArgs;
	private final List<String> args;

	public CscCommandArguments(
			@Nullable String subcommand,
			List<String> subcommandArgs,
			List<String> args
	) {
		this.subcommand = subcommand;
		this.subcommandArgs = subcommandArgs;
		this.args = args;
	}

	public CscCommandArguments(List<String> args) {
		this(null, List.of(), args);
	}
}