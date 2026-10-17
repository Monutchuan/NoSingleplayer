package com.monutchuan.onlymultiplayer;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod
(
        modid = OnlyMultiplayer.MODID,
        name = OnlyMultiplayer.NAME,
        version = OnlyMultiplayer.VERSION,
        clientSideOnly = true,
        acceptedMinecraftVersions = "[1.12.2]"
)
public class OnlyMultiplayer
{
    public static final String MODID = "onlymultiplayer";
    public static final String NAME = "OnlyMultiplayer";
    public static final String VERSION = "1.12.2-1.0.0";

    //对主菜单“单人游戏”按钮的处理方式，由“config/onlymultiplayer.cfg”控制。
    //REMOVE = 移除按钮（预设值，直接不显示）
    //DISABLE = 禁用按钮（不移除，但无法点击）
    public enum Mode {REMOVE, DISABLE}

    public static Mode mode = Mode.REMOVE;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
        cfg.load();
        String raw = cfg.getString
        (
                "mode",
                Configuration.CATEGORY_GENERAL,
                "remove",
                "How to handle the Singleplayer button: 'remove' = hide it entirely, 'disable' = grey it out but keep it visible."
        );

        try
        {
            mode = Mode.valueOf(raw.trim().toUpperCase());
        }
        catch (IllegalArgumentException e)
        {
            mode = Mode.REMOVE; //非法值回退到预设值
        }

        if (cfg.hasChanged())
            cfg.save();
    }
}
