package com.oscimate.oscimate_soulflame;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.text.TranslatableText;

public class ModMenuApiImpl implements ModMenuApi {

        @Override
        public ConfigScreenFactory<?> getModConfigScreenFactory() {
                return parent -> new ConfigScreen(new TranslatableText("ImprovedFireOverlay"));
        }
}