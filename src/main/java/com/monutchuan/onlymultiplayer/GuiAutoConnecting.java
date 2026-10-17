package com.monutchuan.onlymultiplayer;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

//启动时的过渡界面：主菜单被它顶掉，玩家打开游戏后看到的第一屏就是“正在连接 …”。
//它只负责把界面立刻画出来，真正的连接交给下一帧，免得在 initGui 里换屏。
public class GuiAutoConnecting extends GuiScreen
{
    private boolean started = false;

    @Override
    public void initGui()
    {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 + 30, 200, 20,
                I18n.format("menu.quit")));

        if (started)
            return;
        started = true;

        Minecraft.getMinecraft().addScheduledTask(new Runnable()
        {
            @Override
            public void run()
            {
                //玩家可能在下一帧之前就按了 Esc 或点了退出，这时不要再把他拽去做连接。
                if (Minecraft.getMinecraft().currentScreen == GuiAutoConnecting.this)
                    AutoConnect.connect(Minecraft.getMinecraft());
            }
        });
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.id == 0)
            this.mc.shutdown();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();

        int centerX = this.width / 2;
        this.drawCenteredString(this.fontRenderer, I18n.format("connect.connecting"),
                centerX, this.height / 2 - 50, 0xFFFFFF);
        this.drawCenteredString(this.fontRenderer,
                OnlyMultiplayer.serverHost + ":" + OnlyMultiplayer.serverPort,
                centerX, this.height / 2 - 30, 0xA0A0A0);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
