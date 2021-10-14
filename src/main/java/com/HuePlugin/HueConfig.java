package com.HuePlugin;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("example")
public interface HueConfig extends Config
{
	@ConfigItem(
			position = 1,
			keyName = "IPConfig",
			name = "Bridge IP",
			description = "Simple example of a textbox to store strings"
	)
	default String IPConfig() { return ""; }


	@ConfigItem(
			position = 2,
			keyName = "RoomNameConfig",
			name = "Room Name",
			description = "Simple example of a textbox to store strings"
	)
	default String RoomNameConfig() { return ""; }


}
