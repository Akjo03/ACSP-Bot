package io.github.akjo03.discord.cscbot.constants;

public enum CscDeployMode {
	LOCAL,
	PROD;

	public static CscDeployMode getDeployMode(String deployMode) {
		if (deployMode == null || deployMode.isEmpty()) {
			return LOCAL;
		}

		switch (deployMode) {
			case "LOCAL":
				return LOCAL;
			case "PROD":
				return PROD;
			default:
				return LOCAL;
		}
	}
}