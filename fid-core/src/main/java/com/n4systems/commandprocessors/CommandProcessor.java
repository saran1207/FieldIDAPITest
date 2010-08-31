package com.n4systems.commandprocessors;


import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.messages.MessageCommand;
import com.n4systems.model.messages.MessageCommandSaver;
import com.n4systems.model.user.User;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.loaders.NonSecureLoaderFactory;

public abstract class CommandProcessor<T extends MessageCommand> {

	private final Class<T> clazz;
	
	protected T command;

	protected User actor;
	protected NonSecureLoaderFactory nonSecureLoaderFactory;
	protected Transaction transaction;

	public CommandProcessor(Class<T> clazz) {
		super();
		this.clazz = clazz;
	}
	
	public void process(MessageCommand command, Transaction transaction) {
		this.transaction = transaction;
		execute(convertCommand(command));
		markCommandProcessed(command);
	}
	
	protected abstract void execute(T command);
	
	protected abstract boolean isCommandStillValid(T command);

	@SuppressWarnings("unchecked")
	private T convertCommand(MessageCommand command) {
		if (command.getClass() != clazz) {
			throw new InvalidArgumentException("this requires a " + clazz.getName());
		}
		
		this.command = (T)command;
		
		return this.command;
	}

	
	private void markCommandProcessed(MessageCommand command) {
		command.setProcessed(true);
		new MessageCommandSaver().update(transaction, command);
	}

	
	public CommandProcessor<T> setActor(User actor) {
		this.actor = actor;
		return this;
	}

	public CommandProcessor<T> setNonSecureLoaderFactory(NonSecureLoaderFactory nonSecureLoaderFactory) {
		this.nonSecureLoaderFactory = nonSecureLoaderFactory;
		return this;
	}

	
}
