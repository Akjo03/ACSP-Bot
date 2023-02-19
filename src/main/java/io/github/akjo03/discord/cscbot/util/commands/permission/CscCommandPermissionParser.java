package io.github.akjo03.discord.cscbot.util.commands.permission;

import io.github.akjo03.discord.cscbot.constants.CscCategories;
import io.github.akjo03.discord.cscbot.constants.CscChannels;
import io.github.akjo03.discord.cscbot.constants.CscRoles;
import io.github.akjo03.discord.cscbot.data.config.command.permission.CscBotCommandPermission;
import io.github.akjo03.lib.logging.Logger;
import io.github.akjo03.lib.logging.LoggerManager;

import java.util.*;

public class CscCommandPermissionParser {
	private static final Logger LOGGER = LoggerManager.getLogger(CscCommandPermissionParser.class);

	private final String commandName;
	private final List<CscBotCommandPermission> permissions;

	public CscCommandPermissionParser(String commandName, List<CscBotCommandPermission> permissions) {
		this.commandName = commandName;
		this.permissions = permissions;
	}

	public CscCommandPermissionValidator parse() {
		List<CscCommandChannelPermissions> channelPermissions = new ArrayList<>();

		// Create a map from roles to channels first
		Map<CscRoles, EnumSet<CscChannels>> roleChannelMap = new EnumMap<>(CscRoles.class);

		for (CscBotCommandPermission permission : permissions) {
			List<String> channelDefinitions = permission.getChannels();
			List<String> roleDefinitions = permission.getRoles();

			if (channelDefinitions == null || roleDefinitions == null) {
				continue;
			}

			// Get all roles from the role definitions
			EnumSet<CscRoles> roles = EnumSet.noneOf(CscRoles.class);
			for (String roleDefinition : roleDefinitions) {
				List<String> exclusionRoleDefinitions = new ArrayList<>();
				List<String> simpleRoleDefinitions = new ArrayList<>();

				if (roleDefinition.startsWith("-")) {
					exclusionRoleDefinitions.add(roleDefinition.substring(1));
				} else {
					simpleRoleDefinitions.add(roleDefinition);
				}

				for (String exclusionRoleDefinition : exclusionRoleDefinitions) {
					// If the exclusion definition is *, remove all roles
					if (exclusionRoleDefinition.equals("*")) {
						roles.clear();
						continue;
					}
					String roleName = exclusionRoleDefinition.toUpperCase();
					CscRoles role = CscRoles.getRoleByName(roleName);
					if (role == null) {
						LOGGER.warn("While parsing command permissions for command \"" + commandName + "\", role was not found: " + roleName);
						continue;
					}
					roles.remove(role);
				}

				for (String simpleRoleDefinition : simpleRoleDefinitions) {
					// If the role definition is *, add all roles
					if (simpleRoleDefinition.equals("*")) {
						roles.addAll(EnumSet.allOf(CscRoles.class));
						continue;
					}
					String roleName = simpleRoleDefinition.toUpperCase();
					CscRoles role = CscRoles.getRoleByName(roleName);
					if (role == null) {
						LOGGER.warn("While parsing command permissions for command \"" + commandName + "\", role was not found: " + roleName);
						continue;
					}
					roles.add(role);
				}
			}

			// For every role, add the channels to the map
			for (CscRoles role : roles) {
				boolean roleExists = roleChannelMap.containsKey(role);

				EnumSet<CscChannels> channels = roleExists ? roleChannelMap.get(role) : EnumSet.noneOf(CscChannels.class);

				// Parse the channel definitions into different types depending on the prefix
				List<String> categoryChannelDefinitions = new ArrayList<>();
				List<String> exclusionChannelDefinitions = new ArrayList<>();
				List<String> simpleChannelDefinitions = new ArrayList<>();

				for (String channelDefinition : channelDefinitions) {
					if (channelDefinition.startsWith(">")) {
						categoryChannelDefinitions.add(channelDefinition.substring(1));
					} else if (channelDefinition.startsWith("-")) {
						exclusionChannelDefinitions.add(channelDefinition.substring(1));
					} else {
						simpleChannelDefinitions.add(channelDefinition);
					}
				}

				// For every category definition, add all channels in that category
				for (String categoryDefinition : categoryChannelDefinitions) {
					// If the category definition is *, add all channels
					if (categoryDefinition.equals("*")) {
						channels.addAll(EnumSet.allOf(CscChannels.class));
						continue;
					}
					String categoryName = categoryDefinition.toUpperCase();
					CscCategories category = CscCategories.getCategoryByName(categoryName);
					if (category == null) {
						LOGGER.warn("While parsing command permissions for command \"" + commandName + "\", category was not found: " + categoryName);
						continue;
					}
					EnumSet<CscChannels> categoryChannels = CscChannels.getChannelsByCategory(category);
					channels.addAll(categoryChannels);
				}

				// For every exclusion definition, remove the channel from the list
				for (String exclusionDefinition : exclusionChannelDefinitions) {
					// If the exclusion definition is *, remove all channels
					if (exclusionDefinition.equals("*")) {
						channels.clear();
						continue;
					}
					String channelName = exclusionDefinition.toUpperCase();
					CscChannels channel = CscChannels.getChannelByName(channelName);
					if (channel == null) {
						LOGGER.warn("While parsing command permissions for command \"" + commandName + "\", channel was not found: " + channelName);
						continue;
					}
					channels.remove(channel);
				}

				// For every simple definition, add the channel to the list
				for (String simpleDefinition : simpleChannelDefinitions) {
					// If the simple definition is *, add all channels
					if (simpleDefinition.equals("*")) {
						channels.addAll(EnumSet.allOf(CscChannels.class));
						continue;
					}
					String channelName = simpleDefinition.toUpperCase();
					CscChannels channel = CscChannels.getChannelByName(channelName);
					if (channel == null) {
						LOGGER.warn("While parsing command permissions for command \"" + commandName + "\", channel was not found: " + channelName);
						continue;
					}
					channels.add(channel);
				}

				roleChannelMap.put(role, channels);
			}
		}

		// Now reverse the map to create a map from channels to roles
		Map<CscChannels, EnumSet<CscRoles>> channelRoleMap = new EnumMap<>(CscChannels.class);

		for (Map.Entry<CscRoles, EnumSet<CscChannels>> entry : roleChannelMap.entrySet()) {
			CscRoles role = entry.getKey();
			EnumSet<CscChannels> channels = entry.getValue();
			for (CscChannels channel : channels) {
				boolean channelExists = channelRoleMap.containsKey(channel);
				EnumSet<CscRoles> roles = channelExists ? channelRoleMap.get(channel) : EnumSet.noneOf(CscRoles.class);
				roles.add(role);
				channelRoleMap.putIfAbsent(channel, roles);
			}
		}

		// Finally, add the channel permissions to the list
		for (Map.Entry<CscChannels, EnumSet<CscRoles>> entry : channelRoleMap.entrySet()) {
			CscChannels channel = entry.getKey();
			EnumSet<CscRoles> roles = entry.getValue();
			channelPermissions.add(new CscCommandChannelPermissions(channel, roles));
		}

		return new CscCommandPermissionValidator(channelPermissions);
	}
}