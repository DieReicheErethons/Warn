package com.dre.warn;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;


public class PlayerListener implements Listener {
	@EventHandler()
	public void onPlayerLogin(PlayerLoginEvent event) {
		WPlayer wplayer = WPlayer.get(event.getPlayer().getUniqueId());
		if (wplayer != null) {
			wplayer.lastname = event.getPlayer().getName();
		} else {
			wplayer = new WPlayer();
			wplayer.uuid = event.getPlayer().getUniqueId();
			wplayer.lastname = event.getPlayer().getName();
		}
	}
}
