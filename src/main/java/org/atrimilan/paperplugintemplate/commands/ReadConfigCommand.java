package org.atrimilan.paperplugintemplate.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.atrimilan.paperplugintemplate.utils.ConfigManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Set;

/**
 * Example command that reads config.yml
 */
public class ReadConfigCommand {

    public static final String DESCRIPTION = "Reads config.yml";

    public static final Set<String> ALIASES = Set.of("rc");

    private static final String CONFIG_PATH = "example.";

    private ReadConfigCommand() {
    }

    /**
     * @return A {@code LiteralCommandNode} of the {@code /read-config} command, ready to be registered with a
     * {@code LifecycleEventManager}
     */
    public static LiteralCommandNode<CommandSourceStack> createCommand() {
        return Commands.literal("read-config") //
                .requires(sender -> sender.getSender().hasPermission("testplugin.readconfig"))
                .then(Commands.literal("ultimate-answer").executes(ReadConfigCommand::readUltimateAnswer))
                .then(Commands.literal("pangram").executes(ReadConfigCommand::readSentence))
                .then(Commands.literal("boolean").executes(ReadConfigCommand::readBoolean))
                .then(Commands.literal("player").executes(ReadConfigCommand::readPlayer)).build();
    }

    private static int readUltimateAnswer(CommandContext<CommandSourceStack> ctx) {
        FileConfiguration config = ConfigManager.getConfig();
        ctx.getSource().getSender().sendRichMessage(
                "The answer to the Ultimate Question of Life, the Universe, and Everything is: <dark_red><bold>" + //
                        config.getInt(CONFIG_PATH + "ultimate-answer"));

        return Command.SINGLE_SUCCESS;
    }

    private static int readSentence(CommandContext<CommandSourceStack> ctx) {
        FileConfiguration config = ConfigManager.getConfig();
        ctx.getSource().getSender().sendPlainMessage("Pangram: " + config.getString(CONFIG_PATH + "pangram"));

        return Command.SINGLE_SUCCESS;
    }

    private static int readBoolean(CommandContext<CommandSourceStack> ctx) {
        FileConfiguration config = ConfigManager.getConfig();
        ctx.getSource().getSender()
                .sendRichMessage("Isn't this a Boolean: <green>" + config.getBoolean(CONFIG_PATH + "boolean"));

        return Command.SINGLE_SUCCESS;
    }

    private static int readPlayer(CommandContext<CommandSourceStack> ctx) {
        FileConfiguration config = ConfigManager.getConfig();
        ConfigurationSection player = config.getConfigurationSection(CONFIG_PATH + "player");

        if (player != null) {
            String uuid = player.getString("uuid");
            if (uuid != null) {
                ctx.getSource().getSender().sendRichMessage( //
                        "Player with UUID <gold><uuid></gold> is located at <aqua><x> <y> <z></aqua>",
                        Placeholder.component("uuid", Component.text(uuid)),
                        Placeholder.component("x", Component.text(player.getDouble("location.x"))),
                        Placeholder.component("y", Component.text(player.getDouble("location.y"))),
                        Placeholder.component("z", Component.text(player.getDouble("location.z"))));
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}
