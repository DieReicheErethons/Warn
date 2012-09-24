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
		
		w.getServer().getLogger().log(Level.INFO,"[WarnDRE]Plugin Geladen");
		
		getCommand("warn").setExecutor(new CommandListener());
		
		
		//sheduler
		
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
		
		warn.w.getServer().getLogger().log(Level.INFO,"asd gehen");
		
		w.setupPermissions();
		
		
		warn.w.getServer().getLogger().log(Level.INFO,"ssss gehen");
		
	}
	
	
	
	public static void save(){
		File file = new File(warn.w.getDataFolder(),"saves.yml");
		FileConfiguration configFile = new YamlConfiguration();
		
		
		for(WPlayer wplayer:WPlayer.WPlayers){
			configFile.set(wplayer.player+".warnings",wplayer.getVerwarnPunkte());
			configFile.set(wplayer.player+".time",wplayer.ResTime);
			configFile.set(wplayer.player+".resonList", wplayer.Grundliste);
		}
		
		try {
			configFile.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void savegrief(String Name, double x, double y, double z){
		
		File file = new File(warn.w.getDataFolder(),"Griefs/"+System.currentTimeMillis()+".yml");
		FileConfiguration configFile = new YamlConfiguration();
		
		//int num=0;
		//int nochgriefs=1;
		
		/*do{
			num=num+1;
			if(configFile.contains(""+num)){
				
				nochgriefs=1;
			}else{
				nochgriefs=0;
			}
		}while(nochgriefs==1);*/
		
		
		
		
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
				warn.w.getServer().getLogger().log(Level.INFO,fileArray[i].getAbsolutePath());
				i=fileArray.length+10;
				
			}
		}
			
		if(file2==null){return null+"|"+0+"|"+0+"|"+0;}
		if((!file2.exists()) || (file2==null)){return null+"|"+0+"|"+0+"|"+0;}
		
		
		
			new YamlConfiguration();
			FileConfiguration configFile = YamlConfiguration.loadConfiguration(file2);
			Name=configFile.getString("Name");
			x=configFile.getDouble("x");
			y=configFile.getDouble("y");
			z=configFile.getDouble("z");
			fileArray[fileint].delete();
			
		//}else{
			
			//return null+"|"+0+"|"+0+"|"+0;
		//}
		
		String returnwert=null;
		
		returnwert=Name+"|"+x+"|"+y+"|"+z;
		return returnwert;
		
		
		/*int first=1;
		int localzahl=0;
		for(String zahl:configFile.getKeys(false)){
			if(first==1){
				first=0;
				
				
				
				
				
			}else{
				localzahl = Integer.parseInt(zahl);
				localzahl--;
				configFile.set(localzahl+".Name", configFile.getString(zahl+".Name"));
				configFile.set(localzahl+".x", configFile.getDouble(zahl+".x"));
				configFile.set(localzahl+".y", configFile.getDouble(zahl+".y"));
				configFile.set(localzahl+".z", configFile.getDouble(zahl+".z"));
			}
		}*/
		
		
		
		
		
		
		
		
		
	/*	try {
			configFile.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
	}
	
	
	
	public void loadconfig(){
		File file = new File(this.getDataFolder(),"config.yml");
		FileConfiguration configFile = YamlConfiguration.loadConfiguration(file);
		
		int nochzahlen=0;
		int num=0;
		
		//w.getServer().getLogger().log(Level.INFO,"configfile wird geladen....-.-"+configFile.getKeys(false));
		//if(configFile.getKeys(false).equals("Strafen")){
			//w.getServer().getLogger().log(Level.INFO,"Strafen gefundet");
		//configFile.contains("Strafen."+id);
			//for(String num:configFile.getKeys(false)){
				
			do{	
				num=num+1;
				if(configFile.contains("Strafen."+num)){
					//w.getServer().getLogger().log(Level.INFO,"nummern gefundet="+num);
					ConfVerwarnung verwarnung=new ConfVerwarnung();
					verwarnung.setNummer(num);
					//w.getServer().getLogger().log(Level.INFO,"neuer conf eintrag");
					@SuppressWarnings("unchecked")
					List<String> StrafenListe=(List<String>) configFile.getList("Strafen."+num);
					//w.getServer().getLogger().log(Level.INFO,"Straflisten geladen"+StrafenListe);
					if(StrafenListe!=null){
						for(String strafe:StrafenListe){
							//w.getServer().getLogger().log(Level.INFO,"Straflisten geladen"+strafe);
							verwarnung.BefehlListe.add(strafe);
							//w.getServer().getLogger().log(Level.INFO,""+verwarnung.BefehlListe);
						}
					}else{
						//w.getServer().getLogger().log(Level.INFO,"Straflisten geladen");
					}
				}else{
					nochzahlen=1;
				}
			}while(nochzahlen == 0);
		//}
		
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
			wplayer.Grundliste=configFile.getString(player+".resonList");
		}
	}
	
	
	public static Permission perms = null;
	
	private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
	
	
	
}
