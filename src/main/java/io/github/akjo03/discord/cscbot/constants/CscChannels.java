package io.github.akjo03.discord.cscbot.constants;

import io.github.akjo03.discord.cscbot.CscBot;

import java.util.EnumSet;

public enum CscChannels {
	WELCOME_CHANNEL(1075515133494841375L, 1073275189338591233L, CscCategories.WELCOME_CATEGORY),
	RULES_CHANNEL(1075515133494841376L, 1073273398693724332L, CscCategories.WELCOME_CATEGORY),
	BEGIN_CHANNEL(1075515133494841378L, 1073346501314629732L, CscCategories.BEGIN_CATEGORY),
	ADMIN_CHANNEL(1075515133494841381L, 1073273398693724333L, CscCategories.ADMIN_CATEGORY);

	private final long localId;
	private final long prodId;
	private final CscCategories category;

	CscChannels(long localId, long prodId, CscCategories category) {
		this.localId = localId;
		this.prodId = prodId;
		this.category = category;
	}

	public long getId() {
		return switch (CscBot.getDeployMode()) {
			case LOCAL -> localId;
			case PROD -> prodId;
		};
	}

	public CscCategories getCategory() {
		return category;
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