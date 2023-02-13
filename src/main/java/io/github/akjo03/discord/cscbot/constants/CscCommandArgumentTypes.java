package io.github.akjo03.discord.cscbot.constants;

public enum CscCommandArgumentTypes {
	INTEGER,
	DECIMAL,
	STRING,
	BOOLEAN,
	CHOICE;

	public static CscCommandArgumentTypes getArgumentTypeByName(String name) {
		for (CscCommandArgumentTypes type : CscCommandArgumentTypes.values()) {
			if (type.name().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return null;
	}
}