package io.github.akjo03.discord.cscbot.constants;

import lombok.Getter;

@Getter
public enum CscCategories {
	WELCOME_CATEGORY(1073274747468644373L),
	BEGIN_CATEGORY(1073346445958201375L),
	ADMIN_CATEGORY(1073273754110668931L);

	private final long id;

	CscCategories(long id) {
		this.id = id;
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