package io.github.akjo03.discord.cscbot.services.help;

import io.github.akjo03.discord.cscbot.services.BotConfigService;
import io.github.akjo03.discord.cscbot.services.StringsResourceService;
import io.github.akjo03.lib.logging.EnableLogger;
import io.github.akjo03.lib.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@EnableLogger
public class CommandHelpService {
	private Logger logger;

	private final BotConfigService botConfigService;
	private final StringsResourceService stringsResourceService;
}