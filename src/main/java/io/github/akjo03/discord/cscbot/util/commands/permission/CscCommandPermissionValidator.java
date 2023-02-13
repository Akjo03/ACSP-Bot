package io.github.akjo03.discord.cscbot.util.commands.permission;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.GuildMessageChannelUnion;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CscCommandPermissionValidator {
	private final List<CscCommandChannelPermissions> channelPermissions;

	public CscCommandPermissionValidator(List<CscCommandChannelPermissions> channelPermissions) {
		this.channelPermissions = channelPermissions;
	}

	public boolean validate(GuildMessageChannelUnion channel, Member member) {
		AtomicBoolean isAllowed = new AtomicBoolean(false);

		channelPermissions.stream()
				.filter(channelPermission -> channelPermission.getChannel().getId() == channel.getIdLong())
				.findFirst()
				.ifPresent(channelPermission -> {
					isAllowed.set(channelPermission.getAllowedRoles().stream()
							.anyMatch(role -> member.getRoles().stream()
									.anyMatch(memberRole -> memberRole.getIdLong() == role.getId())
							));
				});

		return isAllowed.get();
	}

	@Override
	public String toString() {
		return "ChannelPermissionsValidator{ " + channelPermissions + " }";
	}
}