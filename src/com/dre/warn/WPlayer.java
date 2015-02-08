package com.dre.warn;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;

public class WPlayer {
	public static Set<WPlayer> WPlayers = new HashSet<WPlayer>();
	
	public UUID uuid;
	public String lastname;
	private int Verwarnpunkt;
	public long ResTime;

	public String Grundliste = "";

	public int reasonzahl;
	public String[] reason = new String[0];
	public String[] von = new String[0];
	public String[] datum = new String[0];
	public String[] position = new String[0];

	public WPlayer() {
		WPlayers.add(this);
	}

	public void addPunkt() {
		this.setVerwarnpunkt(this.getVerwarnpunkt() + 1);
		this.ResTime = System.currentTimeMillis();
	}

	public void remPunkt() {
		this.setVerwarnpunkt(this.getVerwarnpunkt() - 1);

		if (this.getVerwarnpunkt() < 0) {
			this.setVerwarnpunkt(0);
		}
	}

	public int getVerwarnPunkte() {
		return this.getVerwarnpunkt();
	}

	public int getVerwarnpunkt() {
		return Verwarnpunkt;
	}

	public void setVerwarnpunkt(int verwarnpunkt) {
		Verwarnpunkt = verwarnpunkt;
	}
	
	public static WPlayer get(String name) {
		for (WPlayer wplayer : WPlayers) {
			String wname = wplayer.getName(); 
			
			if (wname != null) {
				if (wname.equalsIgnoreCase(name)) {
					return wplayer;
				}
			}
		}
		
		return null;
	}
	
	public static WPlayer get(UUID uuid) {
		for (WPlayer wplayer : WPlayers) {
			if (wplayer.uuid == uuid) {
				return wplayer;
			}
		}
		
		return null;
	}

	public String getName() {
		if (lastname == null) {
			lastname = Bukkit.getOfflinePlayer(uuid).getName();
		}
		
		return lastname;
	}
}
