package io.github.akjo03.discord.cscbot.constants;

public enum CscDeployMode {
	LOCAL,
	PROD;

	public static CscDeployMode getDeployMode(String deployMode) {
		if (deployMode == null || deployMode.isEmpty()) {
			return LOCAL;
		}

		return switch (deployMode) {
			case "LOCAL" -> LOCAL;
			case "PROD" -> PROD;
			default -> LOCAL;
		};
	}
}