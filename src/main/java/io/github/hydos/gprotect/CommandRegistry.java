package io.github.hydos.gprotect;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

import static net.minecraft.util.Formatting.*;

public class CommandRegistry {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("gprotect").requires((serverCommandSource) -> serverCommandSource.hasPermissionLevel(1)).executes((commandContext) -> {
            PlayerEntity entity = (PlayerEntity) commandContext.getSource().getEntity();
            if (entity != null) {
                entity.sendMessage(new LiteralText("----" + DARK_GREEN + "G Protect" + RESET + "----"), false);
                entity.sendMessage(new LiteralText(GOLD + "/gprotect history" + DARK_AQUA + " <player>"), false);
                entity.sendMessage(new LiteralText(GOLD + "/gprotect region" + DARK_AQUA + " <...>"), false);
                entity.sendMessage(new LiteralText("-----------------"), false);
                entity.sendMessage(new LiteralText(AQUA + "Version " + GProtect.VERSION), true);
            }
            return 1;
        }));

        dispatcher.register(CommandManager.literal("history").requires((serverCommandSource) -> serverCommandSource.hasPermissionLevel(2)).then(CommandManager.argument("player", EntityArgumentType.player()).executes((commandContext) -> {
            PlayerEntity sender = (PlayerEntity) commandContext.getSource().getEntity();
            PlayerEntity specifiedPlayer = commandContext.getArgument("player", PlayerEntity.class);
            if (specifiedPlayer != null) {
            }else{
                sender.sendMessage(new LiteralText(RED + "Please specify a valid player."), false);
            }
            return 1;
        })));
    }

}
