package io.github.akjo03.discord.cscbot.util.command.argument;

import lombok.Getter;

import java.util.List;

@Getter
public class CscCommandArguments {
	private final List<CscCommandArgument<?>> arguments;
	private final String subcommand;
	private final List<CscCommandArgument<?>> subcommandArguments;

	private CscCommandArguments(List<CscCommandArgument<?>> arguments) {
		this.arguments = arguments;
		this.subcommand = null;
		this.subcommandArguments = null;
	}

	private CscCommandArguments(List<CscCommandArgument<?>> arguments, String subcommand, List<CscCommandArgument<?>> subcommandArguments) {
		this.arguments = arguments;
		this.subcommand = subcommand;
		this.subcommandArguments = subcommandArguments;
	}

	public static CscCommandArguments of(List<CscCommandArgument<?>> arguments) {
		return new CscCommandArguments(arguments);
	}

	public static CscCommandArguments of(List<CscCommandArgument<?>> arguments, String subcommand, List<CscCommandArgument<?>> subcommandArguments) {
		return new CscCommandArguments(arguments, subcommand, subcommandArguments);
	}
}