package ml.denisd3d.mc2discord.forge.mixin.core;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import ml.denisd3d.mc2discord.core.M2DUtils;
import ml.denisd3d.mc2discord.core.Mc2Discord;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.commands.EmoteCommands;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EmoteCommands.class)
public class MixinEmoteCommand {
    @SuppressWarnings("target")
    @Inject(method = {"lambda$register$2(Lcom/mojang/brigadier/context/CommandContext;)I", "func_198365_a(Lcom/mojang/brigadier/context/CommandContext;)I"}, at = @At("RETURN"), remap = false)
    private static void lambda(CommandContext<CommandSourceStack> commandContext, CallbackInfoReturnable<Integer> ci) {
        if (Mc2Discord.INSTANCE.config.misc.relay_say_me_command && M2DUtils.canHandleEvent()) {
            TranslatableComponent translatableComponent = new TranslatableComponent("chat.type.emote",
                    commandContext.getSource().getDisplayName(), StringArgumentType.getString(commandContext, "action"));
            Mc2Discord.INSTANCE.messageManager.sendInfoMessage(translatableComponent.getString());
        }
    }
}