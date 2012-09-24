package com.dre.warn;

import java.util.logging.Level;

import net.milkbowl.vault.chat.Chat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.ChatPaginator.ChatPage;

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
		warn.w.getServer().getLogger().log(Level.INFO,cmd.getName());
		if(nothing.equalsIgnoreCase("warn")){
			warn.w.getServer().getLogger().log(Level.INFO,"command warn startet");
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
						if(warn.perms.playerHas(player2, "warn.user")){
							sender.sendMessage(ChatColor.YELLOW+"/warn info");
						}else if(warn.perms.playerHas(player2, "warn.admin") || warn.perms.playerHas(player2, "warn.mod") || sender.isOp()){
							sender.sendMessage(ChatColor.YELLOW+"/warn info <SpielerName>");
							sender.sendMessage(ChatColor.YELLOW+"/Warn [SpielerName] <Grund>");
							sender.sendMessage(ChatColor.YELLOW+"/Warn list");
							if(warn.perms.playerHas(player2, "warn.admin") || sender.isOp()){
								sender.sendMessage(ChatColor.YELLOW+"/Warn reload");
							}
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
						if(args.length > 1){
							if(warn.perms.playerHas(player2, "warn.admin") || warn.perms.playerHas(player2, "warn.mod") || sender.isOp()){
								for(WPlayer wplayer:WPlayer.WPlayers){
									if(wplayer.player.equals(args[1])){
										sender.sendMessage(ChatColor.GREEN+"[WarnDRE] Info zu "+wplayer.player+":");
										sender.sendMessage(ChatColor.YELLOW+"User: "+wplayer.player+" hat "+wplayer.getVerwarnpunkt()+" Punkte");
										//sender.sendMessage(ChatColor.YELLOW+"--->  "+wplayer.Grundliste);
										
										this.GruList=wplayer.Grundliste.split("[ ]");
										
										
										for(int inc=0;inc<this.GruList.length;inc++){
											if (!this.GruList[inc].equals(" ")){
												if (!this.GruList[inc].equals("  ")){
													if (!this.GruList[inc].equals("am:")){
														if (!this.GruList[inc].equals("um:")){
															if (!this.GruList[inc].equals("von:")){
																if(this.GruList[inc].contains("|") && (!this.GruList[inc].contains("POS"))){
																	sender.sendMessage(ChatColor.YELLOW+" ");
																}												
																sender.sendMessage(ChatColor.YELLOW+this.GruList[inc]);
															}
														}
													}
												}
											}
										}
										
										
										/*{
											
											this.GruListPart=this.GruList.substring(0, this.GruList.indexOf(" "));
											this.GruList=this.GruList.substring(this.GruList.indexOf(" ")+2);
											//sender.sendMessage(ChatColor.YELLOW+"--->  "+this.GruListPart);
											sender.sendMessage(ChatColor.YELLOW+"--->  "+this.GruList.indexOf(" "));
											
										}while(this.GruList.contains(' '));
										*/
										
										
										//this.GruListPart=this.GruList.substring(this.GruList.indexOf("|")+1,this.GruList.indexOf(" ",this.GruList.indexOf("|")));
										//sender.sendMessage(ChatColor.YELLOW+"--->  "+this.GruListPart);
										
										//this.GruListPart=this.GruList.substring(this.GruList.indexOf(" ",this.GruList.indexOf("|"))+1,this.GruList.indexOf(" ",this.GruList.indexOf("|")));
										//sender.sendMessage(ChatColor.YELLOW+"--->  "+this.GruListPart);
										
										this.foundplayer=true;
									}
										
								}
								if(this.foundplayer==false){
									sender.sendMessage(ChatColor.GREEN+"[WarnDRE] Info zu "+args[1]+":");
									sender.sendMessage(ChatColor.YELLOW+"User: "+args[1]+" hat noch keine Strafen.");
								}
							}else if(warn.perms.playerHas(player2, "warn.user")){
								sender.sendMessage(ChatColor.GREEN+"[WarnDRE]: "+ChatColor.WHITE+"Versuche /warn info");
							}else{
								sender.sendMessage(ChatColor.GREEN+"[WarnDRE]: "+ChatColor.RED+"Du hast keine Berechtigung auf diesem CMD");
							}
						}else{
							if(warn.perms.playerHas(player2, "warn.admin") || warn.perms.playerHas(player2, "warn.mod") || warn.perms.playerHas(player2, "warn.user") || sender.isOp()){
								for(WPlayer wplayer:WPlayer.WPlayers){
									if(wplayer.player.equals(sender.getName())){
										sender.sendMessage(ChatColor.GREEN+"[WarnDRE] Info zu "+wplayer.player+":");
										sender.sendMessage(ChatColor.YELLOW+"User: "+wplayer.player+" hat "+wplayer.getVerwarnpunkt()+" Punkte");
										//sender.sendMessage(ChatColor.YELLOW+"--->  "+wplayer.Grundliste);
										
										
										this.GruList=wplayer.Grundliste.split("[ ]");
										
										
										for(int inc=0;inc<this.GruList.length;inc++){
											if (!this.GruList[inc].equals(" ")){
												if (!this.GruList[inc].equals("  ")){
													if (!this.GruList[inc].equals("am:")){
														if (!this.GruList[inc].equals("um:")){
															if (!this.GruList[inc].equals("von:")){
																if(this.GruList[inc].contains("|") && (!this.GruList[inc].contains("POS"))){
																	sender.sendMessage(ChatColor.YELLOW+" ");
																}												
																sender.sendMessage(ChatColor.YELLOW+this.GruList[inc]);
															}
														}
													}
												}
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
								sender.sendMessage(ChatColor.GREEN+"[WarnDRE]: "+ChatColor.RED+"Du hast keine Berechtigung auf diesem CMD");
								
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
						
						//////////////////////////////////HSajsfklsdhfkalsödkfasdfasdölkf    Chat
						
						
					}
				}else{
					
					if(warn.perms.playerHas(player2, "warn.admin") || warn.perms.has(player2, "warn.mod") || sender.isOp()){
						if(args.length>1){
							warn.w.getServer().getLogger().log(Level.INFO,"perms gehen");
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
								this.PLAYER.Grundliste=this.PLAYER.Grundliste+" |wegen--->"+args[1]+" von--->"+sender.getName()+" am--->"+date.format(now)+" Ort_der_Bestrafung--->POS("+player2.getLocation().getBlockX()+","+player2.getLocation().getBlockY()+","+player2.getLocation().getBlockZ()+")|";
							}else{
								this.PLAYER.Grundliste=this.PLAYER.Grundliste+"   |UNKNOWN von: "+sender.getName()+" am: "+date.format(now)+"|";
							}
							
							
							
							
							ConsoleCommandSender consolesender=warn.w.getServer().getConsoleSender();
							//warn.w.getServer().dispatchCommand(consolesender, "say /jail ");
							
							String[] listarray;
							
							for(ConfVerwarnung confVerwarnungen:ConfVerwarnung.ConfVerwarnungen){
								//warn.w.getServer().dispatchCommand(consolesender, "say /jail "+this.PLAYER.getVerwarnpunkt()+"!"+confVerwarnungen.Nummer);
								if(this.PLAYER.getVerwarnpunkt() == confVerwarnungen.Nummer){
									for(String list:confVerwarnungen.BefehlListe){
										//strreplace
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
										
										//warn.w.getServer().dispatchCommand(consolesender,"say "+list);
										
										
										warn.w.getServer().getLogger().log(Level.INFO,"log3 gehen");
										
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
				
				
				
				//warn.w.getServer().dispatchCommand(consolesender, "say /jail "+warn.ResetTime+" "+this.JailTime+" "+this.Grund);
				
				
				
				
				
				
			}else{
				if(this.isPlayer){
					sender.sendMessage(ChatColor.GREEN+"[WarnDRE] "+ChatColor.WHITE+"/warn help");
				}else{
					
				}
			}
				
				
				
			warn.save();
		
		}
		/*if(nothing.equalsIgnoreCase("grief")){
			warn.w.getServer().getLogger().log(Level.INFO,cmd.getName());
			Player player2=null;
			if(sender instanceof Player){
				this.isPlayer=true;
				player2=(Player) sender;
				warn.perms.playerHas(player2, "warn.admin");
			}else{
				this.isPlayer=false;
			}
			if(this.isPlayer){
				
				warn.savegrief(sender.getName(), player2.getLocation().getBlockX(), player2.getLocation().getBlockY(), player2.getLocation().getBlockZ());
				sender.sendMessage(ChatColor.GREEN+"[WarnDRE] "+ChatColor.YELLOW+"Grief gemeldet, und Name und Position Gespeichert");
				
			}
			
			
		}
		if(nothing.equalsIgnoreCase("tpgrief")){
			warn.w.getServer().getLogger().log(Level.INFO,"command tpgrief startet");
			Player player2=null;
			if(sender instanceof Player){
				this.isPlayer=true;
				player2=(Player) sender;
				warn.perms.playerHas(player2, "warn.admin");
			}else{
				this.isPlayer=false;
			}
			if(this.isPlayer){
				if(warn.perms.playerHas(player2, "warn.admin") || sender.isOp()){
					
					this.Grief=warn.loadgrief().split("[|]");
					sender.sendMessage(ChatColor.YELLOW+"Grief gemeldet von: "+this.Grief[0]+" an der Position X:"+this.Grief[1]+" Y:"+this.Grief[2]+" Z:"+this.Grief[3]);
				}
			}
			
			
		}*/
		return false;
	}
	
	
	

}
