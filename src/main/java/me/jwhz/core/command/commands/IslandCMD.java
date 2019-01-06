package me.jwhz.core.command.commands;

import me.jwhz.core.command.CommandBase;
import me.jwhz.core.config.ConfigValue;
import me.jwhz.core.gui.guis.skyblock.IslandGUI;
import me.jwhz.core.gui.guis.skyblock.IslandSelectorGUI;
import me.jwhz.core.gui.guis.skyblock.IslandTopGUI;
import me.jwhz.core.skyblock.islands.Island;
import me.jwhz.core.skyblock.islands.Settings;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

import static me.jwhz.core.Core.pl;
import static me.jwhz.core.utils.TimeUtils.formatSeconds;
import static org.bukkit.ChatColor.YELLOW;

@CommandBase.Info(
        command = "island"
)
public class IslandCMD extends CommandBase {

    @ConfigValue(path = "messages.skyblock.invalid number")
    public String invalidNumber = "&7Invalid number entered!";
    @ConfigValue(path = "messages.skyblock.no island found")
    public String noIslandFound = "&cNo island found!";
    @ConfigValue(path = "messages.skyblock.teleported to your island")
    public String teleportedToYourIsland = "&eYou have teleported to your island.";
    @ConfigValue(path = "messages.skyblock.banlist header")
    public String banListHeader = "&eBanned List:";
    @ConfigValue(path = "messages.skyblock.you do not have an island")
    public String noIsland = "&cYou do not have an island.";
    @ConfigValue(path = "messages.skyblock.specify a player")
    public String specifyPlayer = "&cPlease specify a player.";
    @ConfigValue(path = "messages.skyblock.player not found")
    public String playerNotFound = "&cPlayer can not be found.";
    @ConfigValue(path = "messages.skyblock.cant ban coop members")
    public String youCantBanCoopMembers = "&cYou can not ban people apart of your island.";
    @ConfigValue(path = "messages.skyblock.already banned")
    public String alreadyBanned = "&cThis user is already banned from your island.";
    @ConfigValue(path = "messages.skyblock.player banned")
    public String playerBanned = "&eYou have banned &6$player &efrom your island.";
    @ConfigValue(path = "messages.skyblock.you have been banned")
    public String youHaveBeenBanned = "&cYou have been banned from &e$player&c's island.";
    @ConfigValue(path = "messages.skyblock.not banned from island")
    public String notBanned = "&cThis user isn't banned from your island.";
    @ConfigValue(path = "messages.skyblock.unbanned player")
    public String unbannedPlayer = "&eYou have unbanned &6$player &efrom your island.";
    @ConfigValue(path = "messages.skyblock.you have been unbanned")
    public String playerHasBeenUnbanned = "&cYou have been unbanned from &e$player&c's island.";
    @ConfigValue(path = "messages.skyblock.you must be an owner")
    public String mustBeOwner = "&cYou must be the owner of the island to use this command.";
    @ConfigValue(path = "messages.skyblock.relevel cooldown")
    public String relevelCooldown = "&cPlease wait $cooldown before recalculating your island level.";
    @ConfigValue(path = "messages.skyblock.calculating island level")
    public String calculatingIslandLevel = "&eCalculating island level...";
    @ConfigValue(path = "messages.skyblock.your island level")
    public String islandLevel = "&eYour island level is:&f $level";
    @ConfigValue(path = "messages.skyblock.player doesnt have island")
    public String playerDoesntHaveIsland = "&cThis player does not have an island.";
    @ConfigValue(path = "messages.skyblock.failed tp because banned")
    public String failedTp = "&cYou can't teleport due to being banned from their island.";
    @ConfigValue(path = "messages.skyblock.private island")
    public String privateIsland = "&cThis island is currently private.";
    @ConfigValue(path = "messages.skyblock.teleported to users island")
    public String teleportedToIsland = "&eYou have teleported to &a$player&e's island.";
    @ConfigValue(path = "messages.skyblock.island full")
    public String islandFull = "&cYou can't invite because your island is full.";
    @ConfigValue(path = "messages.skyblock.cant invite yourself")
    public String cantInviteSelf = "&cYou can't invite yourself to your island.";
    @ConfigValue(path = "messages.skyblock.player already has island")
    public String playerAlreadyHasIsland = "&cThis player already has an island.";
    @ConfigValue(path = "messages.skyblock.player already invited")
    public String playerAlreadyInvited = "&cYou have already invited this player.";
    @ConfigValue(path = "messages.skyblock.player invited")
    public String playerInvited = "&eYou have invited $player to your island.";
    @ConfigValue(path = "messages.skyblock.island invitation")
    public String invitation = "You have received an invite to join $player's island. Click here to join";
    @ConfigValue(path = "messages.skyblock.no pending invites")
    public String noInvites = "&cYou have no pending invites.";
    @ConfigValue(path = "messages.skyblock.this player didnt invite you")
    public String didntInvite = "&cThis player did not invite you to their island.";
    @ConfigValue(path = "messages.skyblock.this island is already full")
    public String thisIslandAlreadyFull = "&c$player's island is already full.";
    @ConfigValue(path = "messages.skyblock.already in island")
    public String alreadyInIsland = "&cYou are already in an island.";
    @ConfigValue(path = "messages.skyblock.player joined")
    public String playerJoined = "&e$player has joined your island";
    @ConfigValue(path = "messages.skyblock.joined island")
    public String joinedIsland = "&eYou have joined &a$player's&e island.";
    @ConfigValue(path = "messages.skyblock.deleted island")
    public String deletedIsland = "&cYou have deleted your island.";
    @ConfigValue(path = "messages.skyblock.left island")
    public String leftIsland = "&cYou have left your island.";
    @ConfigValue(path = "messages.skyblock.cant kick yourself")
    public String cantKickYourself = "&cYou can't kick yourself from your island.";
    @ConfigValue(path = "messages.skyblock.player not apart island")
    public String notApartOfIsland = "&cYou can't kick this player because they aren't apart of your island.";
    @ConfigValue(path = "messages.skyblock.kicked")
    public String kicked = "&eYou have been kicked from your island.";
    @ConfigValue(path = "messages.skyblock.youve kicked")
    public String youveKicked = "&eYou have kicked &bplayer&e from your island.";
    @ConfigValue(path = "messages.skyblock.deleted other island")
    public String deletedOtherIsland = "&eYou have deleted &b$player's&e island.";
    @ConfigValue(path = "messages.skyblock.no permission")
    public String noPermission = "&cNo permission.";

    public LinkedHashMap<UUID, UUID> invites = new LinkedHashMap<>();

    @Override
    public void onCommand(CommandSender sender, String[] args) {

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        if (args.length < 1) {

            if (core.skyblockManager.hasIsland(uuid))
                new IslandGUI(player);
            else
                new IslandSelectorGUI(player);

        } else {

            if (args[0].equalsIgnoreCase("go")) {

                if (core.skyblockManager.hasIsland(((Player) sender).getUniqueId())) {

                    player.sendMessage(teleportedToYourIsland);
                    player.teleport(core.skyblockManager.getIsland(((Player) sender).getUniqueId()).spawn);

                } else
                    player.sendMessage(noIslandFound);

            } else if (args[0].equalsIgnoreCase("banned")) {

                if (core.skyblockManager.hasIsland(uuid)) {

                    player.sendMessage(banListHeader);

                    if (core.skyblockManager.getIsland(uuid).banned != null)
                        for (String id : core.skyblockManager.getIsland(uuid).banned)
                            player.sendMessage(" " + ChatColor.WHITE + Bukkit.getOfflinePlayer(UUID.fromString(id)).getName());

                } else
                    player.sendMessage(noIsland);

            } else if (args[0].equalsIgnoreCase("ban")) {

                if (args.length < 2) {

                    player.sendMessage(specifyPlayer);
                    return;

                }

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                if (target == null || !target.hasPlayedBefore()) {

                    player.sendMessage(playerNotFound);
                    return;

                }

                if (core.skyblockManager.getIsland(uuid).coop.contains(target.getUniqueId().toString())) {

                    player.sendMessage(youCantBanCoopMembers);
                    return;

                }

                if (core.skyblockManager.getIsland(player.getUniqueId()).banned.contains(target.getUniqueId().toString())) {

                    player.sendMessage(alreadyBanned);
                    return;

                }

                player.sendMessage(playerBanned.replace("$player", target.getName()));

                if (target.isOnline()) {
                    Location loc = (Location) core.commandManager.getYamlConfiguration().get("Spawn Location");

                    Bukkit.getPlayer(target.getUniqueId()).teleport(loc);
                    Bukkit.getPlayer(target.getUniqueId()).sendMessage(youHaveBeenBanned.replace("$player", player.getName()));

                }

                core.skyblockManager.getIsland(player.getUniqueId()).banned.add(target.getUniqueId().toString());
                core.skyblockManager.getIsland(player.getUniqueId()).save();

            } else if (args[0].equalsIgnoreCase("unban")) {

                if (args.length < 2) {

                    player.sendMessage(specifyPlayer);
                    return;

                }

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                if (target == null || !target.hasPlayedBefore()) {

                    player.sendMessage(playerNotFound);
                    return;

                }

                if (!core.skyblockManager.getIsland(uuid).banned.contains(target.getUniqueId().toString())) {

                    player.sendMessage(notBanned);
                    return;

                }

                player.sendMessage(unbannedPlayer.replace("$player", target.getName()));

                if (target.isOnline())
                    Bukkit.getPlayer(target.getUniqueId()).sendMessage(playerHasBeenUnbanned.replace("$player", player.getName()));

                core.skyblockManager.getIsland(player.getUniqueId()).banned.remove(target.getUniqueId().toString());
                core.skyblockManager.getIsland(player.getUniqueId()).save();


            } else if (args[0].equalsIgnoreCase("relevel")) {

                if (!core.skyblockManager.hasIsland(uuid)) {

                    player.sendMessage(noIsland);
                    return;

                }

                if (!core.skyblockManager.isOwner(uuid)) {

                    player.sendMessage(mustBeOwner);
                    return;

                }

                if (core.levelSystem.hasCooldown(core.skyblockManager.getIsland(uuid))) {

                    player.sendMessage(relevelCooldown.replace("$cooldown", formatSeconds(core.levelSystem.getLeftover(core.skyblockManager.getIsland(uuid)))));
                    return;

                }

                player.sendMessage(calculatingIslandLevel);

                int level = core.levelSystem.getLevel(core.skyblockManager.getIsland(uuid));

                player.sendMessage(islandLevel.replace("$level", level + ""));

            } else if (args[0].equalsIgnoreCase("list")) {

                if (core.skyblockManager.hasIsland(player.getUniqueId())) {

                    Island island = core.skyblockManager.getIsland(player.getUniqueId());

                    int totalOnline = 0, total = island.coop.size() + 1;

                    for (String id : island.coop)
                        if (Bukkit.getPlayer(UUID.fromString(id)) != null && Bukkit.getPlayer(UUID.fromString(id)).isOnline())
                            totalOnline++;

                    if (Bukkit.getPlayer(island.owner) != null && Bukkit.getPlayer(island.owner).isOnline())
                        totalOnline++;

                    player.sendMessage(c("&7&m-----------------------------------------------------"));
                    player.sendMessage(ChatColor.BLUE + Bukkit.getOfflinePlayer(island.owner).getName() + "'s Island " + ChatColor.GRAY + "[" + totalOnline + "/" + total + "]");

                    player.sendMessage(c("&eOwner: &a" + (Bukkit.getOfflinePlayer(island.owner).isOnline() ? "&a" : "&7") + Bukkit.getOfflinePlayer(island.owner).getName()));

                    String string = "";

                    if (island.coop != null)
                        for (String id : island.coop)
                            if (Bukkit.getPlayer(UUID.fromString(id)) != null && Bukkit.getPlayer(UUID.fromString(id)).isOnline())
                                string += ChatColor.GREEN + Bukkit.getOfflinePlayer(UUID.fromString(id)).getName() + ChatColor.GRAY + ", ";
                            else
                                string += ChatColor.GRAY + Bukkit.getOfflinePlayer(UUID.fromString(id)).getName() + ", ";


                    if (string.length() > 0)
                        player.sendMessage(c("&eMembers: " + string.substring(0, string.length() - 2)));
                    player.sendMessage(c("&7&m-----------------------------------------------------"));

                } else
                    player.sendMessage(noIslandFound);

            } else if (args[0].equalsIgnoreCase("new")) {

                new IslandSelectorGUI(player);

            } else if (args[0].equalsIgnoreCase("visit")) {

                if (args.length < 2) {

                    player.sendMessage(specifyPlayer);

                    return;

                }

                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {

                    player.sendMessage(playerNotFound);
                    return;

                }

                if (!core.skyblockManager.hasIsland(target.getUniqueId())) {

                    player.sendMessage(playerDoesntHaveIsland);
                    return;

                }

                if (core.skyblockManager.getIsland(target.getUniqueId()).banned.contains(player.getUniqueId().toString()))
                    player.sendMessage(failedTp);
                else {

                    if (core.skyblockManager.getIsland(target.getUniqueId()).settings.getValue(Settings.Value.PRIVATE) &&
                            !core.skyblockManager.getIsland(target.getUniqueId()).coop.contains(uuid.toString()) &&
                            !core.skyblockManager.getIsland(target.getUniqueId()).owner.equals(uuid))
                        player.sendMessage(privateIsland);
                    else {

                        player.sendMessage(teleportedToIsland.replace("$player", target.getName()));
                        player.teleport(core.skyblockManager.getIsland(target.getUniqueId()).spawn);

                    }
                }

            } else if (args[0].equalsIgnoreCase("show")) {

                if (args.length < 2) {

                    player.sendMessage(specifyPlayer);

                    return;

                }

                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {

                    player.sendMessage(playerNotFound);
                    return;

                }

                if (!core.skyblockManager.hasIsland(target.getUniqueId())) {

                    player.sendMessage(playerDoesntHaveIsland);
                    return;

                }

                Island island = core.skyblockManager.getIsland(target.getUniqueId());

                int totalOnline = 0, total = island.coop.size() + 1;

                for (String id : island.coop)
                    if (Bukkit.getPlayer(UUID.fromString(id)) != null && Bukkit.getPlayer(UUID.fromString(id)).isOnline())
                        totalOnline++;

                if (Bukkit.getPlayer(island.owner) != null && Bukkit.getPlayer(island.owner).isOnline())
                    totalOnline++;

                player.sendMessage(c("&7&m-----------------------------------------------------"));
                player.sendMessage(ChatColor.BLUE + Bukkit.getOfflinePlayer(island.owner).getName() + "'s Island " + ChatColor.GRAY + "[" + totalOnline + "/" + total + "] "
                        + ChatColor.DARK_AQUA + "- " + ChatColor.YELLOW + "Island Level: " + ChatColor.WHITE + core.levelSystem.levels.get(island));

                player.sendMessage(c("&eOwner: &a" + (Bukkit.getOfflinePlayer(island.owner).isOnline() ? "&a" : "&7") + Bukkit.getOfflinePlayer(island.owner).getName()));

                String string = "";

                if (island.coop != null)
                    for (String id : island.coop)
                        if (Bukkit.getPlayer(UUID.fromString(id)) != null && Bukkit.getPlayer(UUID.fromString(id)).isOnline())
                            string += ChatColor.GREEN + Bukkit.getOfflinePlayer(UUID.fromString(id)).getName() + ChatColor.GRAY + ", ";
                        else
                            string += ChatColor.GRAY + Bukkit.getOfflinePlayer(UUID.fromString(id)).getName() + ", ";

                if (string.length() > 0)
                    player.sendMessage(c("&eMembers: " + string.substring(0, string.length() - 2)));

                int size = island.max.x - island.min.x;

                player.sendMessage(c("&eBorder: &f" + size + "x" + size + " &3- &eType: &f" + island.chosenSchematic));

                player.sendMessage(c("&7&m-----------------------------------------------------"));

            } else if (args[0].equalsIgnoreCase("invite")) {

                if (!core.skyblockManager.hasIsland(uuid)) {

                    player.sendMessage(noIsland);
                    return;

                }

                if (core.skyblockManager.getIsland(uuid).coop != null && core.skyblockManager.getIsland(uuid).coop.size() >= 4) {

                    player.sendMessage(islandFull);
                    return;

                }

                if (args.length < 2) {

                    player.sendMessage(specifyPlayer);
                    return;

                }

                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {

                    player.sendMessage(playerNotFound);
                    return;

                }

                if (target.equals(player)) {

                    player.sendMessage(cantInviteSelf);
                    return;

                }

                if (core.skyblockManager.hasIsland(target.getUniqueId())) {

                    player.sendMessage(playerAlreadyHasIsland);
                    return;

                }

                if (invites.containsKey(uuid) && invites.get(uuid).equals(target.getUniqueId())) {

                    player.sendMessage(playerAlreadyInvited);
                    return;

                }

                invites.put(uuid, target.getUniqueId());

                player.sendMessage(playerInvited.replace("$player", target.getName()));

                target.sendMessage("");
                TextComponent textComponent = new TextComponent(invitation.replace("$player", player.getName()));
                textComponent.setColor(net.md_5.bungee.api.ChatColor.GREEN);
                textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/is accept " + player.getName()));
                textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent(ChatColor.GREEN + "Click to accept.")}));

                target.spigot().sendMessage(textComponent);
                target.sendMessage("");


            } else if (args[0].equalsIgnoreCase("accept")) {

                if (!invites.containsValue(uuid)) {

                    player.sendMessage(noInvites);
                    return;

                }

                if (args.length < 2) {

                    player.sendMessage(specifyPlayer);
                    return;

                }

                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {

                    player.sendMessage(playerNotFound);
                    return;

                }

                if (!invites.containsKey(target.getUniqueId()) || !invites.get(target.getUniqueId()).equals(uuid)) {

                    player.sendMessage(didntInvite.replace("$player", target.getName()));
                    return;

                }

                Island island = core.skyblockManager.getIsland(target.getUniqueId());

                if (island.coop != null && island.coop.size() >= 4) {

                    player.sendMessage(thisIslandAlreadyFull.replace("$player", target.getName()));
                    return;

                }

                if (core.skyblockManager.hasIsland(uuid)) {

                    player.sendMessage(alreadyInIsland);
                    return;

                }

                target.sendMessage(playerJoined.replace("$player", player.getName()));
                player.sendMessage(joinedIsland.replace("$player", target.getName()));

                Iterator<Map.Entry<UUID, UUID>> invs = invites.entrySet().iterator();

                while (invs.hasNext())
                    if (invs.next().getValue().toString().equals(uuid.toString()))
                        invs.remove();

                island.coop.add(uuid.toString());
                island.save();

                player.teleport(island.spawn);

            } else if (args[0].equalsIgnoreCase("leave")) {

                if (!core.skyblockManager.hasIsland(uuid)) {

                    player.sendMessage(noIsland);
                    return;

                }

                if (core.skyblockManager.isOwner(uuid)) {

                    core.skyblockManager.deleteIsland(player);

                    player.sendMessage(deletedIsland);

                } else {

                    Island island = core.skyblockManager.getIsland(uuid);
                    island.coop.remove(uuid.toString());
                    island.save();

                    Location loc = (Location) core.commandManager.getYamlConfiguration().get("Spawn Location");

                    player.teleport(loc);

                    player.sendMessage(leftIsland);

                }


            } else if (args[0].equalsIgnoreCase("kick")) {

                if (!core.skyblockManager.hasIsland(uuid)) {

                    player.sendMessage(noIsland);
                    return;

                }

                if (!core.skyblockManager.isOwner(uuid)) {

                    player.sendMessage(mustBeOwner);
                    return;

                }

                if (args.length < 2) {

                    player.sendMessage(specifyPlayer);
                    return;

                }

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                if (target == null) {

                    player.sendMessage(playerNotFound);
                    return;

                }

                if (target.equals(player)) {

                    player.sendMessage(cantKickYourself);
                    return;

                }

                if (core.skyblockManager.getIsland(uuid).coop != null && !core.skyblockManager.getIsland(uuid).coop.contains(target.getUniqueId().toString())) {

                    player.sendMessage(notApartOfIsland);
                    return;

                }

                if (target.isOnline()) {

                    target.getPlayer().sendMessage(kicked);
                    Location loc = (Location) core.commandManager.getYamlConfiguration().get("Spawn Location");

                    target.getPlayer().teleport(loc);

                }

                player.sendMessage(youveKicked.replace("$player", target.getName()));

                core.skyblockManager.getIsland(uuid).coop.remove(target.getUniqueId().toString());
                core.skyblockManager.getIsland(uuid).save();

            } else if (args[0].equalsIgnoreCase("top")) {

                new IslandTopGUI(player);

            } else if (args[0].equalsIgnoreCase("help")) {

                if (args.length == 1) {

                    player.sendMessage(c("&cIsland Help: &6&m----------"));
                    player.sendMessage(c(" &eYou are viewing page: &b#1 &7(1/2)"));
                    player.sendMessage(c("  &e/is new:&b Creates a new island"));
                    player.sendMessage(c("  &e/is go: &b Teleport to your island"));
                    player.sendMessage(c("  &e/is visit <player>: &b Visit another player’s island"));
                    player.sendMessage(c("  &e/is list: &b List the members in your island."));
                    player.sendMessage(c("  &e/is show <player>: &b View information about an island"));
                    player.sendMessage(c("  &e/is accept <player>: &b Accept an island invite"));
                    player.sendMessage(c("  &e/is invite <player>: &b Invite a player to your island"));
                    player.sendMessage(c("  &e/is owner <player>: &b Grant owner to your island"));
                    player.sendMessage(c("  &e/is relevel: &b Calculates your island level"));

                } else {

                    if (args[1].equalsIgnoreCase("2")) {

                        player.sendMessage(c("&cIsland Help: &6&m----------"));
                        player.sendMessage(c("  &eYou are viewing page: &b#2 &7(2/2)"));
                        player.sendMessage(c("  &e/is help <page>: &b Displays this message"));
                        player.sendMessage(c("  &e/is top: &b Displays the top 10 islands"));
                        player.sendMessage(c("  &e/is ban <player>: &b Ban a member from your island"));
                        player.sendMessage(c("  &e/is unban <player>: &b Remove the ban of a player"));
                        player.sendMessage(c("  &e/is banned: &b Display who is banned off of your island"));
                        player.sendMessage(c("  &e/is kick <player>: &b Remove a member from your island"));
                        player.sendMessage(c("  &e/is leave: &b Leave your current island"));


                    } else if (args[1].equalsIgnoreCase("1")) {

                        player.sendMessage(c("&cIsland Help: &6&m----------"));
                        player.sendMessage(c(" &eYou are viewing page: &b#1 &7(1/2)"));
                        player.sendMessage(c("  &e/is new:&b Creates a new island"));
                        player.sendMessage(c("  &e/is go: &b Teleport to your island"));
                        player.sendMessage(ChatColor.YELLOW + "  /is visit <player>: " + ChatColor.AQUA + " Visit another player’s island");
                        player.sendMessage(c("  &e/is list: &b List the members in your island."));
                        player.sendMessage(c("  &e/is show <player>: &b View information about an island"));
                        player.sendMessage(c("  &e/is accept <player>: &b Accept an island invite"));
                        player.sendMessage(c("  &e/is invite <player>: &b Invite a player to your island"));
                        player.sendMessage(c("  &e/is relevel: &b Calculates your island level"));

                    } else {

                        player.sendMessage(invalidNumber);

                    }

                }

            }

            if (!player.hasPermission("Skyblock.staff") && !player.hasPermission("Skyblock.admin")) {

                return;

            }

            if (args[0].equalsIgnoreCase("tp")) {


                if (args.length < 2) {

                    player.sendMessage(specifyPlayer);

                    return;

                }

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                if (target == null || !target.hasPlayedBefore()) {

                    player.sendMessage(playerNotFound);
                    return;

                }

                if (!core.skyblockManager.hasIsland(target.getUniqueId())) {

                    player.sendMessage(playerDoesntHaveIsland);
                    return;

                }


                player.sendMessage(teleportedToIsland.replace("$player", target.getName()));
                player.teleport(core.skyblockManager.getIsland(target.getUniqueId()).spawn);

            }

            if (args[0].equalsIgnoreCase("remove") && player.hasPermission("skyblock.admin")) {

                if (args.length < 2) {

                    player.sendMessage(specifyPlayer);

                    return;

                }

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                if (target == null || !target.hasPlayedBefore()) {

                    player.sendMessage(playerNotFound);
                    return;

                }


                if (!core.skyblockManager.hasIsland(target.getUniqueId())) {

                    player.sendMessage(noIsland);
                    return;

                }

                core.skyblockManager.deleteIsland(target);

                player.sendMessage(deletedOtherIsland);

            } else if (args[0].equalsIgnoreCase("admin") && player.hasPermission("skyblock.admin")) {
                if (args.length < 2) {
                    player.sendMessage(new String[]{
                            "§c§lAdmin Controls",
                            "",
                            "§c/is admin setspawn - Set spawn point"
                    });
                    return;

                }
                if (args[1].equalsIgnoreCase("setspawn")) {
                    pl.getConfig().set("Island.delete.spawn_location.world", player.getLocation().getWorld().getName());
                    pl.getConfig().set("Island.delete.spawn_location.x", player.getLocation().getX());
                    pl.getConfig().set("Island.delete.spawn_location.y", player.getLocation().getY());
                    pl.getConfig().set("Island.delete.spawn_location.z", player.getLocation().getZ());
                    pl.saveConfig();
                    player.sendMessage("§a§l(!) §aSpawn set!");
                }
            }

        }

    }

}
