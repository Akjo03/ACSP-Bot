package io.github.akjo03.discord.cscbot.util.interactions;

import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;

public interface CscInteractionHandler<T extends GenericInteractionCreateEvent> {
	void onExecute(T event);
}