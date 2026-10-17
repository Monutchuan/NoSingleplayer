package com.monutchuan.onlymultiplayer;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;

//自动进服时的连接界面。
//原版这一屏只有“取消”，点下去会原样退回上一层界面（多人游戏列表），玩家再从列表返回就能回到主菜单，
//共用设备场景里等于留了一扇后门。所以这里把唯一的按钮改成“退出游戏”。
public class GuiServerConnecting extends GuiConnecting
{
    public GuiServerConnecting(Minecraft mc, ServerData data)
    {
        //父界面仍给主菜单：连接失败时原版会把它交给断开界面，而断开界面上的按钮由 AutoConnect 接管。
        super(new GuiMainMenu(), mc, data);
    }

    @Override
    public void initGui()
    {
        super.initGui();

        //原版的“取消”按钮 id 固定为 0。
        for (GuiButton button : this.buttonList)
        {
            if (button.id == 0)
                button.displayString = I18n.format("menu.quit");
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        //不调用 super：super 会先置取消标记再切回上一层界面，这里直接结束游戏。
        //shutdown() 内部就是 System.exit，正在握手的连接线程会随进程一起结束。
        if (button.id == 0)
            this.mc.shutdown();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        //原版这一屏的 keyTyped 是空的，Esc 本来就没有反应；这里显式留空，免得以后有人加回“返回”。
    }
}
