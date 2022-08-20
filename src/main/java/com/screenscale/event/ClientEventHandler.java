package com.screenscale.event;

import com.screenscale.ScreenScale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Tuple;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class ClientEventHandler
{
    public static final OptionInstance<Integer> MENU_SCALE = new OptionInstance<>("Menu Scale", OptionInstance.noTooltip(),
      (component, value) ->
      {
          return value == 0 ? Component.translatable("options.guiScale.auto") : Component.literal(Integer.toString(value));
      },
      new OptionInstance.ClampingLazyMaxIntRange(0, () ->
      {
          Minecraft minecraft = Minecraft.getInstance();
          return !minecraft.isRunning() ? 2147483646 : minecraft.getWindow().calculateScale(0, minecraft.isEnforceUnicode());
      }),
      ScreenScale.config.getCommonConfig().menuScale.get(),
      (value) ->
      {
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

    private static List<Tuple<Integer, Integer>> changes = new ArrayList<>();

    @SubscribeEvent
    public static void on(ScreenEvent.Opening event)
    {
        if (event.isCanceled())
        {
            return;
        }

        if (event.getCurrentScreen() == null && event.getNewScreen() != null)
        {
            changes.add(new Tuple<>(Minecraft.getInstance().options.guiScale().get(), ScreenScale.config.getCommonConfig().menuScale.get()));
            //ScreenScale.LOGGER.info("Changing from "+Minecraft.getInstance().options.guiScale().get()+" to "+ScreenScale.config.getCommonConfig().menuScale.get());

            Minecraft.getInstance()
              .getWindow()
              .setGuiScale(ScreenScale.config.getCommonConfig().menuScale.get() != 0
                             ? ScreenScale.config.getCommonConfig().menuScale.get()
                             : Minecraft.getInstance().getWindow().calculateScale(0, Minecraft.getInstance().isEnforceUnicode()));

            Minecraft.getInstance().options.guiScale().set(ScreenScale.config.getCommonConfig().menuScale.get());
            Minecraft.getInstance().resizeDisplay();
        }
    }

    public static void onClose()
    {
        if (!changes.isEmpty())
        {
            for (int i = changes.size() - 1; i >= 0; i--)
            {
                final Tuple<Integer, Integer> change = changes.get(i);
                if (Minecraft.getInstance().options.guiScale().get().equals(change.getB()))
                {
                    //ScreenScale.LOGGER.info("Restoring from " + change.getB() + " to " + change.getA());
                    Minecraft.getInstance().options.guiScale().set(change.getA());
                }
            }
            changes.clear();
            Minecraft.getInstance().resizeDisplay();
        }
    }
}