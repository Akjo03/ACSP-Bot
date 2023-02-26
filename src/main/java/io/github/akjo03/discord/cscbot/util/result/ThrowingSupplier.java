package io.github.akjo03.discord.cscbot.util.result;

@FunctionalInterface
public interface ThrowingSupplier<S> {
	S get() throws Exception;
}