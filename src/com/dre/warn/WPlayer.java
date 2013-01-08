package com.dre.warn;
import java.util.HashSet;
import java.util.Set;


public class WPlayer {
	private P p = P.p;
	
	public String player;
	private int Verwarnpunkt;
	public long ResTime;
	
	String Grundliste;
	
	public int reasonzahl;
	public String[] reason;
	public String[] von; 
	public String[] datum;
	public String[] position;
	
	public static Set<WPlayer> WPlayers=new HashSet<WPlayer>();
	
	public WPlayer(){
		WPlayers.add(this);
		this.addPunkt();
		this.Grundliste="";
		this.reason = new String[30];
		this.von = new String[30];
		this.datum = new String[30];
		this.position = new String[30];
	}
	
	public void addPunkt(){
		this.setVerwarnpunkt(this.getVerwarnpunkt()+1);
		this.ResTime=System.currentTimeMillis();
		p.save();
	}
	public void remPunkt(){
		this.setVerwarnpunkt(this.getVerwarnpunkt()-1);
		this.ResTime=System.currentTimeMillis();
		
		if(this.getVerwarnpunkt() < 0){
			this.setVerwarnpunkt(0);
		}
		p.save();
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
