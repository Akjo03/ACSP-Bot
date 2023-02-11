package io.github.akjo03.discord.cscbot.constants;

import lombok.Getter;

@Getter
public enum CscRoles {
	EVERYONE_ROLE(1073266273619816549L),
	BOTS_ROLE(1073678363237027915L),
	ADMIN_ROLE(1073273895395791009L);

	private final long id;

	CscRoles(long id) {
		this.id = id;
	}

	public static CscRoles getRoleById(long id) {
		for (CscRoles role : CscRoles.values()) {
			if (role.getId() == id) {
				return role;
			}
		}
		return null;
	}

	public static CscRoles getRoleByName(String name) {
		for (CscRoles role : CscRoles.values()) {
			if (role.name().equalsIgnoreCase(name)) {
				return role;
			}
		}
		return null;
	}
}