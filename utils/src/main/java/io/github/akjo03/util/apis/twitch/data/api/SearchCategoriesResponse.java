package io.github.akjo03.util.apis.twitch.data.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.net.URI;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchCategoriesResponse extends TwitchApiResponse<SearchCategoriesResponse> {
	private final String id;
	private final String name;
	private final URI boxArtURL;

	public SearchCategoriesResponse(
			@JsonProperty("id")
					String id,
			@JsonProperty("name")
					String name,
			@JsonProperty("box_art_url")
					String boxArtURL
	) {
		this.id = id;
		this.name = name;
		this.boxArtURL = URI.create(boxArtURL.replace("{width}", "600").replace("{height}", "800"));
	}

	@Override
	public String toString() {
		return "SearchCategoriesResponse{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", boxArtURL=" + boxArtURL + '}';
	}
}