package com.HuePlugin;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;
import io.github.zeroone3010.yahueapi.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.*;





import net.runelite.api.events.*;


@Slf4j
@PluginDescriptor(
	name = "Hue"
)
public class HuePlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private HueConfig config;

	private String RoomName;

	private String IP;

	private Hue hue;

	private Room room;

	private static final Pattern SPECIAL_DROP_MESSAGE = Pattern.compile("(.+) - (.+)");

	@Override
	protected void startUp() throws Exception
	{



	}

	@Override
	protected void shutDown() throws Exception
	{

	}


	@Subscribe
	public void onChatMessage(ChatMessage chatMessage)
	{
		if (chatMessage.getType() == ChatMessageType.PUBLICCHAT){

			if(chatMessage.getName().equals(client.getLocalPlayer().getName()) && chatMessage.getMessage().equals("!Connecthue")){
				this.RoomName = config.RoomNameConfig();
				this.IP = config.IPConfig();

				final String appName = "RuneLite"; // Fill in the name of your application
				final CompletableFuture<String> apiKey = Hue.hueBridgeConnectionBuilder(this.IP).initializeApiConnection(appName);
				// Push the button on your Hue Bridge to resolve the apiKey future:
				String key;
				try{
					key = apiKey.get();
					this.hue = new Hue(this.IP, key);
					this.room = hue.getRoomByName(this.RoomName).get();
					client.addChatMessage(ChatMessageType.GAMEMESSAGE,"","Connection Sucessful","");
				}catch (Exception e){
					client.addChatMessage(ChatMessageType.GAMEMESSAGE,"","Connection Unsucessful","");


				}



			}
		}






		String message = Text.removeTags(chatMessage.getMessage());



		if(chatMessage.getType() == ChatMessageType.GAMEMESSAGE){
			if (message.contains("interesting")){
				this.room.setState(State.builder().color(Color.of(java.awt.Color.PINK)).on());
				this.room.setBrightness(250);


			}


			if(message.contains("You have a funny feeling like") || message.contains("You feel something weird sneaking into your backpack")){
				this.room.setState(State.builder().color(Color.of(java.awt.Color.CYAN)).on());
				this.room.setBrightness(250);


			}else if(message.contains("Valuable drop:")){

				if(triggerDrop(message)){
					this.room.setState(State.builder().color(Color.of(java.awt.Color.GREEN)).on());
					this.room.setBrightness(250);

				}



			}else if(message.contains("Oh dear, you are dead!")){
				this.room.setState(State.builder().color(Color.of(java.awt.Color.RED)).on());
				this.room.setBrightness(250);

			}

		}else if(chatMessage.getType() == ChatMessageType.FRIENDSCHATNOTIFICATION){
			Matcher matcher;
			matcher = SPECIAL_DROP_MESSAGE.matcher(message);
			if(matcher.find()){

				this.room.setState(State.builder().color(Color.of(java.awt.Color.MAGENTA)).on());
				this.room.setBrightness(250);


			}
		}


	}

	public boolean triggerDrop(String string){
		String num = string.substring(string.indexOf("(")+1,string.indexOf(")"));
		num = num.replaceAll(" coins","");
		int check = Integer.parseInt(num);

		if(check > 1000000){
			return true;
		}else{
			return false;
		}

	}

	@Provides
	HueConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(HueConfig.class);
	}
}
