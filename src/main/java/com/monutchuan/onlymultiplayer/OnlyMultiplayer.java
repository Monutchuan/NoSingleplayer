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
    //显示名称带空格，和 Mod 列表里其他 Mod 的写法保持一致；modid 仍然必须是小写且无空格。
    public static final String NAME = "Only Multiplayer";
    //Mod 列表里显示成“Version: 1.0.0 (1.0.0)”；jar 文件名仍由 build.gradle 的 version 决定。
    public static final String VERSION = "1.0.0";

    //对主菜单“单人游戏”按钮的处理方式，由“config/onlymultiplayer.cfg”控制。
    //REMOVE = 移除按钮（预设值，直接不显示）
    //DISABLE = 禁用按钮（不移除，但无法点击）
    public enum Mode {REMOVE, DISABLE}

    //服务器资源包的处理方式。
    //ALLOW = 直接接受，PROMPT = 询问玩家，DENY = 直接拒绝
    public enum ResourcePackPolicy {ALLOW, PROMPT, DENY}

    public static Mode mode = Mode.REMOVE;

    //进入游戏后自动连接服务器（共用设备、白嫖机等场景）。
    public static boolean autoConnect = false;
    public static String serverHost = "";
    public static int serverPort = 25565;
    public static ResourcePackPolicy resourcePack = ResourcePackPolicy.PROMPT;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
        cfg.load();

        String rawMode = cfg.getString
        (
                "mode",
                Configuration.CATEGORY_GENERAL,
                "remove",
                "How to handle the Singleplayer button: 'remove' = hide it entirely, 'disable' = grey it out but keep it visible."
        );

        try
        {
            mode = Mode.valueOf(rawMode.trim().toUpperCase());
        }
        catch (IllegalArgumentException e)
        {
            mode = Mode.REMOVE; //非法值回退到预设值
        }

        autoConnect = cfg.getBoolean
        (
                "autoConnect",
                Configuration.CATEGORY_GENERAL,
                false,
                "If true, join 'serverAddress' as soon as the main menu loads."
        );

        parseAddress
        (
                cfg.getString
                (
                        "serverAddress",
                        Configuration.CATEGORY_GENERAL,
                        "",
                        "Server to join on startup, written as 'host' or 'host:port'. The port defaults to 25565."
                )
        );

        String rawPack = cfg.getString
        (
                "resourcePack",
                Configuration.CATEGORY_GENERAL,
                "prompt",
                "Server resource packs: 'allow' = accept automatically, 'prompt' = ask the player, 'deny' = refuse."
        );

        try
        {
            resourcePack = ResourcePackPolicy.valueOf(rawPack.trim().toUpperCase());
        }
        catch (IllegalArgumentException e)
        {
            resourcePack = ResourcePackPolicy.PROMPT; //非法值回退到询问
        }

        if (cfg.hasChanged())
            cfg.save();
    }

    //自动连接是否真的可用：开关打开，且地址不为空。
    public static boolean hasServer()
    {
        return autoConnect && !serverHost.isEmpty();
    }

    //解析“主机”或“主机:端口”，端口缺失或非法时整串按主机名处理。
    private static void parseAddress(String raw)
    {
        serverHost = "";
        serverPort = 25565;

        if (raw == null || raw.trim().isEmpty())
            return;

        String text = raw.trim();
        int colon = text.lastIndexOf(':');
        if (colon > 0 && colon < text.length() - 1)
        {
            try
            {
                int parsed = Integer.parseInt(text.substring(colon + 1));
                if (parsed > 0 && parsed <= 65535)
                {
                    serverHost = text.substring(0, colon);
                    serverPort = parsed;
                    return;
                }
            }
            catch (NumberFormatException ignored)
            {
                //不是合法端口，落到下面按主机名处理
            }
        }

        serverHost = text;
    }
}
