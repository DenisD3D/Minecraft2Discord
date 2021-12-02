package ml.denisd3d.mc2discord.forge.account;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import ml.denisd3d.mc2discord.core.Mc2Discord;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;

public class LinkCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        String[] strings = Mc2Discord.INSTANCE.config.account.link_command.trim().split(" ");

        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal(strings[strings.length - 1]).executes(context -> {
            if (!Mc2Discord.INSTANCE.config.account.link_command.trim().equals(context.getInput().substring(1))) {
                context.getSource().sendFailure(new TextComponent("Mc2Discord settings have changed. Please use /" + Mc2Discord.INSTANCE.config.account.link_command.trim() + " instead. This command will be removed at next server restart"));
            } else {
                ServerPlayer player = context.getSource().getPlayerOrException();
                if (Mc2Discord.INSTANCE.m2dAccount != null) { // Should always be the case at this point
                    String result = Mc2Discord.INSTANCE.m2dAccount.generateCodeOrNull(player.getGameProfile(), player.getGameProfile().getId());
                    if (result != null) {
                        context.getSource().sendSuccess(new TextComponent(result), false);
                    } else {
                        context.getSource().sendFailure(new TextComponent(Mc2Discord.INSTANCE.config.account.messages.link_error_already));
                    }
                }
            }
            return 1;
        });
        for (int i = strings.length - 2; i >= 0; i--) {
            command = Commands.literal(strings[i]).then(command);
        }
        dispatcher.register(command);
    }
}