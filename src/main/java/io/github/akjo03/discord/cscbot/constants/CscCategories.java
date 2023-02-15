package io.github.akjo03.discord.cscbot.constants;

import io.github.akjo03.discord.cscbot.CscBot;

public enum CscCategories {
	WELCOME_CATEGORY(1075515133494841374L, 1073274747468644373L),
	BEGIN_CATEGORY(1075515133494841377L, 1073346445958201375L),
	ADMIN_CATEGORY(1075515133494841380L, 1073273754110668931L);

	private final long localId;
	private final long prodId;

	CscCategories(long localId, long prodId) {
		this.localId = localId;
		this.prodId = prodId;
	}

	public long getId() {
		return switch (CscBot.getDeployMode()) {
			case LOCAL -> localId;
			case PROD -> prodId;
		};
	}

	public static CscCategories getCategoryById(long id) {
		for (CscCategories category : CscCategories.values()) {
			if (category.getId() == id) {
				return category;
			}
		}
		return null;
	}

	public static CscCategories getCategoryByName(String name) {
		for (CscCategories category : CscCategories.values()) {
			if (category.name().equals(name)) {
				return category;
			}
		}
		return null;
	}
}