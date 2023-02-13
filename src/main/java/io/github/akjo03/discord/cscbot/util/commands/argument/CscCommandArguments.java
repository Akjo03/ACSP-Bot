package io.github.akjo03.discord.cscbot.util.commands.argument;

import lombok.Getter;

import javax.annotation.Nullable;
import java.util.List;

@Getter
public class CscCommandArguments {
	@Nullable private final String subcommand;
	private final List<CscCommandArgument<?>> subcommandArgs;
	private final List<CscCommandArgument<?>> args;

	public CscCommandArguments(
			@Nullable String subcommand,
			List<CscCommandArgument<?>> subcommandArgs,
			List<CscCommandArgument<?>> args
	) {
		this.subcommand = subcommand;
		this.subcommandArgs = subcommandArgs;
		this.args = args;
	}

	public CscCommandArguments(List<CscCommandArgument<?>> args) {
		this(null, List.of(), args);
	}
}