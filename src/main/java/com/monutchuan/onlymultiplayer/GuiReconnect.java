package com.monutchuan.onlymultiplayer;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

//连接失败或掉线后的界面：重试，或者直接退出游戏。
public class GuiReconnect extends GuiScreen
{
    @Override
    public void initGui()
    {
        this.buttonList.clear();

        int top = this.height / 2 + 24;
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, top, 98, 20,
                I18n.format("onlymultiplayer.gui.reconnect")));
        this.buttonList.add(new GuiButton(0, this.width / 2 + 2, top, 98, 20,
                I18n.format("onlymultiplayer.gui.quit")));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.id == 1)
            AutoConnect.connect(this.mc);
        else if (button.id == 0)
            this.mc.shutdown();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();

        int centerX = this.width / 2;
        this.drawCenteredString(this.fontRenderer, I18n.format("onlymultiplayer.gui.disconnected"),
                centerX, this.height / 2 - 64, 0xFFFFFF);

        String address = OnlyMultiplayer.serverHost + ":" + OnlyMultiplayer.serverPort;
        this.drawCenteredString(this.fontRenderer, address, centerX, this.height / 2 - 44, 0xA0A0A0);
        this.drawCenteredString(this.fontRenderer, I18n.format("onlymultiplayer.gui.hint"),
                centerX, this.height / 2 - 26, 0xA0A0A0);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
