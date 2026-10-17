package com.monutchuan.onlymultiplayer;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

//纯客户端事件订阅：仅在客户端加载，监听GUI初始化完成事件。
@Mod.EventBusSubscriber(modid = OnlyMultiplayer.MODID, value = Side.CLIENT)
public class DisableSingleplayerButton
{
    //主菜单按钮的行距（原版每行相隔 24 像素）。
    private static final int ROW_HEIGHT = 24;

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
        switch (OnlyMultiplayer.mode)
        {
            case REMOVE:
                //移除按钮：直接从主介面移除，不显示“单人游戏”。
                //按钮组是绝对坐标排布的，单纯删掉会空出一整行，所以把下方的按钮整体上移一行。
                int removedY = singleplayer.y;
                event.getButtonList().remove(singleplayer);
                for (GuiButton button : event.getButtonList())
                {
                    //只动主菜单按钮组（同一列、且在被移除按钮下方几行内），避免影响右上角的图标按钮。
                    if (button.y > removedY && button.y <= removedY + ROW_HEIGHT * 4)
                        button.y -= ROW_HEIGHT;
                }
                break;
            case DISABLE:
                //禁用按钮：保留按钮但无法点击（enabled=false）。
                singleplayer.enabled = false;
                break;
        }
    }
}
