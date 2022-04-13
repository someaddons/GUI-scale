package com.screenscale.event;

import com.screenscale.ScreenScale;
import net.minecraft.client.CycleOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Option;
import net.minecraft.client.gui.screens.VideoSettingsScreen;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.client.event.ScreenOpenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ClientEventHandler
{
    public static final CycleOption MENU_SCALE = CycleOption.create("Menu Scale", () -> {
        return IntStream.rangeClosed(0, Minecraft.getInstance().getWindow().calculateScale(0, Minecraft.getInstance().isEnforceUnicode())).boxed().collect(Collectors.toList());
    }, (integer) -> {

        if (integer == 0)
        {
            return new TextComponent("Auto");
        }

        return new TextComponent("" + integer);
    }, (options) -> {
        return ScreenScale.config.getCommonConfig().menuScale.get();
    }, (options, option, value) -> {
        int guiScale = ScreenScale.config.getCommonConfig().menuScale.get();
        guiScale = value;
        ScreenScale.config.getCommonConfig().menuScale.set(guiScale);

        Minecraft.getInstance()
          .getWindow()
          .setGuiScale(guiScale != 0 ? guiScale : Minecraft.getInstance().getWindow().calculateScale(0, Minecraft.getInstance().isEnforceUnicode()));
        if (Minecraft.getInstance().screen != null)
        {
            Minecraft.getInstance().screen.resize(Minecraft.getInstance(),
              Minecraft.getInstance().getWindow().getGuiScaledWidth(),
              Minecraft.getInstance().getWindow().getGuiScaledHeight());
        }
    });
    static
    {
        try
        {
            final List<Option> options = new ArrayList<>(Arrays.asList(VideoSettingsScreen.OPTIONS));
            options.add(options.indexOf(Option.GUI_SCALE) + 1, MENU_SCALE);
            VideoSettingsScreen.OPTIONS = options.toArray(new Option[0]);
        }
        catch (Throwable e)
        {
            ScreenScale.LOGGER.error("Error trying to add an option Button to video settings, likely optifine is present which removes vanilla functionality required."
                                       + " The mod still works, but you'll need to manually adjust the config to get different UI scalings as the button could not be added.");
        }
    }

    static int oldScale = -1;

    @SubscribeEvent
    public static void on(ScreenOpenEvent event)
    {
        if (event.isCanceled())
        {
            return;
        }

        if (Minecraft.getInstance().screen == null && event.getScreen() != null)
        {
            Minecraft.getInstance()
              .getWindow()
              .setGuiScale(ScreenScale.config.getCommonConfig().menuScale.get() != 0
                             ? ScreenScale.config.getCommonConfig().menuScale.get()
                             : Minecraft.getInstance().getWindow().calculateScale(0, Minecraft.getInstance().isEnforceUnicode()));
            if (oldScale == -1)
            {
                oldScale = Minecraft.getInstance().options.guiScale;
                Minecraft.getInstance().options.guiScale = ScreenScale.config.getCommonConfig().menuScale.get();
            }
        }

        if (event.getScreen() == null)
        {
            if (oldScale != -1)
            {
                if (Minecraft.getInstance().options.guiScale == ScreenScale.config.getCommonConfig().menuScale.get())
                {
                    Minecraft.getInstance().options.guiScale = oldScale;
                }
                oldScale = -1;
            }
            Minecraft.getInstance().resizeDisplay();
        }
    }
}