package gateways;

import java.util.List;

import spigot.Command;

public interface CommandGateway {

	Command findCommandByName(String name);
	
	List<Command> findAllCommands();
	
	void registerCommand(Command command);
	
	boolean containsCommandWithName(String name);
	
}