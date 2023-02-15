package io.github.akjo03.discord.cscbot.constants;

import io.github.akjo03.discord.cscbot.CscBot;

public enum CscRoles {
	EVERYONE_ROLE(1075515131288625182L, 1073266273619816549L),
	BOTS_ROLE(1075515131288625183L, 1073678363237027915L),
	ADMIN_ROLE(1075515131288625184L, 1073273895395791009L);

	private final long localId;
	private final long prodId;

	CscRoles(long localId, long prodId) {
		this.localId = localId;
		this.prodId = prodId;
	}

	public long getId() {
		return CscBot.getDeployMode() == CscDeployMode.LOCAL ? localId : prodId;
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