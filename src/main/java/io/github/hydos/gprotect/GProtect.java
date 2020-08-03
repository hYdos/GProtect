package io.github.hydos.gprotect;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.arguments.MessageArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.LiteralText;

public class GProtect implements ModInitializer {

    public static final String VERSION = "0.0.1";

    @Override
    public void onInitialize() {
        registerCommands();
    }

    public void registerCommands(){
        CommandRegistrationCallback.EVENT.register((dispatcher, b) -> dispatcher.register(CommandManager.literal("gprotect").requires((serverCommandSource) -> serverCommandSource.hasPermissionLevel(2)).then(CommandManager.argument("message", MessageArgumentType.message()).executes((commandContext) -> {
            PlayerEntity entity = (PlayerEntity) commandContext.getSource().getEntity();
            if (entity != null) {
                entity.sendMessage(new LiteralText("----G Protect----"), false);
                entity.sendMessage(new LiteralText("-----------------"), false);
                entity.sendMessage(new LiteralText("Version " + VERSION), true);
            }else{
                //the only way this could run is if its executed from console. i hope so atleast
            }
            return 1;
        }))));
    }
}
