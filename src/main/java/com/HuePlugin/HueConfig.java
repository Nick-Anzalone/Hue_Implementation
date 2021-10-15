package com.HuePlugin;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

import java.awt.*;

@ConfigGroup("example")
public interface HueConfig extends Config
{

	@ConfigSection(
			name = "Connection",
			description = "Connection Settings",
			position = 0,
			closedByDefault = false
	)
	String connectionSettings = "connectionSettings";
	@ConfigItem(
			position = 1,
			keyName = "IPConfig",
			name = "Bridge IP",
			description = "Enter the IP adress of Phillips Bridge here",
			section = connectionSettings
	)
	default String IPConfig() { return ""; }


	@ConfigItem(

			position = 2,
			keyName = "APIKeyConfig",
			name = "API Key",
			description = "Enter the API Key of the Phillips Bridge Here",
			section = connectionSettings
	)
	default String APIConfig() { return ""; }

	@ConfigItem(
			position = 2,
			keyName = "RoomNameConfig",
			name = "Room Name",
			description = "Enter the name of the room you wish the plugin to connect to here.",
			section = connectionSettings
	)
	default String RoomNameConfig() { return ""; }

	@ConfigItem(
			position = 3,
			keyName = "ConnectConfig",
			name = "Connect",
			description = "Check this box to connect to the plugin.",
			section = connectionSettings
	)
	default boolean ConnectConfig(){ return false;}


	@ConfigSection(
			name = "Color Configuration",
			description = "Connection Settings",
			position = 100,
			closedByDefault = true
	)
	String colorSettings = "colorSettings";


	@ConfigItem(
			position = 101,
			keyName = "valConfig",
			name = "Drop Value Threshold",
			description = "How expensive of a drop do you want the plugin to trigger on?",
			section  =colorSettings
	)
	default int valConfig() { return 1000000; }



	@ConfigItem(
			position = 102,
			keyName = "dropColorConfig",
			name = "Valuable Drop Color",
			description = "This color represents the color of the light when you receive an expensive drop",
			section  =colorSettings
	)
	default Color dropColorConfig() { return Color.GREEN; }

	@ConfigItem(
			position = 103,
			keyName = "deathColorConfig",
			name = "Death Color",
			description = "This color represents the color of the light when you die",
			section  =colorSettings
	)
	default Color deathColorConfig() {return Color.RED;}


	@ConfigItem(
			position = 104,
			keyName = "raidsDropConfig",
			name = "Raids Drop Color",
			description = "This color represents the color of the light when you receive a raids drop",
			section  =colorSettings
	)
	default Color raidsDropConfig() {return Color.MAGENTA;}


	@ConfigItem(
			position = 105,
			keyName = "petDropConfig",
			name = "Pet Drop Color",
			description = "This color represents the color of the light when you receive a pet",
			section  =colorSettings
	)
	default Color petDropConfig() {return Color.CYAN;}


}
