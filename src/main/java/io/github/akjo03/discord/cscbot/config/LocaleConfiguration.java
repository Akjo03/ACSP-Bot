package io.github.akjo03.discord.cscbot.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

@Configuration
@Component
@Getter
public class LocaleConfiguration {
	@Value("${application.translation.baseName}")
	private String baseName;

	@Value("${application.translation.defaultLocale}")
	private String defaultLocale;

	@Bean(name = "stringsResourceBundleMessageSource")
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource rs = new ResourceBundleMessageSource();
		rs.setBasename(baseName);
		rs.setDefaultEncoding("UTF-8");
		rs.setUseCodeAsDefaultMessage(true);
		return rs;
	}
}