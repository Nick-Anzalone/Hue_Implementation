package com.HuePlugin;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
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
	name = "Hue Phillips Integration"
)
public class HuePlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private HueConfig config;

	private String RoomName;

	private String IP;

	private String APIKey;

	private Hue hue;

	private Room room;

	private static final Pattern SPECIAL_DROP_MESSAGE = Pattern.compile("(.+) - (.+)");

	private boolean connectionSucessful;

	@Override
	protected void startUp() throws Exception
	{
		connectToHue();
	}

	@Override
	protected void shutDown() throws Exception
	{

	}

	public String getRoomName(){ return this.RoomName;}

	public String getIP(){
	return this.IP;
	}

	public Hue getHue(){  return this.hue;}

	public String getAPI(){return this.APIKey;}

	public void setHue(Hue hue){
		this.hue = hue;
		return;
	}
	public void setRoom(Room room){
		this.room =room;
		return;
	}

	public void setConnectionSucessful(boolean bool){
		this.connectionSucessful = bool;
		return;
	}

	public void connectToHue(){
		this.APIKey = config.APIConfig();
		this.RoomName = config.RoomNameConfig();
		this.IP = config.IPConfig();
		ConnectThread thread = new ConnectThread(this);
		thread.start();
		return;
	}

	public void setToDefaultColor(){
		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

		executorService.schedule(()->{
			this.room.setState(State.builder().color(Color.of(config.defaultColorConfig())).on());
			this.room.setBrightness(250);
		}, config.msToTurnBackConfig(), TimeUnit.MILLISECONDS);
	}
	@Subscribe
	public void onConfigChanged(ConfigChanged event){
		if(event.getKey().equals("ConnectConfig") && event.getNewValue().equals("true") ){
			connectToHue();
		}
		return;
	}


	@Subscribe
	public void onChatMessage(ChatMessage chatMessage)
	{
		if (chatMessage.getType() == ChatMessageType.PUBLICCHAT){
			if(chatMessage.getMessage().equals("!Testhueconnection")){
				if(connectionSucessful){
					client.addChatMessage(ChatMessageType.GAMEMESSAGE,"","Your smart lights are connected.","");
				}else{
					client.addChatMessage(ChatMessageType.GAMEMESSAGE,"","Your smart lights are not connected.","");
				}
			}
			if(chatMessage.getMessage().equals("!Testlights")) {
				if (connectionSucessful) {
					client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Your smart lights should turn on now.", "");
					this.room.setState(State.builder().color(Color.of(config.petDropConfig())).on());
					this.room.setBrightness(250);
					setToDefaultColor();
				} else {
					client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Your smart lights are not connected.", "");

				}
			}
		}
		String message = Text.removeTags(chatMessage.getMessage());

		if(chatMessage.getType() == ChatMessageType.GAMEMESSAGE){

			if(message.contains("You have a funny feeling like") || message.contains("You feel something weird sneaking into your backpack")){
				this.room.setState(State.builder().color(Color.of(config.petDropConfig())).on());
				this.room.setBrightness(250);
				setToDefaultColor();

			}else if(message.contains("Valuable drop:")){

				if(triggerDrop(message)){
					this.room.setState(State.builder().color(Color.of(config.dropColorConfig())).on());
					this.room.setBrightness(250);
					setToDefaultColor();
				}



			}else if(message.contains("Oh dear, you are dead!")){
				this.room.setState(State.builder().color(Color.of(config.deathColorConfig())).on());
				this.room.setBrightness(250);
				setToDefaultColor();

			}else if(message.contains("Congratulations, you've just advanced")){
				Fireworks fw = new Fireworks(this.room, Color.of(config.defaultColorConfig()));
				fw.start();
			}

		}else if(chatMessage.getType() == ChatMessageType.FRIENDSCHATNOTIFICATION){
			Matcher matcher;
			matcher = SPECIAL_DROP_MESSAGE.matcher(message);
			if(matcher.find()){
				this.room.setState(State.builder().color(Color.of(config.raidsDropConfig())).on());
				this.room.setBrightness(250);
				setToDefaultColor();
			}
		}else if(chatMessage.getType() == ChatMessageType.TRADE && chatMessage.getMessage().contains("You won!")){
			this.room.setState(State.builder().color(Color.of(config.dropColorConfig())).on());
			this.room.setBrightness(250);
			setToDefaultColor();
		}


	}

	public boolean triggerDrop(String string) {
		String num = string.substring(string.indexOf("(") + 1, string.indexOf(")"));
		num = num.replaceAll(" coins", "");
		try {

			int check = Integer.parseInt(num);

			if (check > config.valConfig()) {
				return true;
			} else {
				return false;
			}

		}catch (Exception e){

		}
		return false;
	}
	@Provides
	HueConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(HueConfig.class);
	}
}
