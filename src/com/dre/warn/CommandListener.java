package com.dre.warn;


import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandListener implements CommandExecutor {
	
	
	
	WPlayer PLAYER;
	String Grund;
	String[] GruList;
	String GruListPart;
	int JailTime;
	boolean isPlayer;
	boolean foundplayer;
	boolean playerAlredyExists;
	String[] Grief;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String nothing, String[] args) {
		if(nothing.equalsIgnoreCase("warn")){
			Player player2=null;
			this.playerAlredyExists=false;
			this.Grund="";
			this.JailTime=0;
			this.PLAYER=null;
			this.foundplayer=false;
			
			if(sender instanceof Player){
				this.isPlayer=true;
				player2=(Player) sender;
				warn.perms.playerHas(player2, "warn.admin");
			}else{
				this.isPlayer=false;
			}
			
			if(args.length > 0){
				
				if(args[0].equalsIgnoreCase("help")){
					if(this.isPlayer){
						sender.sendMessage(ChatColor.GREEN+"==================[WarnDRE Help]==================");
						if(warn.perms.playerHas(player2, "warn.admin") || warn.perms.playerHas(player2, "warn.mod") || sender.isOp()){
							sender.sendMessage(ChatColor.YELLOW+"/warn info <SpielerName> <Seitenzahl>");
							sender.sendMessage(ChatColor.YELLOW+"/Warn [SpielerName] <Grund>");
							sender.sendMessage(ChatColor.YELLOW+"/Warn list");
							if(warn.perms.playerHas(player2, "warn.admin") || sender.isOp()){
								sender.sendMessage(ChatColor.YELLOW+"/Warn reload");
							}
						}else if(warn.perms.playerHas(player2, "warn.user")){
							sender.sendMessage(ChatColor.YELLOW+"/warn info <Seitenzahl>");
						}else{
							sender.sendMessage(ChatColor.GREEN+"[WarnDRE]: "+ChatColor.RED+"Du hast keine Berechtigung auf diesem CMD");
						}
						
					}else{
						warn.w.getServer().getLogger().log(Level.INFO,"[WarnDRE]HelpCMD");
					}
				}else if(args[0].equalsIgnoreCase("list")){
					if(this.isPlayer){
						if(warn.perms.playerHas(player2, "warn.admin") || warn.perms.playerHas(player2, "warn.mod") || sender.isOp()){
							sender.sendMessage(ChatColor.GREEN+"[WarnDRE] Liste:");
							for(WPlayer wplayer:WPlayer.WPlayers){
								sender.sendMessage(ChatColor.YELLOW+"User: "+wplayer.player+" hat "+wplayer.getVerwarnpunkt()+" Punkte");
							}
						}
					}else{
						warn.w.getServer().getLogger().log(Level.INFO,"[WarnDRE]ListCMD");
					}
				}else if(args[0].equalsIgnoreCase("info")){
					if(this.isPlayer){
						//Normaler Spieler
						if(!warn.perms.playerHas(player2, "warn.admin") && !warn.perms.playerHas(player2, "warn.mod") && !sender.isOp()){
							if(warn.perms.playerHas(player2, "warn.user")){
								for(WPlayer wplayer:WPlayer.WPlayers){
									if(wplayer.player.equalsIgnoreCase(sender.getName())){
										sender.sendMessage(ChatColor.GREEN+"[WarnDRE] Info zu "+wplayer.player+":");
										sender.sendMessage(ChatColor.YELLOW+"User: "+wplayer.player+" hat "+wplayer.getVerwarnpunkt()+" Punkte");
										
										int reasonzahldividiert=0;
										if((wplayer.reasonzahl % 3)==0){
											reasonzahldividiert=(wplayer.reasonzahl / 3);
											
										}else{
											reasonzahldividiert=(wplayer.reasonzahl / 3);
											reasonzahldividiert++;
										}
										
										if(args.length > 1){
											sender.sendMessage(ChatColor.GREEN+"[--------------------["+args[1]+"/"+reasonzahldividiert+"]----------------------]");
											if(args[1].matches("[0-9]+") && args[1].length()<6){
												int inc;
												for(inc=((Integer.parseInt(args[1])-1)*3);inc<(Integer.parseInt(args[1])*3);inc++){
													if(inc<wplayer.reasonzahl){
														sender.sendMessage(ChatColor.YELLOW+"Grund:         "+wplayer.reason[inc]);
														sender.sendMessage(ChatColor.YELLOW+"Von:            "+wplayer.von[inc]);
														sender.sendMessage(ChatColor.YELLOW+"Datum:         "+wplayer.datum[inc]);
														sender.sendMessage(ChatColor.YELLOW+"Position:       "+wplayer.position[inc]);								
														sender.sendMessage(ChatColor.YELLOW+"-------------------------------------------------");							
													}
												}
												if(inc==((Integer.parseInt(args[1])-1)*3)){
													sender.sendMessage(ChatColor.YELLOW+"Auf dieser Seite sind keine Einträge!");
												}
											}else{
												sender.sendMessage(ChatColor.YELLOW+"Die Letzte Eingabe muss eine Zahl sein!");
											}
										}else{
											sender.sendMessage(ChatColor.GREEN+"[--------------------[1/"+reasonzahldividiert+"]----------------------]");
											int inc;
											for(inc=0;inc<3;inc++){
												if(inc<wplayer.reasonzahl){
													sender.sendMessage(ChatColor.YELLOW+"Grund:         "+wplayer.reason[inc]);
													sender.sendMessage(ChatColor.YELLOW+"Von:            "+wplayer.von[inc]);
													sender.sendMessage(ChatColor.YELLOW+"Datum:         "+wplayer.datum[inc]);
													sender.sendMessage(ChatColor.YELLOW+"Position:       "+wplayer.position[inc]);								
													sender.sendMessage(ChatColor.YELLOW+"-------------------------------------------------");							
												}
											}
											if(inc==0){
												sender.sendMessage(ChatColor.YELLOW+"Auf dieser Seite sind keine Einträge!");
											}
											
										}
										this.foundplayer=true;
									}
								}
								if(this.foundplayer==false){
									sender.sendMessage(ChatColor.GREEN+"[WarnDRE] Info zu "+sender.getName()+":");
									sender.sendMessage(ChatColor.YELLOW+"User: "+sender.getName()+" hat noch keine Strafen.");
								}
							}else{
								sender.sendMessage(ChatColor.GREEN+"[WarnDRE]:"+ChatColor.RED+"Keine Berechtigung für diesen CMD");
							}
						}else{
							//Admins Mods und OPs
							int foundsearchplayer=0;
							
							for(WPlayer wplayer:WPlayer.WPlayers){
								if(args.length>1){
									if(wplayer.player.equalsIgnoreCase(args[1])){
										sender.sendMessage(ChatColor.GREEN+"[WarnDRE] Info zu "+wplayer.player+":");
										sender.sendMessage(ChatColor.YELLOW+"User: "+wplayer.player+" hat "+wplayer.getVerwarnpunkt()+" Punkte");
										
										int reasonzahldividiert=0;
										
										if((wplayer.reasonzahl % 3)==0){
											reasonzahldividiert=(wplayer.reasonzahl / 3);
											
										}else{
											reasonzahldividiert=(wplayer.reasonzahl / 3);
											reasonzahldividiert++;
										}
										
										if(args.length > 2){
											sender.sendMessage(ChatColor.GREEN+"[--------------------["+args[2]+"/"+reasonzahldividiert+"]----------------------]");
											if(args[2].matches("[0-9]+")&& args[2].length()<6){
												int inc;
												
												for(inc=((Integer.parseInt(args[2])-1)*3);inc<(Integer.parseInt(args[2])*3);inc++){
													if(inc<wplayer.reasonzahl){
														sender.sendMessage(ChatColor.YELLOW+"Grund:         "+wplayer.reason[inc]);
														sender.sendMessage(ChatColor.YELLOW+"Von:            "+wplayer.von[inc]);
														sender.sendMessage(ChatColor.YELLOW+"Datum:         "+wplayer.datum[inc]);
														sender.sendMessage(ChatColor.YELLOW+"Position:       "+wplayer.position[inc]);								
														sender.sendMessage(ChatColor.YELLOW+"-------------------------------------------------");							
													}
												}
												
												if(inc==((Integer.parseInt(args[2])-1)*3)){
													sender.sendMessage(ChatColor.YELLOW+"Auf dieser Seite sind keine Einträge!");
												}
											}else{
												sender.sendMessage(ChatColor.YELLOW+"Die Letzte Eingabe muss eine Zahl sein!");
											}
										}else{
											sender.sendMessage(ChatColor.GREEN+"[--------------------[1/"+reasonzahldividiert+"]----------------------]");
											int inc;
											
											for(inc=0;inc<3;inc++){
												if(inc<wplayer.reasonzahl){
													sender.sendMessage(ChatColor.YELLOW+"Grund:         "+wplayer.reason[inc]);
													sender.sendMessage(ChatColor.YELLOW+"Von:            "+wplayer.von[inc]);
													sender.sendMessage(ChatColor.YELLOW+"Datum:         "+wplayer.datum[inc]);
													sender.sendMessage(ChatColor.YELLOW+"Position:       "+wplayer.position[inc]);								
													sender.sendMessage(ChatColor.YELLOW+"-------------------------------------------------");							
												}
											}
											
											if(inc==0){
												sender.sendMessage(ChatColor.YELLOW+"Auf dieser Seite sind keine Einträge!");
											}
										}
										
										foundsearchplayer=1;
									}
								}else{
									if(wplayer.player.equals(sender.getName())){
										sender.sendMessage(ChatColor.GREEN+"[WarnDRE] Info zu "+wplayer.player+":");
										sender.sendMessage(ChatColor.YELLOW+"User: "+wplayer.player+" hat "+wplayer.getVerwarnpunkt()+" Punkte");
										
										sender.sendMessage(ChatColor.GREEN+"[------------------------------------------------]");
										int inc;
										for(inc=0;inc<wplayer.reasonzahl;inc++){
											if(inc<wplayer.reasonzahl){
												sender.sendMessage(ChatColor.YELLOW+"Grund:         "+wplayer.reason[inc]);
												sender.sendMessage(ChatColor.YELLOW+"Von:            "+wplayer.von[inc]);
												sender.sendMessage(ChatColor.YELLOW+"Datum:         "+wplayer.datum[inc]);
												sender.sendMessage(ChatColor.YELLOW+"Position:       "+wplayer.position[inc]);								
												sender.sendMessage(ChatColor.YELLOW+"-------------------------------------------------");							
											}
										}
										
										if(inc==0){
											sender.sendMessage(ChatColor.YELLOW+"Auf dieser Seite sind keine Einträge!");
										}
										
										this.foundplayer=true;
									}
								}
							}
							
							if(args.length>1){
								if(foundsearchplayer==0){
									sender.sendMessage(ChatColor.GREEN+"[WarnDRE] Info zu "+args[1]+":");
									sender.sendMessage(ChatColor.YELLOW+"User: "+args[1]+" hat noch keine Strafen.");
								}
							}else{
								if(this.foundplayer==false){
									sender.sendMessage(ChatColor.GREEN+"[WarnDRE] Info zu "+sender.getName()+":");
									sender.sendMessage(ChatColor.YELLOW+"User: "+sender.getName()+" hat noch keine Strafen.");
								}
							}	
						}
					}else{
						warn.w.getServer().getLogger().log(Level.INFO,"[WarnDRE]InfoCMD");
					}
				}else if(args[0].equalsIgnoreCase("reload")){
					if(warn.perms.playerHas(player2, "warn.admin") || !(this.isPlayer) || player2.isOp()){
						ConfVerwarnung.ConfVerwarnungen.clear();
						warn.w.loadconfig();
						WPlayer.WPlayers.clear();
						warn.w.load();
						sender.sendMessage(ChatColor.GREEN+"[WarnDRE]: "+ChatColor.WHITE+"Konfig-File und Save-File neu geladen.");
						warn.w.getServer().getLogger().log(Level.INFO,"[WarnDRE]Config Reloaded");
					}else{
						sender.sendMessage(ChatColor.GREEN+"[WarnDRE]: "+ChatColor.RED+"Du hast keine Berechtigung auf diesem CMD");
					}
				}else if(args[0].equalsIgnoreCase("grief")){
					if(this.isPlayer){
						warn.savegrief(sender.getName(), player2.getLocation().getBlockX(), player2.getLocation().getBlockY(), player2.getLocation().getBlockZ());
						sender.sendMessage(ChatColor.GREEN+"[WarnDRE] "+ChatColor.YELLOW+"Grief gemeldet, und Name und Position Gespeichert");
					}
				}else if(args[0].equalsIgnoreCase("savetwo")){
					warn.w.savetwo();
				}else if(args[0].equalsIgnoreCase("tpgrief")){
					if(warn.perms.playerHas(player2, "warn.admin") || sender.isOp()){
						this.Grief=warn.loadgrief().split("[|]");
						if(this.Grief[2].equalsIgnoreCase("0")){
							sender.sendMessage(ChatColor.GREEN+"[WarnDRE] "+ChatColor.YELLOW+"Es wurden keine neuen Griefs gemeldet");
						}else{
							sender.sendMessage(ChatColor.GREEN+"[WarnDRE] "+ChatColor.YELLOW+"Grief gemeldet von: "+this.Grief[0]+" an der Position X:"+this.Grief[1]+" Y:"+this.Grief[2]+" Z:"+this.Grief[3]);
							player2=(Player) sender;
							Bukkit.dispatchCommand(player2, "tploc "+this.Grief[1]+" "+this.Grief[2]+" "+this.Grief[3]);
						}
					}
					
				}else if(args[0].equalsIgnoreCase("chat")){
					if(this.isPlayer){
						player2=(Player) sender;
					}
				}else{
					
					if(warn.perms.playerHas(player2, "warn.admin") || warn.perms.has(player2, "warn.mod") || sender.isOp()){
						if(args.length>1){
							//Wenn bereits Verwarnt
							for(WPlayer wplayer:WPlayer.WPlayers){
								if(wplayer.player.equals(args[0])){
									this.playerAlredyExists=true;
									wplayer.addPunkt();
									this.PLAYER=wplayer;
								}
							}
							
							//Wenn neuer Straftaeter
							if(this.playerAlredyExists == false){
								WPlayer wplayer= new WPlayer();
								wplayer.setName(args[0]);
								this.PLAYER=wplayer;
							}
							
							java.util.Date now=new java.util.Date();
							java.text.SimpleDateFormat date=new java.text.SimpleDateFormat("dd.MM.yyyy/hh.mm.ss");
							
							if(args.length>1){
								this.Grund=args[1];
								
								if(PLAYER.reasonzahl > PLAYER.reason.length){
									PLAYER.reason= warn.w.increaseArray(PLAYER.reason, 10);
								}
								if(PLAYER.reasonzahl > PLAYER.von.length){
									PLAYER.von= warn.w.increaseArray(PLAYER.von, 10);
								}
								if(PLAYER.reasonzahl > PLAYER.datum.length){
									PLAYER.datum= warn.w.increaseArray(PLAYER.datum, 10);
								}
								if(PLAYER.reasonzahl > PLAYER.position.length){
									PLAYER.position= warn.w.increaseArray(PLAYER.position, 10);
								}
								
								this.PLAYER.reason[PLAYER.reasonzahl]=args[1];
								this.PLAYER.von[PLAYER.reasonzahl]=sender.getName();
								this.PLAYER.datum[PLAYER.reasonzahl]=date.format(now);
								this.PLAYER.position[PLAYER.reasonzahl]="POS("+player2.getLocation().getBlockX()+","+player2.getLocation().getBlockY()+","+player2.getLocation().getBlockZ()+")";
								this.PLAYER.reasonzahl++;
								
							}else{
								this.PLAYER.Grundliste=this.PLAYER.Grundliste+"   |UNKNOWN von: "+sender.getName()+" am: "+date.format(now)+"|";
							}
							
							ConsoleCommandSender consolesender=warn.w.getServer().getConsoleSender();
							
							String[] listarray;
							
							for(ConfVerwarnung confVerwarnungen:ConfVerwarnung.ConfVerwarnungen){
								if(this.PLAYER.getVerwarnpunkt() == confVerwarnungen.Nummer){
									for(String list:confVerwarnungen.BefehlListe){
										listarray=list.split(" ");
										
										for(int i=0;i<listarray.length;i++){
											if(listarray[i].equalsIgnoreCase("*Name*")){
												listarray[i]=args[0].toLowerCase();
											}
											if(listarray[i].equalsIgnoreCase("*Grund*")){
												listarray[i]=this.Grund;
											}
										}
										
										list=listarray[0];
										
										for(int i=1;i<listarray.length;i++){
											list=list+" "+listarray[i];
										}
										
										warn.w.getServer().dispatchCommand(consolesender,list);
									}
									if(this.isPlayer){
										sender.sendMessage(ChatColor.GREEN+"[WarnDRE]: "+ChatColor.WHITE+"Der Spieler "+this.PLAYER.player+" wurde erfolgreich Bestraft. Er hat "+this.PLAYER.getVerwarnpunkt()+" VerwarnPunkte.");
									}else{
										warn.w.getServer().getLogger().log(Level.INFO,"[WarnDRE]: "+"Der Speiler "+this.PLAYER.player+" wurde erfolgreich Bestraft. Er hat "+this.PLAYER.getVerwarnpunkt()+" VerwarnPunkte.");
									}
								}
								
							}
						}else{
							if(this.isPlayer){
								sender.sendMessage(ChatColor.GREEN+"[WarnDRE]: "+ChatColor.WHITE+"Bitte Grund Angeben");
							}else{
								warn.w.getServer().getLogger().log(Level.INFO,"[WarnDRE]: "+"Bitte Grund Angeben");
							}
						}
					}else{
						sender.sendMessage(ChatColor.GREEN+"[WarnDRE]: "+ChatColor.RED+"Du hast keine Berechtigung auf diesem CMD");	
					
					}
				}
			}else{
				if(this.isPlayer){
					sender.sendMessage(ChatColor.GREEN+"[WarnDRE] "+ChatColor.WHITE+"/warn help");
				}else{
					
				}
			}
				
			warn.save();
		}
		
		return false;
	}
}
