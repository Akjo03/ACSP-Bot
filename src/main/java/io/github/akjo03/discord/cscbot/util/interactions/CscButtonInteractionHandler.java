package io.github.akjo03.discord.cscbot.util.interactions;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class CscButtonInteractionHandler extends ListenerAdapter {
	protected final List<String> interactionIds;

	protected CscButtonInteractionHandler(List<String> interactionIds) {
		this.interactionIds = interactionIds;
	}

	protected abstract void onExecute(@NotNull ButtonInteractionEvent event);

	@Override
	public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
		if (event.getUser().isBot()) {
			return;
		}
		if (interactionIds.contains(event.getComponentId())) {
			onExecute(event);
		}
	}
}