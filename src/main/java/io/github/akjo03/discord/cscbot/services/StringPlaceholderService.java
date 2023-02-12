package io.github.akjo03.discord.cscbot.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StringPlaceholderService {
	public String replacePlaceholders(String str, String[] placeholders) {
		for (int i = 0; i < placeholders.length; i++) {
			if (str == null) {
				return null;
			}
			str = str.replace("€" + i, placeholders[i]);
		}
		return str;
	}
}