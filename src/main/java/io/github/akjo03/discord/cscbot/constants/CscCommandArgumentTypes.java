package io.github.akjo03.discord.cscbot.constants;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
public enum CscCommandArgumentTypes {
	INTEGER(BigInteger.class),
	DECIMAL(BigDecimal.class),
	STRING(String.class),
	BOOLEAN(Boolean.class),
	CHOICE(String.class),
	DATE(String.class),
	USER(String.class),
	CHANNEL(String.class);

	private final Class<?> type;

	CscCommandArgumentTypes(Class<?> type) {
		this.type = type;
	}

	public <T> T parse(String value, Class<T> type) {
		return switch (this) {
			case INTEGER -> type.cast(new BigInteger(value));
			case DECIMAL -> type.cast(new BigDecimal(value));
			case STRING, CHOICE, DATE, USER, CHANNEL -> type.cast(value);
			case BOOLEAN -> type.cast(Boolean.parseBoolean(value));
		};
	}

	public static CscCommandArgumentTypes getTypeByName(String name) {
		for (CscCommandArgumentTypes type : CscCommandArgumentTypes.values()) {
			if (type.name().equals(name)) {
				return type;
			}
		}
		return null;
	}
}