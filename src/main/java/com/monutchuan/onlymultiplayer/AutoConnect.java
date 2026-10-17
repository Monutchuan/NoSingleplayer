package com.monutchuan.onlymultiplayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

//自动进服：主菜单出现后直接连服务器；连不上或被断开时，换成“重试 / 退出游戏”界面。
@Mod.EventBusSubscriber(modid = OnlyMultiplayer.MODID, value = Side.CLIENT)
public class AutoConnect
{
    //只从主菜单自动连一次；重试走 GuiReconnect 自己的按钮，避免反复触发。
    private static boolean attempted = false;

    @SubscribeEvent
    public static void onMainMenuInit(GuiScreenEvent.InitGuiEvent.Post event)
    {
        if (attempted || !OnlyMultiplayer.hasServer())
            return;
        if (!(event.getGui() instanceof GuiMainMenu))
            return;

        attempted = true;
        final Minecraft mc = Minecraft.getMinecraft();

        //等这一帧的界面初始化结束再切屏，避免在 initGui 里替换掉正在初始化的界面。
        mc.addScheduledTask(new Runnable()
        {
            @Override
            public void run()
            {
                connect(mc);
            }
        });
    }

    @SubscribeEvent
    public static void onGuiOpen(GuiOpenEvent event)
    {
        if (!OnlyMultiplayer.hasServer())
            return;

        GuiScreen gui = event.getGui();
        //连接失败和游戏中断线都会打开 GuiDisconnected，这里统一接管。
        if (gui instanceof GuiDisconnected)
            event.setGui(new GuiReconnect());
    }

    //连接服务器；“重试”按钮也走这里。
    public static void connect(Minecraft mc)
    {
        String address = OnlyMultiplayer.serverHost + ":" + OnlyMultiplayer.serverPort;

        //用 ServerData 连接，资源包策略就能绑在这次连接上，不必去改客户端的全局设置。
        ServerData data = new ServerData(address, address, false);
        data.setResourceMode(toResourceMode(OnlyMultiplayer.resourcePack));

        mc.displayGuiScreen(new GuiConnecting(new GuiMainMenu(), mc, data));
    }

    private static ServerData.ServerResourceMode toResourceMode(OnlyMultiplayer.ResourcePackPolicy policy)
    {
        switch (policy)
        {
            case ALLOW:
                return ServerData.ServerResourceMode.ENABLED;
            case DENY:
                return ServerData.ServerResourceMode.DISABLED;
            default:
                return ServerData.ServerResourceMode.PROMPT;
        }
    }
}
