package com.HuePlugin;

import com.HuePlugin.*;
import io.github.zeroone3010.yahueapi.Hue;
import net.runelite.api.ChatMessageType;

import java.util.concurrent.CompletableFuture;

public class ConnectThread extends Thread {
    HuePlugin plugin;



    public ConnectThread(HuePlugin plugin) {
        this.plugin = plugin;

    }

    public void run() {
        try{
             plugin.setHue(new Hue(plugin.getIP(), plugin.getAPI()));
             plugin.setRoom(plugin.getHue().getRoomByName(plugin.getRoomName()).get());
             plugin.setConnectionSucessful(true);
        }catch (Exception e){


        }

    }
}