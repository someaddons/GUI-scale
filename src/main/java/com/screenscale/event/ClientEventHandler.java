package com.screenscale.event;

import com.screenscale.ScreenScale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ClientEventHandler
{
    public static final OptionInstance<Integer> MENU_SCALE = new OptionInstance<Integer>("Menu Scale", OptionInstance.noTooltip(),
      (component, value) ->
      {
          return value == 0 ? Component.translatable("options.guiScale.auto") : Component.literal(Integer.toString(value));
      },
      new OptionInstance.ClampingLazyMaxIntRange(0, () ->
      {
          Minecraft minecraft = Minecraft.getInstance();
          return !minecraft.isRunning() ? 2147483646 : minecraft.getWindow().calculateScale(0, minecraft.isEnforceUnicode());
      }, 2147483646),
      ScreenScale.config.getCommonConfig().menuScale,
      (value) ->
      {
          int guiScale = value;
          ScreenScale.config.getCommonConfig().menuScale = guiScale;

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

    public static void onScreenSet(Screen screen)
    {
        if (Minecraft.getInstance().screen == null && screen != null)
        {
            Minecraft.getInstance()
              .getWindow()
              .setGuiScale(ScreenScale.config.getCommonConfig().menuScale != 0
                             ? ScreenScale.config.getCommonConfig().menuScale
                             : Minecraft.getInstance().getWindow().calculateScale(0, Minecraft.getInstance().isEnforceUnicode()));
        }

        if (screen == null)
        {
            Minecraft.getInstance().resizeDisplay();
        }
    }
}