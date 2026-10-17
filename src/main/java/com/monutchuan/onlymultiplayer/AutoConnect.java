package com.monutchuan.onlymultiplayer;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

//自动进服：主菜单一出现就换成“正在连接”界面；连不上或被断开时，把原版的“返回”换成“重试 / 退出游戏”。
@Mod.EventBusSubscriber(modid = OnlyMultiplayer.MODID, value = Side.CLIENT)
public class AutoConnect
{
    //“重试”“退出游戏”用固定 id，和原版按钮错开。
    private static final int BUTTON_RETRY = 9001;
    private static final int BUTTON_QUIT = 9002;

    //只自动连一次；之后玩家再回到主菜单就不抢屏了。
    private static boolean attempted = false;

    @SubscribeEvent
    public static void onGuiOpen(GuiOpenEvent event)
    {
        if (attempted || !OnlyMultiplayer.hasServer())
            return;
        if (!(event.getGui() instanceof GuiMainMenu))
            return;

        //直接换成过渡界面，主菜单连一帧都不显示，玩家开机后看到的第一屏就是连接状态。
        attempted = true;
        event.setGui(new GuiAutoConnecting());
    }

    @SubscribeEvent
    public static void onGuiInit(GuiScreenEvent.InitGuiEvent.Post event)
    {
        GuiScreen gui = event.getGui();
        if (!OnlyMultiplayer.hasServer() || !(gui instanceof GuiDisconnected))
            return;

        //原版断开界面只有一个“返回”按钮，这里换成“重试 / 退出游戏”。
        //断开原因由原版自己绘制，换掉界面就等于把服务器给的提示一起丢了，所以这里只动按钮。
        List<GuiButton> buttons = event.getButtonList();
        GuiButton back = null;
        for (GuiButton button : buttons)
        {
            if (button.id == 0)
            {
                back = button;
                break;
            }
        }
        if (back == null)
            return;

        buttons.remove(back);
        int y = back.y;
        buttons.add(new GuiButton(BUTTON_RETRY, gui.width / 2 - 100, y, 98, 20,
                OnlyMultiplayer.tr("onlymultiplayer.gui.reconnect", "重试", "Retry")));
        buttons.add(new GuiButton(BUTTON_QUIT, gui.width / 2 + 2, y, 98, 20,
                I18n.format("menu.quit")));
    }

    @SubscribeEvent
    public static void onActionPre(GuiScreenEvent.ActionPerformedEvent.Pre event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        GuiScreen gui = event.getGui();
        GuiButton button = event.getButton();

        if (gui instanceof GuiDisconnected)
        {
            if (button.id == BUTTON_RETRY)
            {
                event.setCanceled(true);
                connect(mc);
            }
            else if (button.id == BUTTON_QUIT)
            {
                event.setCanceled(true);
                mc.shutdown();
            }
            return;
        }

        //暂停菜单里的“断开连接”（id 1）是玩家主动退出：原来会回到多人游戏列表，这里改成直接结束游戏。
        //单人存档仍交给原版处理，免得存档没保存。
        if (gui instanceof GuiIngameMenu && button.id == 1
                && OnlyMultiplayer.hasServer() && !mc.isIntegratedServerRunning())
        {
            event.setCanceled(true);
            leaveAndExit(mc);
        }
    }

    //主动断开：先走一遍原版的断线流程，再退出，免得服务器那边把玩家挂着。
    private static void leaveAndExit(Minecraft mc)
    {
        if (mc.world != null)
            mc.world.sendQuittingDisconnectingPacket();
        mc.loadWorld(null);
        mc.shutdown();
    }

    //连接服务器；“重试”按钮也走这里。
    public static void connect(Minecraft mc)
    {
        String address = OnlyMultiplayer.serverHost + ":" + OnlyMultiplayer.serverPort;

        //用 ServerData 连接，资源包策略就能绑在这次连接上，不必去改客户端的全局设置。
        ServerData data = new ServerData(address, address, false);
        data.setResourceMode(toResourceMode(OnlyMultiplayer.resourcePack));

        //用自己的一层：原版连接界面的“取消”会退回服务器列表，再一步就是主菜单。
        mc.displayGuiScreen(new GuiServerConnecting(mc, data));
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
