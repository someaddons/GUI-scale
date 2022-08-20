package com.screenscale.mixin;

import com.screenscale.event.ClientEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin
{
    @Inject(method = "setScreen", at = @At("RETURN"))
    public void onCloseScreen(final Screen newscreen, final CallbackInfo ci)
    {
        if (newscreen == null)
        {
            ClientEventHandler.onClose();
        }
    }
}
