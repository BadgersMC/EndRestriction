package org.atrimilan.paperplugintemplate.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * Example command to change the fly speed of a player
 */
public class FlySpeedCommand {

    public static final Set<String> ALIASES = Set.of("fspeed", "fs");

    private FlySpeedCommand() {
    }

    private static final FloatArgumentType flySpeedLimits = FloatArgumentType.floatArg(0f, 10f);

    /**
     * @return A {@code LiteralCommandNode} of the {@code /flyspeed} command, ready to be registered with a
     * {@code LifecycleEventManager}
     */
    public static LiteralCommandNode<CommandSourceStack> createCommand() {
        return Commands.literal("flyspeed")
                // flyspeed <speed>
                .then(Commands.argument("speed", flySpeedLimits) //
                        .executes(FlySpeedCommand::runFlySpeed))

                // flyspeed <player> <speed>
                .then(Commands.argument("player", ArgumentTypes.player())
                        .requires(sender -> sender.getSender().isOp()) // Require OP for this command
                        .then(Commands.argument("speed", flySpeedLimits) //
                                .executes(ctx -> {
                                    final PlayerSelectorArgumentResolver targetResolver = ctx.getArgument("player",
                                            PlayerSelectorArgumentResolver.class);
                                    final Player player = targetResolver.resolve(ctx.getSource()).getFirst();
                                    return runFlySpeed(ctx, player);
                                }))) //
                .build();
    }

    /**
     * Define the flight speed of the player executing the command
     */
    private static int runFlySpeed(CommandContext<CommandSourceStack> ctx) {
        Entity executor = ctx.getSource().getExecutor();

        if (!(executor instanceof Player player)) {
            CommandSender sender = ctx.getSource().getSender();
            sender.sendPlainMessage("Only players can fly!");
            return Command.SINGLE_SUCCESS;
        }
        return runFlySpeed(ctx, player);
    }

    /**
     * Define the flight speed of the selected player
     *
     * @param ctx    The command context
     * @param player The player to set the flight speed for
     */
    private static int runFlySpeed(CommandContext<CommandSourceStack> ctx, Player player) {
        float speed = FloatArgumentType.getFloat(ctx, "speed");
        CommandSender sender = ctx.getSource().getSender();
        Entity executor = ctx.getSource().getExecutor();

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
