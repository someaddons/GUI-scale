package com.screenscale.mixin;

import com.screenscale.event.ClientEventHandler;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.VideoSettingsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VideoSettingsScreen.class)
public class VideoSettingsScreenMixin
{
    @Inject(method = "options", at = @At(value = "RETURN"), cancellable = true)
    private static void on(final Options options, final CallbackInfoReturnable<OptionInstance<?>[]> cir)
    {
        final OptionInstance<?>[] original = cir.getReturnValue();
        final OptionInstance<?>[] copyplusone = new OptionInstance<?>[original.length + 1];

        int copyindex = 0;
        for (int i = 0; i < original.length; i++)
        {
            final OptionInstance optionInstance = original[i];
            copyplusone[copyindex] = optionInstance;
            if (optionInstance == options.guiScale())
            {
                copyplusone[copyindex + 1] = ClientEventHandler.MENU_SCALE;
                copyindex++;
            }

            copyindex++;
        }
        cir.setReturnValue(copyplusone);
    }
}
