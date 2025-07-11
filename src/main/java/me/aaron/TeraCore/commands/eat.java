package me.aaron.TeraCore.commands;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.aaron.TeraCore.main.LanguageLoader;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.aaron.TeraCore.color.PlaceHolder;
import me.aaron.TeraCore.main.ConfigLoader;
import me.aaron.TeraCore.main.DefaultConfig;
import me.aaron.TeraCore.main.TeraMain;

public class eat implements CommandExecutor, TabCompleter {

	File file;
	FileConfiguration config;


	public eat() {
		String filetype = getClass().getSimpleName();
		LanguageLoader.load(LanguageLoader.LanguageFolder.commands, filetype);
		File temp = new File(CommandMain.datafolder, filetype + ".yml");
		if (temp.exists()) {
			file = temp;
			config = (FileConfiguration) YamlConfiguration.loadConfiguration(file);
		} else {
			config = ConfigLoader.getConfig(filetype);
			file = temp;
			try {
				config.save(temp);
			} catch (IOException e) {
			}
		}
	}

	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (command.getName().equalsIgnoreCase("eat")) {
				
				if (player.hasPermission(config.getString("command.args0.permission"))
						|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
					if (args.length == 0) {
						player.setFoodLevel(20);
						player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.args0.usage")));
						return true;
					}
					if (args.length == 1) {
						if (player.hasPermission(config.getString("command.args1.permission"))
								|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
							Player trust = Bukkit.getPlayer(args[0]);
							if (trust == null) {
								player.sendMessage(PlaceHolder.replacePlaceholder(
										DefaultConfig.getConfig().getString("message.player_not_found")));
								return true;
							}

							trust.setFoodLevel(20);
							trust.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.args0.usage")));
							player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.args1.usage")
									.replace("%player%", trust.getDisplayName())));
							return true;
						}

						player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.help")));

						return true;
					}
				} else {
					player.sendMessage(PlaceHolder
							.replacePlaceholder(DefaultConfig.getConfig().getString("message.no_permission")));
					return true;
				}
			}
		}
		return false;
	}

	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> tab = new ArrayList<>();
		if (sender instanceof Player) {
			Player player = (Player) sender;
		if (command.getName().equalsIgnoreCase("eat")) {
			try {
			if (player.hasPermission(config.getString("command.args0.permission"))
					|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
				if (args.length == 1) {
					List<String> end = new ArrayList<>();

					for (Player online : Bukkit.getOnlinePlayers()) {
						tab.add(online.getDisplayName());
					}
					for (int i = 0; i < tab.size(); i++) {
						if (args[0] != null) {
							if (tab.get(i).toLowerCase().startsWith(args[0].toLowerCase())) {
								end.add(tab.get(i));
							}
						}
					}
					return end;
				}

			}
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
		}
		return tab;
	}
}
