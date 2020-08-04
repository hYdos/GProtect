package io.github.hydos.gprotect;

import io.github.hydos.gprotect.config.BlockChangeListener;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

public class GProtect implements ModInitializer {

    public static final String VERSION = "0.0.1";

    @Override
    public void onInitialize() {
        registerCommands();
    }

    public void registerCommands() {
        CommandRegistrationCallback.EVENT.register((commandDispatcher, b) -> CommandRegistry.register(commandDispatcher));
        new BlockChangeListener.MySQLWriter(); //test db
    }
}
