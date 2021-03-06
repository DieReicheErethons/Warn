package com.dre.warn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandListener implements CommandExecutor {
	private P p = P.p;

	String Grund;
	boolean isPlayer;
	boolean foundplayer;
	String[] Grief;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String nothing, String[] args) {
		if (nothing.equalsIgnoreCase("warn")) {
			Player player2 = null;
			this.Grund = "";
			this.foundplayer = false;

			if (sender instanceof Player) {
				this.isPlayer = true;
				player2 = (Player) sender;
			} else {
				this.isPlayer = false;
			}

			if (args.length > 0) {
				if (args[0].equalsIgnoreCase("help")) {
					if (this.isPlayer) {
						sender.sendMessage(ChatColor.GREEN + "==================[WarnDRE Help]==================");
						if (p.perms.playerHas(player2, "warn.admin") || p.perms.playerHas(player2, "warn.mod") || sender.isOp()) {
							sender.sendMessage(ChatColor.YELLOW + "/warn info <SpielerName> <Seitenzahl>");
							sender.sendMessage(ChatColor.YELLOW + "/Warn [SpielerName] <Grund>");
							sender.sendMessage(ChatColor.YELLOW + "/Warn list");
							if (p.perms.playerHas(player2, "warn.admin") || sender.isOp()) {
								sender.sendMessage(ChatColor.YELLOW + "/Warn reload");
							}
						} else if (p.perms.playerHas(player2, "warn.user")) {
							sender.sendMessage(ChatColor.YELLOW + "/warn info <Seitenzahl>");
						} else {
							sender.sendMessage(ChatColor.GREEN + "[WarnDRE]: " + ChatColor.RED + "Du hast keine Berechtigung auf diesem CMD");
						}

					} else {
						p.getServer().getLogger().log(Level.INFO, "[WarnDRE]HelpCMD");
					}
				} else if (args[0].equalsIgnoreCase("list")) {
					if (this.isPlayer) {
						if (p.perms.playerHas(player2, "warn.admin") || p.perms.playerHas(player2, "warn.mod") || sender.isOp()) {
							int page = 1;
							int maxPages = 0;

							/* Send only players with >0 warnings */
							List<WPlayer> wPlayersWithWarnings = new ArrayList<WPlayer>();

							for (WPlayer wplayer : WPlayer.WPlayers) {
								if (wplayer.getVerwarnpunkt() > 0) {
									wPlayersWithWarnings.add(wplayer);
								}
							}

							/* Sort the list */
							if (args.length > 2) {
								if (args[2].equalsIgnoreCase("name")) {
									Collections.sort(wPlayersWithWarnings, new NameComparator());
								} else if (args[2].equalsIgnoreCase("oname")) {
									Collections.sort(wPlayersWithWarnings, new oNameComparator());
								} else if (args[2].equalsIgnoreCase("points")) {
									Collections.sort(wPlayersWithWarnings, new PointsComparator());
								} else if (args[2].equalsIgnoreCase("opoints")) {
									Collections.sort(wPlayersWithWarnings, new oPointsComparator());
								}
							} else {
								Collections.sort(wPlayersWithWarnings, new NameComparator());
							}

							/* Send the list */
							maxPages = (int) Math.ceil(wPlayersWithWarnings.size() / 10) + 1;

							if (args.length > 1) {
								page = Integer.parseInt(args[1]);
								if (page < 1) {
									page = 1;
								}
								if (page > maxPages) {
									page = maxPages;
								}
							}

							sender.sendMessage(ChatColor.GREEN + "[------ Liste aller Spieler ------]");
							for (int i = (page - 1) * 10; i < page * 10; i++) {
								if (i < wPlayersWithWarnings.size()) {
									WPlayer wplayer = wPlayersWithWarnings.get(i);
									sender.sendMessage(ChatColor.YELLOW + wplayer.getName() + " : " + wplayer.getVerwarnpunkt());
								}
							}
							sender.sendMessage(ChatColor.GREEN + "[----------- (" + page + "/" + maxPages + ") -----------]");
						}
					} else {
						p.getServer().getLogger().log(Level.INFO, "[WarnDRE]ListCMD");
					}
				} else if (args[0].equalsIgnoreCase("info")) {
					if (this.isPlayer) {
						
						if (!p.perms.playerHas(player2, "warn.admin") && !p.perms.playerHas(player2, "warn.mod") && !sender.isOp()) { 
							if (p.perms.playerHas(player2, "warn.user")) { // Show the warnings	
								WPlayer wplayer = WPlayer.get(args[1]);
								
								if (wplayer != null) {
									sender.sendMessage(ChatColor.GREEN + "[WarnDRE] Info zu " + sender.getName() + ":");
									sender.sendMessage(ChatColor.YELLOW + "User: " + sender.getName() + " hat " + wplayer.getVerwarnpunkt() + " Punkte");

									int reasonzahldividiert = 0;
									if ((wplayer.reasonzahl % 3) == 0) {
										reasonzahldividiert = (wplayer.reasonzahl / 3);

									} else {
										reasonzahldividiert = (wplayer.reasonzahl / 3);
										reasonzahldividiert++;
									}

									if (args.length > 1) {
										sender.sendMessage(ChatColor.GREEN + "[--------------------[" + args[1] + "/" + reasonzahldividiert + "]----------------------]");
										if (args[1].matches("[0-9]+") && args[1].length() < 6) {
											int inc;
											for (inc = ((Integer.parseInt(args[1]) - 1) * 3); inc < (Integer.parseInt(args[1]) * 3); inc++) {
												if (inc < wplayer.reasonzahl) {
													sender.sendMessage(ChatColor.YELLOW + "Grund:         " + wplayer.reason[inc]);
													sender.sendMessage(ChatColor.YELLOW + "Von:            " + wplayer.von[inc]);
													sender.sendMessage(ChatColor.YELLOW + "Datum:         " + wplayer.datum[inc]);
													sender.sendMessage(ChatColor.YELLOW + "Position:       " + wplayer.position[inc]);
													sender.sendMessage(ChatColor.YELLOW + "-------------------------------------------------");
												}
											}
											if (inc == ((Integer.parseInt(args[1]) - 1) * 3)) {
												sender.sendMessage(ChatColor.YELLOW + "Auf dieser Seite sind keine Eintr�ge!");
											}
										} else {
											sender.sendMessage(ChatColor.YELLOW + "Die Letzte Eingabe muss eine Zahl sein!");
										}
									} else {
										sender.sendMessage(ChatColor.GREEN + "[--------------------[1/" + reasonzahldividiert + "]----------------------]");
										int inc;
										for (inc = 0; inc < 3; inc++) {
											if (inc < wplayer.reasonzahl) {
												sender.sendMessage(ChatColor.YELLOW + "Grund:         " + wplayer.reason[inc]);
												sender.sendMessage(ChatColor.YELLOW + "Von:            " + wplayer.von[inc]);
												sender.sendMessage(ChatColor.YELLOW + "Datum:         " + wplayer.datum[inc]);
												sender.sendMessage(ChatColor.YELLOW + "Position:       " + wplayer.position[inc]);
												sender.sendMessage(ChatColor.YELLOW + "-------------------------------------------------");
											}
										}
										if (inc == 0) {
											sender.sendMessage(ChatColor.YELLOW + "Auf dieser Seite sind keine Eintr�ge!");
										}

									}
								} else {
									sender.sendMessage(ChatColor.GREEN + "[WarnDRE] Info zu " + sender.getName() + ":");
									sender.sendMessage(ChatColor.YELLOW + "User: " + sender.getName() + " hat noch keine Strafen.");
								}
							} else {
								sender.sendMessage(ChatColor.GREEN + "[WarnDRE]:" + ChatColor.RED + "Keine Berechtigung f�r diesen CMD");
							}
						} else { // Warn the player
							if (args.length > 1) {
								WPlayer wplayer = WPlayer.get(args[1]);
								
								if (wplayer != null) {
									sender.sendMessage(ChatColor.GREEN + "[WarnDRE] Info zu " + wplayer.getName() + ":");
									sender.sendMessage(ChatColor.YELLOW + "User: " + wplayer.getName() + " hat " + wplayer.getVerwarnpunkt() + " Punkte");

									int reasonzahldividiert = 0;

									if ((wplayer.reasonzahl % 3) == 0) {
										reasonzahldividiert = (wplayer.reasonzahl / 3);

									} else {
										reasonzahldividiert = (wplayer.reasonzahl / 3);
										reasonzahldividiert++;
									}

									if (args.length > 2) {
										sender.sendMessage(ChatColor.GREEN + "[--------------------[" + args[2] + "/" + reasonzahldividiert + "]----------------------]");
										if (args[2].matches("[0-9]+") && args[2].length() < 6) {
											int inc;

											for (inc = ((Integer.parseInt(args[2]) - 1) * 3); inc < (Integer.parseInt(args[2]) * 3); inc++) {
												if (inc < wplayer.reasonzahl) {
													sender.sendMessage(ChatColor.YELLOW + "Grund:         " + wplayer.reason[inc]);
													sender.sendMessage(ChatColor.YELLOW + "Von:            " + wplayer.von[inc]);
													sender.sendMessage(ChatColor.YELLOW + "Datum:         " + wplayer.datum[inc]);
													sender.sendMessage(ChatColor.YELLOW + "Position:       " + wplayer.position[inc]);
													sender.sendMessage(ChatColor.YELLOW + "-------------------------------------------------");
												}
											}

											if (inc == ((Integer.parseInt(args[2]) - 1) * 3)) {
												sender.sendMessage(ChatColor.YELLOW + "Auf dieser Seite sind keine Eintr�ge!");
											}
										} else {
											sender.sendMessage(ChatColor.YELLOW + "Die Letzte Eingabe muss eine Zahl sein!");
										}
									} else {
										sender.sendMessage(ChatColor.GREEN + "[--------------------[1/" + reasonzahldividiert + "]----------------------]");
										int inc;

										for (inc = 0; inc < 3; inc++) {
											if (inc < wplayer.reasonzahl) {
												sender.sendMessage(ChatColor.YELLOW + "Grund:         " + wplayer.reason[inc]);
												sender.sendMessage(ChatColor.YELLOW + "Von:            " + wplayer.von[inc]);
												sender.sendMessage(ChatColor.YELLOW + "Datum:         " + wplayer.datum[inc]);
												sender.sendMessage(ChatColor.YELLOW + "Position:       " + wplayer.position[inc]);
												sender.sendMessage(ChatColor.YELLOW + "-------------------------------------------------");
											}
										}

										if (inc == 0) {
											sender.sendMessage(ChatColor.YELLOW + "Auf dieser Seite sind keine Eintr�ge!");
										}
									}
								} else {
									sender.sendMessage(ChatColor.GREEN + "[WarnDRE] Info zu " + args[1] + ":");
									sender.sendMessage(ChatColor.YELLOW + "User: " + args[1] + " hat noch keine Strafen.");
								}	
							} else {
								WPlayer wplayer = WPlayer.get(sender.getName());
								if (wplayer != null) {
									sender.sendMessage(ChatColor.GREEN + "[WarnDRE] Info zu " + sender.getName() + ":");
									sender.sendMessage(ChatColor.YELLOW + "User: " + sender.getName() + " hat " + wplayer.getVerwarnpunkt() + " Punkte");

									sender.sendMessage(ChatColor.GREEN + "[------------------------------------------------]");
									int inc;
									for (inc = 0; inc < wplayer.reasonzahl; inc++) {
										if (inc < wplayer.reasonzahl) {
											sender.sendMessage(ChatColor.YELLOW + "Grund:         " + wplayer.reason[inc]);
											sender.sendMessage(ChatColor.YELLOW + "Von:            " + wplayer.von[inc]);
											sender.sendMessage(ChatColor.YELLOW + "Datum:         " + wplayer.datum[inc]);
											sender.sendMessage(ChatColor.YELLOW + "Position:       " + wplayer.position[inc]);
											sender.sendMessage(ChatColor.YELLOW + "-------------------------------------------------");
										}
									}

									if (inc == 0) {
										sender.sendMessage(ChatColor.YELLOW + "Auf dieser Seite sind keine Eintr�ge!");
									}

									this.foundplayer = true;
								} else {
									if (this.foundplayer == false) {
										sender.sendMessage(ChatColor.GREEN + "[WarnDRE] Info zu " + sender.getName() + ":");
										sender.sendMessage(ChatColor.YELLOW + "User: " + sender.getName() + " hat noch keine Strafen.");
									}
								}
							}
						}
					}
				} else if (args[0].equalsIgnoreCase("reload")) {
					if (p.perms.playerHas(player2, "warn.admin") || !(this.isPlayer) || player2.isOp()) {
						ConfVerwarnung.ConfVerwarnungen.clear();
						p.loadConfig();
						WPlayer.WPlayers.clear();
						p.load();
						sender.sendMessage(ChatColor.GREEN + "[WarnDRE]: " + ChatColor.WHITE + "Konfig-File und Save-File neu geladen.");
						p.getServer().getLogger().log(Level.INFO, "[WarnDRE]Config Reloaded");
					} else {
						sender.sendMessage(ChatColor.GREEN + "[WarnDRE]: " + ChatColor.RED + "Du hast keine Berechtigung auf diesem CMD");
					}
				} else if (args[0].equalsIgnoreCase("grief")) {
					if (this.isPlayer) {
						p.saveGrief(player2.getUniqueId(), player2.getLocation().getBlockX(), player2.getLocation().getBlockY(), player2.getLocation().getBlockZ());
						sender.sendMessage(ChatColor.GREEN + "[WarnDRE] " + ChatColor.YELLOW + "Grief gemeldet, und Name und Position Gespeichert");
					}
				} else if (args[0].equalsIgnoreCase("savetwo")) {
					if (p.perms.playerHas(player2, "warn.admin") || p.perms.playerHas(player2, "warn.mod") || sender.isOp()) {
						p.saveTwo();
					}
				} else if (args[0].equalsIgnoreCase("tpgrief")) {
					if (p.perms.playerHas(player2, "warn.admin") || p.perms.playerHas(player2, "warn.mod") || sender.isOp()) {
						this.Grief = p.loadGrief().split("[|]");

						if (this.Grief[2].equalsIgnoreCase("0")) {
							sender.sendMessage(ChatColor.GREEN + "[WarnDRE] " + ChatColor.YELLOW + "Es wurden keine neuen Griefs gemeldet");
						} else {
							sender.sendMessage(ChatColor.GREEN + "[WarnDRE] " + ChatColor.YELLOW + "Grief gemeldet von: " + this.Grief[0] + " an der Position X:" + this.Grief[1] + " Y:"
									+ this.Grief[2] + " Z:" + this.Grief[3]);
							player2 = (Player) sender;
							Bukkit.dispatchCommand(player2, "tploc " + this.Grief[1] + " " + this.Grief[2] + " " + this.Grief[3]);
						}
					}
				} else if (args[0].equalsIgnoreCase("remove")) {
					if (p.perms.playerHas(player2, "warn.admin") || p.perms.playerHas(player2, "warn.mod") || sender.isOp()) {
						if (args.length > 1) {
							WPlayer wplayer = WPlayer.get(args[1]);
							
							if (wplayer != null) {
								wplayer.remPunkt();
								sender.sendMessage(ChatColor.GREEN + "1 Punkt wurde von " + ChatColor.GOLD + args[1] + ChatColor.GREEN + " entfernt");
							} else {
								sender.sendMessage(ChatColor.RED + "Spieler " + ChatColor.GOLD + args[1] + ChatColor.RED + " wurde nicht gefunden!");
							}
						} else {
							sender.sendMessage(ChatColor.RED + "/warn remove <player>");
						}
					}
				} else {
					if (p.perms.playerHas(player2, "warn.admin") || p.perms.has(player2, "warn.mod") || sender.isOp()) {
						if (args.length > 1) {
							WPlayer wplayer = WPlayer.get(args[0]);
							
							if (wplayer != null) {
								wplayer.addPunkt();
	
								java.util.Date now = new java.util.Date();
								java.text.SimpleDateFormat date = new java.text.SimpleDateFormat("dd.MM.yyyy/hh.mm.ss");
	
								if (args.length > 1) {
									this.Grund = args[1];
	
									if (wplayer.reasonzahl >= wplayer.reason.length) {
										wplayer.reason = p.increaseArray(wplayer.reason, 1);
									}
									if (wplayer.reasonzahl >= wplayer.von.length) {
										wplayer.von = p.increaseArray(wplayer.von, 1);
									}
									if (wplayer.reasonzahl >= wplayer.datum.length) {
										wplayer.datum = p.increaseArray(wplayer.datum, 1);
									}
									if (wplayer.reasonzahl >= wplayer.position.length) {
										wplayer.position = p.increaseArray(wplayer.position, 1);
									}
	
									wplayer.reason[wplayer.reasonzahl] = args[1];
									wplayer.von[wplayer.reasonzahl] = sender.getName();
									wplayer.datum[wplayer.reasonzahl] = date.format(now);
									wplayer.position[wplayer.reasonzahl] = 
											"POS(" + player2.getLocation().getBlockX() 
											+ "," + player2.getLocation().getBlockY() 
											+ "," + player2.getLocation().getBlockZ() 
											+ ")";
									wplayer.reasonzahl++;
	
								} else {
									wplayer.Grundliste = wplayer.Grundliste + "   |UNKNOWN von: " + sender.getName() + " am: " + date.format(now) + "|";
								}
	
								ConsoleCommandSender consolesender = p.getServer().getConsoleSender();
	
								String[] listarray;
	
								for (ConfVerwarnung confVerwarnungen : ConfVerwarnung.ConfVerwarnungen) {
									if (wplayer.getVerwarnpunkt() == confVerwarnungen.Nummer) {
										for (String list : confVerwarnungen.BefehlListe) {
											listarray = list.split(" ");
	
											for (int i = 0; i < listarray.length; i++) {
												if (listarray[i].equalsIgnoreCase("*Name*")) {
													listarray[i] = args[0].toLowerCase();
												}
												if (listarray[i].equalsIgnoreCase("*Grund*")) {
													listarray[i] = this.Grund;
												}
											}
	
											list = listarray[0];
	
											for (int i = 1; i < listarray.length; i++) {
												list = list + " " + listarray[i];
											}
	
											p.getServer().dispatchCommand(consolesender, list);
										}
										if (this.isPlayer) {
											sender.sendMessage(ChatColor.GREEN + "[WarnDRE]: " + ChatColor.WHITE + "Der Spieler " + wplayer.getName() + " wurde erfolgreich Bestraft. Er hat "
													+ wplayer.getVerwarnpunkt() + " VerwarnPunkte.");
										} else {
											p.getServer()
													.getLogger()
													.log(Level.INFO,
															"[WarnDRE]: " + "Der Speiler " + wplayer.getName() + " wurde erfolgreich Bestraft. Er hat " + wplayer.getVerwarnpunkt()
																	+ " VerwarnPunkte.");
										}
									}
								}
							} else {
								sender.sendMessage(ChatColor.GREEN + "[WarnDRE]: " + ChatColor.WHITE + "Spieler war noch nie auf dem Server!");
							}
						} else {
							if (this.isPlayer) {
								sender.sendMessage(ChatColor.GREEN + "[WarnDRE]: " + ChatColor.WHITE + "Bitte Grund Angeben");
							} else {
								p.getServer().getLogger().log(Level.INFO, "[WarnDRE]: " + "Bitte Grund Angeben");
							}
						}
					} else {
						sender.sendMessage(ChatColor.GREEN + "[WarnDRE]: " + ChatColor.RED + "Du hast keine Berechtigung auf diesem CMD");

					}
				}
			} else {
				if (this.isPlayer) {
					sender.sendMessage(ChatColor.GREEN + "[WarnDRE] " + ChatColor.WHITE + "/warn help");
				} else {

				}
			}
		}

		return false;
	}

	/* Comporators */
	private class NameComparator implements Comparator<WPlayer> {
		@Override
		public int compare(WPlayer player1, WPlayer player2) {
			return player1.getName().compareTo(player2.getName());
		}
	}

	private class oNameComparator implements Comparator<WPlayer> {
		@Override
		public int compare(WPlayer player1, WPlayer player2) {
			return -player1.getName().compareTo(player2.getName());
		}
	}

	private class PointsComparator implements Comparator<WPlayer> {
		@Override
		public int compare(WPlayer player1, WPlayer player2) {
			return ((Integer) player1.getVerwarnPunkte()).compareTo((Integer) player2.getVerwarnPunkte());
		}
	}

	private class oPointsComparator implements Comparator<WPlayer> {
		@Override
		public int compare(WPlayer player1, WPlayer player2) {
			return -((Integer) player1.getVerwarnPunkte()).compareTo((Integer) player2.getVerwarnPunkte());
		}
	}
}
