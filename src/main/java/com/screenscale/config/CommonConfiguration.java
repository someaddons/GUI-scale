package com.screenscale.config;

import com.cupboard.config.ICommonConfig;
import com.google.gson.JsonObject;

public class CommonConfiguration implements ICommonConfig
{
    public int menuScale = 0;

    public CommonConfiguration()
    {

    }

    public JsonObject serialize()
    {
        final JsonObject root = new JsonObject();

        final JsonObject entry = new JsonObject();
        entry.addProperty("desc:", "UI scale of menus, default = 0(Auto)");
        entry.addProperty("menuScale", menuScale);
        root.add("menuScale", entry);

        return root;
    }

    public void deserialize(JsonObject data)
    {
        menuScale = data.get("menuScale").getAsJsonObject().get("menuScale").getAsInt();
    }
}
