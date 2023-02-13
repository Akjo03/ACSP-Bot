package io.github.akjo03.discord.cscbot.util.commands.permission;

import io.github.akjo03.discord.cscbot.constants.CscChannels;
import io.github.akjo03.discord.cscbot.constants.CscRoles;
import lombok.Getter;

import java.util.EnumSet;

@Getter
public class CscCommandChannelPermissions {
	private final CscChannels channel;
	private final EnumSet<CscRoles> allowedRoles;

	public CscCommandChannelPermissions(CscChannels channel, EnumSet<CscRoles> allowedRoles) {
		this.channel = channel;
		this.allowedRoles = allowedRoles;
	}

	@Override
	public String toString() {
		return "ChannelPermissions(" + channel + "): " + allowedRoles;
	}
}