package io.github.akjo03.discord.cscbot.data.config.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;
import net.dv8tion.jda.api.utils.messages.MessageEditData;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@SuppressWarnings("unused")
public class CscBotConfigMessage {
	@JsonSerialize
	@JsonDeserialize
	private String content;

	@JsonSerialize
	@JsonDeserialize
	private List<CscBotConfigMessageEmbed> embeds;

	@JsonCreator
	public CscBotConfigMessage(
			@JsonProperty("content") String content,
			@JsonProperty("embeds") List<CscBotConfigMessageEmbed> embeds
	) {
		this.content = content;
		this.embeds = embeds;
	}

	public MessageCreateData toMessageCreateData() {
		MessageCreateBuilder messageBuilder = new MessageCreateBuilder();

		messageBuilder.setContent(content);
		messageBuilder.setEmbeds(embeds.stream().map(CscBotConfigMessageEmbed::toMessageEmbed).toList());

		return messageBuilder.build();
	}

	public MessageEditData toMessageEditData() {
		return MessageEditBuilder.fromCreateData(toMessageCreateData()).build();
	}

	public boolean equalsToMessage(Message message) {
		if (content != null && !content.equals(message.getContentRaw())) {
			return false;
		}

		if (embeds != null) {
			for (int i = 0; i < embeds.size(); i++) {
				if (!embeds.get(i).equalsToMessageEmbed(message.getEmbeds().get(i))) {
					return false;
				}
			}
		}

		return true;
	}

	public static CscBotConfigMessage copy(CscBotConfigMessage message) {
		return new CscBotConfigMessage(message.getContent(), message.getEmbeds());
	}
}