package io.github.akjo03.discord.cscbot.data.config.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@SuppressWarnings("unused")
public class CscBotConfigMessageEmbed {
	@JsonSerialize
	@JsonDeserialize
	private CscBotConfigMessageEmbedAuthor author;

	@JsonSerialize
	@JsonDeserialize
	private String title;

	@JsonSerialize
	@JsonDeserialize
	private String description;

	@JsonSerialize
	@JsonDeserialize
	private String url;

	@JsonSerialize
	@JsonDeserialize
	private String color;

	@JsonSerialize
	@JsonDeserialize
	private List<CscBotConfigMessageEmbedField> fields;

	@JsonSerialize
	@JsonDeserialize
	private String imageURL;

	@JsonSerialize
	@JsonDeserialize
	private String thumbnailURL;

	@JsonSerialize
	@JsonDeserialize
	private CscBotConfigMessageEmbedFooter footer;

	@JsonCreator
	public CscBotConfigMessageEmbed(
			@JsonProperty("author") CscBotConfigMessageEmbedAuthor author,
			@JsonProperty("title") String title,
			@JsonProperty("description") String description,
			@JsonProperty("url") String url,
			@JsonProperty("color") String color,
			@JsonProperty("fields") List<CscBotConfigMessageEmbedField> fields,
			@JsonProperty("image_url") String imageURL,
			@JsonProperty("thumbnail_url") String thumbnailURL,
			@JsonProperty("footer") CscBotConfigMessageEmbedFooter footer
	) {
		this.author = author;
		this.title = title;
		this.description = description;
		this.url = url;
		this.color = color;
		this.fields = fields;
		this.imageURL = imageURL;
		this.thumbnailURL = thumbnailURL;
		this.footer = footer;
	}

	public MessageEmbed toMessageEmbed() {
		EmbedBuilder embedBuilder = new EmbedBuilder()
				.setAuthor(author.getName(), author.getUrl(), author.getIconURL())
				.setTitle(title, url)
				.setDescription(description)
				.setColor(Color.decode(color))
				.setImage(imageURL)
				.setThumbnail(thumbnailURL)
				.setFooter(footer.getText(), footer.getIconURL())
				.setTimestamp(
						new DateTimeFormatterBuilder()
								.appendPattern("yyyy-MM-dd HH:mm")
								.parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
								.toFormatter().withZone(ZoneId.of("Europe/Zurich"))
								.parse(footer.getTimestamp(), Instant::from)
				);

		for (CscBotConfigMessageEmbedField field : fields) {
			embedBuilder.addField(field.getName(), field.getValue(), field.isInline());
		}

		return embedBuilder.build();
	}

	public boolean equalsToMessageEmbed(MessageEmbed messageEmbed) {
		if (author != null && !author.equalsToEmbedAuthor(messageEmbed.getAuthor())) {
			return false;
		}

		if (title != null && !title.equals(messageEmbed.getTitle())) {
			return false;
		}

		if (description != null && !description.equals(messageEmbed.getDescription())) {
			return false;
		}

		if (url != null && !url.equals(messageEmbed.getUrl())) {
			return false;
		}

		if (color != null && messageEmbed.getColor() != null && !Color.decode(color).equals(messageEmbed.getColor())) {
			return false;
		}

		if (fields != null) {
			for (int i = 0; i < fields.size(); i++) {
				if (!fields.get(i).equalsToEmbedField(messageEmbed.getFields().get(i))) {
					return false;
				}
			}
		}

		if (imageURL != null && messageEmbed.getImage() != null && !imageURL.equals(messageEmbed.getImage().getUrl())) {
			return false;
		}

		if (thumbnailURL != null && messageEmbed.getThumbnail() != null && !thumbnailURL.equals(messageEmbed.getThumbnail().getUrl())) {
			return false;
		}

		if (footer != null && !footer.equalsToEmbedFooter(messageEmbed.getFooter())) {
			return false;
		}

		if (footer != null && messageEmbed.getTimestamp() != null && !messageEmbed.getTimestamp().toInstant().equals(
				new DateTimeFormatterBuilder()
						.appendPattern("yyyy-MM-dd HH:mm")
						.parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
						.toFormatter().withZone(ZoneId.of("Europe/Zurich"))
						.parse(footer.getTimestamp(), Instant::from)
		)) {
			return false;
		}

		return true;
	}
}