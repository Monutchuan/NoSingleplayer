package com.monutchuan.nosingleplayer;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

//纯客户端事件订阅：仅在客户端加载，监听GUI初始化完成事件。
@Mod.EventBusSubscriber(modid = NoSingleplayer.MODID, value = Side.CLIENT)
public class DisableSingleplayerButton
{

    @SubscribeEvent
    public static void onGuiInit(GuiScreenEvent.InitGuiEvent.Post event)
    {
        //处理主菜单（GuiMainMenu）上“单人游戏”的按钮，一般单人游戏只能在这里进入。
        if (!(event.getGui() instanceof GuiMainMenu))
            return;

        GuiButton singleplayer = null;
        for (GuiButton button : event.getButtonList())
        {
            //用翻译键“menu.singleplayer”进行比对，避免不同语言环境下匹配失败。
            if (button.displayString.equals(I18n.format("menu.singleplayer")))
            {
                singleplayer = button;
                break;
            }
        }
        if (singleplayer == null)
            return;

        //按配置的模式处理按钮。
        switch (NoSingleplayer.mode)
        {
            case REMOVE:
                //移除按钮：直接从主介面移除，不显示“单人游戏”。
                event.getButtonList().remove(singleplayer);
                break;
            case DISABLE:
                //禁用按钮：保留按钮但无法点击（enabled=false）。
                singleplayer.enabled = false;
                break;
        }
    }
}
