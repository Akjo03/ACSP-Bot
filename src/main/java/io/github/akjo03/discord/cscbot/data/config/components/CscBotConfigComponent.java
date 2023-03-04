package io.github.akjo03.discord.cscbot.data.config.components;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
		@JsonSubTypes.Type(value = CscBotConfigActionRowComponent.class, name = "ACTION_ROW"),
		@JsonSubTypes.Type(value = CscBotConfigInteractionButtonComponent.class, name = "INTERACTION_BUTTON")
})
@SuppressWarnings("unused")
public interface CscBotConfigComponent {}