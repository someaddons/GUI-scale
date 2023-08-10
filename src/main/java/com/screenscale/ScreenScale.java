package com.screenscale;

import com.cupboard.config.CupboardConfig;
import com.screenscale.config.CommonConfiguration;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

// The value here should match an entry in the META-INF/mods.toml file
public class ScreenScale implements ModInitializer
{
    public static final String                               MODID        = "guiscale";
    public static final Logger                              LOGGER = LogManager.getLogger();
    public static       CupboardConfig<CommonConfiguration> config = new CupboardConfig<>(MODID, new CommonConfiguration());
    public static       Random                              rand   = new Random();

    public ScreenScale()
    {
    }

    @Override
    public void onInitialize()
    {
        LOGGER.info(MODID + " mod initialized");
    }
}
