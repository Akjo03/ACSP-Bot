package io.github.akjo03.discord.cscbot.handlers;

import io.github.akjo03.discord.cscbot.services.welcome.WelcomeMessageService;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class WelcomeMessageHandler extends ListenerAdapter {
	private final WelcomeMessageService welcomeMessageService;

	@Override
	public void onReady(@NotNull ReadyEvent event) {
		welcomeMessageService.updateWelcomeMessages(Optional.empty());
	}
}
