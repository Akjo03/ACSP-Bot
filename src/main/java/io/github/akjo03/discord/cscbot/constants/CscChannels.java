package io.github.akjo03.discord.cscbot.constants;

import lombok.Getter;

import java.util.EnumSet;

@Getter
public enum CscChannels {
	WELCOME_CHANNEL(1073275189338591233L, CscCategories.WELCOME_CATEGORY),
	RULES_CHANNEL(1073273398693724332L, CscCategories.WELCOME_CATEGORY),
	BEGIN_CHANNEL(1073346501314629732L, CscCategories.BEGIN_CATEGORY),
	ADMIN_CHANNEL(1073273398693724333L, CscCategories.ADMIN_CATEGORY);

	private final long id;
	private final CscCategories category;

	CscChannels(long id, CscCategories category) {
		this.id = id;
		this.category = category;
	}

	public static CscChannels getChannelById(long id) {
		for (CscChannels channel : CscChannels.values()) {
			if (channel.getId() == id) {
				return channel;
			}
		}
		return null;
	}

	public static CscChannels getChannelByName(String name) {
		for (CscChannels channel : CscChannels.values()) {
			if (channel.name().equalsIgnoreCase(name)) {
				return channel;
			}
		}
		return null;
	}

	public static EnumSet<CscChannels> getChannelsByCategory(CscCategories category) {
		EnumSet<CscChannels> channels = EnumSet.noneOf(CscChannels.class);
		for (CscChannels channel : CscChannels.values()) {
			if (channel.getCategory() == category) {
				channels.add(channel);
			}
		}
		return channels;
	}
}