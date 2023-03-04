package io.github.akjo03.discord.cscbot.util.interactions;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public abstract class CscButtonInteractionListener extends ListenerAdapter {
	protected final String interactionId;

	protected CscButtonInteractionListener(String interactionId) {
		this.interactionId = interactionId;
	}

	protected abstract void onExecute(@NotNull ButtonInteractionEvent event);

	@Override
	public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
		if (event.getUser().isBot()) {
			return;
		}
		if (event.getComponentId().equals(interactionId)) {
			onExecute(event);
		}
	}
}