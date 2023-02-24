package io.github.akjo03.discord.cscbot.services;

import io.github.akjo03.discord.cscbot.config.LocaleConfiguration;
import io.github.akjo03.discord.cscbot.constants.Languages;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StringsResourceService {
	private final StringPlaceholderService stringPlaceholderService;
	private final ResourceBundleMessageSource resourceBundleMessageSource;
	private final LocaleConfiguration localeConfiguration;

	public String getString(String label, Optional<Languages> language, String... placeholders) {
		return stringPlaceholderService.replacePlaceholders(
				resourceBundleMessageSource.getMessage(label, null, getLocale(language)),
				placeholders
		);
	}

	public Locale getLocale(Optional<Languages> language) {
		return language.orElse(Languages.fromString(localeConfiguration.getDefaultLocale())).getLocale();
	}
}