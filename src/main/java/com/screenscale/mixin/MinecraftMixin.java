package com.screenscale.mixin;

import com.screenscale.event.ClientEventHandler;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin
{
    @Inject(method = "resizeDisplay", at = @At("HEAD"))
    public void onResize(final CallbackInfo ci)
    {
        ClientEventHandler.oldScale = -1;
    }
}
