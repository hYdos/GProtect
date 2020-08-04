package io.github.hydos.gprotect;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.command.arguments.MessageArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.LiteralText;

import static net.minecraft.util.Formatting.*;

public class GProtect implements ModInitializer {

    public static final String VERSION = "0.0.1";

    @Override
    public void onInitialize() {
        registerCommands();
    }

    public void registerCommands() {
//        .then(CommandManager.argument("message", MessageArgumentType.message())
        CommandRegistrationCallback.EVENT.register((commandDispatcher, b) -> CommandRegistry.register(commandDispatcher));
    }
}
