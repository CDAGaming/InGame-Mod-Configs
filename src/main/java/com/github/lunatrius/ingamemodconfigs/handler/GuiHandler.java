package com.github.lunatrius.ingamemodconfigs.handler;


import com.github.lunatrius.ingamemodconfigs.reference.Reference;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.GuiIngameModOptions;
import cpw.mods.fml.client.GuiModList;
import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiOpenEvent;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

public class GuiHandler {
    @SubscribeEvent
    public void onGuiOpen(final GuiOpenEvent event) {
        if (event.gui instanceof GuiIngameModOptions) {
            event.gui = new GuiModConfigList(new GuiIngameMenu());
        }
    }

    private static class GuiModConfigList extends GuiModList {
        public GuiModConfigList(final GuiScreen screen) {
            super(screen);

            try {
                final Field fieldMods = ReflectionHelper.findField(GuiModList.class, "mods");
                final List<ModContainer> mods = (List<ModContainer>) fieldMods.get(this);
                final Iterator<ModContainer> iterator = mods.iterator();
                while (iterator.hasNext()) {
                    final ModContainer mod = iterator.next();
                    final IModGuiFactory guiFactory = FMLClientHandler.instance().getGuiFactoryFor(mod);
                    if (guiFactory == null || guiFactory.mainConfigGuiClass() == null) {
                        iterator.remove();
                    }
                }
            } catch (Exception e) {
                Reference.logger.error("Failed to tweak mod list!", e);
            }
        }
    }
}
