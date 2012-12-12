package com.dre.warn;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.permission.Permission;

public class warn extends JavaPlugin{
	public static warn w;
	
	public static int ResetTime;
	
	@Override
	public void onEnable(){
		w=this;
		
		this.load();
		this.loadconfig();
		warn.save();
		
		getCommand("warn").setExecutor(new CommandListener());
		
		/* Sheduler */
		w.getServer().getScheduler().scheduleSyncRepeatingTask(w, new Runnable() {
		    public void run() {
		    	for(WPlayer wplayer:WPlayer.WPlayers){
		    		if((warn.ResetTime+wplayer.ResTime)<System.currentTimeMillis()){
		    			wplayer.remPunkt();
		    			w.getServer().getLogger().log(Level.INFO,"[WarnDRE]punkt weg von "+wplayer.player);
		    		}
		    	}
		    	
		    	
		        System.currentTimeMillis();
		    }
		}, 10000L, 10000L);
		
		w.setupPermissions();
	}
	
	public static void save(){
		File file = new File(warn.w.getDataFolder(),"saves.yml");
		FileConfiguration configFile = new YamlConfiguration();
		
		for(WPlayer wplayer:WPlayer.WPlayers){
				configFile.set(wplayer.player+".warnings",wplayer.getVerwarnPunkte());
				configFile.set(wplayer.player+".time",wplayer.ResTime);
				configFile.set(wplayer.player+".reasonzahl", wplayer.reasonzahl);
				
				for(int i=0;i<wplayer.reasonzahl;i++){
					configFile.set(wplayer.player+".resonList."+i+".reason", wplayer.reason[i]);
					configFile.set(wplayer.player+".resonList."+i+".von", wplayer.von[i]);
					configFile.set(wplayer.player+".resonList."+i+".datum", wplayer.datum[i]);
					configFile.set(wplayer.player+".resonList."+i+".position", wplayer.position[i]);
				}
		}
		
		try {
			configFile.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void savegrief(String Name, double x, double y, double z){
		File file = new File(warn.w.getDataFolder(),"Griefs/"+System.currentTimeMillis()+".yml");
		FileConfiguration configFile = new YamlConfiguration();
		
		configFile.set("Name", Name);
		configFile.set("x", x);
		configFile.set("y", y);
		configFile.set("z", z);
		
		try {
			configFile.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static String loadgrief(){
		File file = new File(warn.w.getDataFolder(),"Griefs");
		
		File file2=null;
		int fileint=0;
		
		File[] fileArray = file.listFiles();
		
		double x = 0,y = 0,z = 0;
		String Name=null;
		
		for(int i=0;i<fileArray.length;i++){
			if(fileArray[i].isFile()){
				file2 = new File(fileArray[i].getAbsolutePath());
				fileint=i;
				i=fileArray.length+10;
			}
		}
			
		if(file2==null){
			return null+"|"+0+"|"+0+"|"+0;
		}
		
		if((!file2.exists()) || (file2==null)){
			return null+"|"+0+"|"+0+"|"+0;
		}
		
		new YamlConfiguration();
		FileConfiguration configFile = YamlConfiguration.loadConfiguration(file2);
		Name=configFile.getString("Name");
		x=configFile.getDouble("x");
		y=configFile.getDouble("y");
		z=configFile.getDouble("z");
		fileArray[fileint].delete();
		
		return Name+"|"+x+"|"+y+"|"+z;
	}
	
	
	
	public void loadconfig(){
		File file = new File(this.getDataFolder(),"config.yml");
		FileConfiguration configFile = YamlConfiguration.loadConfiguration(file);
		
		int nochzahlen=0;
		int num=0;	
		do{
			num=num+1;
			if(configFile.contains("Strafen."+num)){
				ConfVerwarnung verwarnung=new ConfVerwarnung();
				verwarnung.setNummer(num);
				
				List<String> StrafenListe=(List<String>) configFile.getList("Strafen."+num);
				if(StrafenListe!=null){
					for(String strafe:StrafenListe){
						verwarnung.BefehlListe.add(strafe);
					}
				}
			}else{
				nochzahlen=1;
			}
		}while(nochzahlen == 0);
		
		warn.ResetTime=configFile.getInt("ResetTime");
		warn.ResetTime=warn.ResetTime*60*1000;
	}
	
	
	
	
	
	public void load(){
		File file = new File(this.getDataFolder(),"saves.yml");
		FileConfiguration configFile = YamlConfiguration.loadConfiguration(file);
		
		for(String player:configFile.getKeys(false)){
			WPlayer wplayer= new WPlayer();
			wplayer.setName(player);
			wplayer.setVerwarnpunkt(configFile.getInt(player+".warnings"));
			wplayer.ResTime=configFile.getLong(player+".time");
			wplayer.reasonzahl=configFile.getInt(player+".reasonzahl");
			
			for(int i=0;i<wplayer.reasonzahl;i++){
				if(i > wplayer.reason.length){
					wplayer.reason= this.increaseArray(wplayer.reason, 10);
				}
				if(i > wplayer.von.length){
					wplayer.von= this.increaseArray(wplayer.von, 10);
				}
				if(i > wplayer.datum.length){
					wplayer.datum= this.increaseArray(wplayer.datum, 10);
				}
				if(i > wplayer.position.length){
					wplayer.position= this.increaseArray(wplayer.position, 10);
				}
				
				wplayer.reason[i]=configFile.getString(player+".resonList."+i+".reason");
				wplayer.von[i]=configFile.getString(player+".resonList."+i+".von");
				wplayer.datum[i]=configFile.getString(player+".resonList."+i+".datum");
				wplayer.position[i]=configFile.getString(player+".resonList."+i+".position");
			}
		}
	}
	
	
	public String[] increaseArray(String[] theArray, int increaseBy)  
	{  
	    int i = theArray.length;  
	    int n = ++i; 
	    n=n+increaseBy;
	    String[] newArray = new String[n];  
	    for(int cnt=0;cnt<theArray.length;cnt++) 
	    {  
	        newArray[cnt] = theArray[cnt];  
	    }  
	    return newArray;  
	}
	
	
	
	public void savetwo(){
		File file = new File(this.getDataFolder(),"savesalt.yml");
		FileConfiguration configFile = YamlConfiguration.loadConfiguration(file);
		File file2 = new File(this.getDataFolder(),"savesneu.yml");
		FileConfiguration configFile2 = YamlConfiguration.loadConfiguration(file2);
		
		int vps=0;
		long time=0;
		String gruli="";
		String reason="";
		String von="";
		String datum="";
		String position="";
		
		int lol=0;
		int count=0;
		
		for(String player:configFile.getKeys(false)){
			if(!player.equalsIgnoreCase("null")){
				count=0;
				
				vps=configFile.getInt(player+".warnings");
				time=configFile.getLong(player+".time");
				gruli=configFile.getString(player+".resonList");
				
				configFile2.set(player+".warnings",vps);
				configFile2.set(player+".time",time);
				
				String[] getreasonzahlTEXT = gruli.split("[ ]");
				int reasonzahl=0;
				
				for (int a=0; a<getreasonzahlTEXT.length; a++){
					if(getreasonzahlTEXT[a].startsWith("|")){
						reasonzahl++;
					}
				}
				configFile2.set(player+".reasonzahl",reasonzahl);
				
				String[] result = gruli.split("[ ]");
				for (int x=0; x<result.length; x++){
					if(!result[x].equals(null)){
						lol=0;
						if(result[x].startsWith("|wegen--->")){
							if(lol==0){
								reason=result[x].replace("|wegen--->", "");
								von=result[x+1].replace("von--->", "");
								datum=result[x+2].replace("am--->", "");
								position=result[x+3].replace("Ort_der_Bestrafung--->", "").replace("|", "");
								
								configFile2.set(player+".resonList."+count+".reason", reason);
								configFile2.set(player+".resonList."+count+".von", von);
								configFile2.set(player+".resonList."+count+".datum", datum);
								configFile2.set(player+".resonList."+count+".position", position);
								count=count+1;
								lol=1;
							}
						}else if(result[x].startsWith("|") && result[x].endsWith("|")){
							if(lol==0){
								reason=result[x].replace("|", "");
								von="unknown";
								datum="unknown";
								position="unknown";
								
								configFile2.set(player+".resonList."+count+".reason", reason);
								configFile2.set(player+".resonList."+count+".von", von);
								configFile2.set(player+".resonList."+count+".datum", datum);
								configFile2.set(player+".resonList."+count+".position", position);
								count=count+1;
								lol=1;
							}
						}else if(result[x].startsWith("|")){
							if(lol==0){
								reason=result[x].replace("|", "");
								von=result[x+2];
								datum=result[x+4]+"/"+result[x+5];
								position=result[x+6].replace("|", "");
								
								configFile2.set(player+".resonList."+count+".reason", reason);
								configFile2.set(player+".resonList."+count+".von", von);
								configFile2.set(player+".resonList."+count+".datum", datum);
								configFile2.set(player+".resonList."+count+".position", position);
								count=count+1;
								lol=1;
							}
						}
					}
				}
			}
		}
		
		try {
			configFile2.save(file2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/* Init Permissions */
	
	public static Permission perms = null;
	
	private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
}
