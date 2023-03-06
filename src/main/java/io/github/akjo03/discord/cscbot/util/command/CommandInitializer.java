package io.github.akjo03.discord.cscbot.util.command;

import io.github.akjo03.discord.cscbot.util.interactions.CscInteractionHandler;
import net.dv8tion.jda.api.JDA;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;

public class CommandInitializer {
	private final ApplicationContext applicationContext;
	private final JDA jdaInstance;

	private CommandInitializer(ApplicationContext applicationContext, JDA jdaInstance) {
		this.applicationContext = applicationContext;
		this.jdaInstance = jdaInstance;
	}

	@Contract(value = "_, _ -> new", pure = true)
	public static @NotNull CommandInitializer of(ApplicationContext applicationContext, JDA jdaInstance) {
		return new CommandInitializer(applicationContext, jdaInstance);
	}

	public void registerInteractionHandler(@NotNull Class<? extends CscInteractionHandler<?>> handlerClass) {
		jdaInstance.addEventListener(applicationContext.getBean(handlerClass));
	}
}