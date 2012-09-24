package com.dre.warn;
import java.util.HashSet;
import java.util.Set;


public class WPlayer {
	public String player;
	private int Verwarnpunkt;
	public long ResTime;
	
	String Grundliste;
	
	public static Set<WPlayer> WPlayers=new HashSet<WPlayer>();
	
	public WPlayer(){
		WPlayers.add(this);
		this.addPunkt();
		this.Grundliste="";
	}
	
	
	
	public void addPunkt(){
		this.setVerwarnpunkt(this.getVerwarnpunkt()+1);
		this.ResTime=System.currentTimeMillis();
		warn.save();
	}
	public void remPunkt(){
		this.setVerwarnpunkt(this.getVerwarnpunkt()-1);
		this.ResTime=System.currentTimeMillis();
		
		if(this.getVerwarnpunkt() < 0){
			this.setVerwarnpunkt(0);
		}
		warn.save();
	}
	
	public void setName(String name){
		this.player=name;
	}
	
	public void Verwarn(){
		
				
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
	
	
	
	
}
