package com.HuePlugin;

import com.HuePlugin.*;
import io.github.zeroone3010.yahueapi.Hue;
import io.github.zeroone3010.yahueapi.Room;
import net.runelite.api.ChatMessageType;
import io.github.zeroone3010.yahueapi.*;
import java.util.concurrent.CompletableFuture;

public class Fireworks extends Thread {
    Room room;



    public Fireworks(Room room) {
        this.room = room;

    }

    public void run() {

        java.awt.Color[] colArray = new java.awt.Color[]{
                java.awt.Color.CYAN, java.awt.Color.ORANGE,
                java.awt.Color.MAGENTA,java.awt.Color.GREEN,
                java.awt.Color.RED,java.awt.Color.PINK, java.awt.Color.YELLOW
        };

        for (int i = 0; i<7; i++){
            for (int j = 0; j<colArray.length; j++) {


                this.room.setState(io.github.zeroone3010.yahueapi.State.builder().color(Color.of(colArray[j])).on());
                this.room.setBrightness(250);
            }
            try {
                Thread.sleep(500);
            }catch (Exception e){

            }
        }



    }
}