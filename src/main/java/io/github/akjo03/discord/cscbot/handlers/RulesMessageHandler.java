package io.github.akjo03.discord.cscbot.handlers;

import io.github.akjo03.discord.cscbot.services.rules.RulesMessageService;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RulesMessageHandler extends ListenerAdapter {
	private final RulesMessageService rulesMessageService;

	@Override
	public void onReady(@NotNull ReadyEvent event) {
		rulesMessageService.updateRulesMessages();
	}
}