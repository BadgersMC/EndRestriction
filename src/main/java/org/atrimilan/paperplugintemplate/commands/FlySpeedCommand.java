package org.atrimilan.paperplugintemplate.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Set;

public class FlySpeedCommand {

    public static final Set<String> ALIASES = Set.of("fspeed", "fs");

    /**
     * @return A {@code LiteralCommandNode} of the {@code /flyspeed} command, ready to be registered with a
     * {@code LifecycleEventManager}
     */
    public static LiteralCommandNode<CommandSourceStack> createCommand() {
        return Commands.literal("flyspeed") //
                .then(Commands.argument("speed", FloatArgumentType.floatArg(0, 10f))
                        .executes(FlySpeedCommand::runFlySpeed)) //
                .build();
    }

    /**
     * Define the flight speed of the player executing the command
     */
    private static int runFlySpeed(CommandContext<CommandSourceStack> ctx) {
        float speed = FloatArgumentType.getFloat(ctx, "speed");
        CommandSender sender = ctx.getSource().getSender();
        Entity executor = ctx.getSource().getExecutor();

        if (!(executor instanceof Player player)) {
            sender.sendPlainMessage("Only players can fly!");
            return Command.SINGLE_SUCCESS;
        }

        player.setFlySpeed(speed / 10f);

        if (sender == executor) {
            player.sendPlainMessage("Successfully set your flight speed to " + speed);
            return Command.SINGLE_SUCCESS;
        }

        sender.sendRichMessage("Successfully set <playername>'s flight speed to " + speed,
                Placeholder.component("playername", player.name()));
        player.sendPlainMessage(sender.getName() + " has set your flight speed to " + speed);
        return Command.SINGLE_SUCCESS;
    }
}
