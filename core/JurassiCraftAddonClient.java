package com.extviljuraaddon.core;

import net.minecraft.entity.passive.EntityVillager;

import com.extvil.extendedvillages.evcore.handler.ConfigHandler;
import com.extvil.extendedvillages.evvillager.RenderExtendedVillager;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class JurassiCraftAddonClient extends JurassiCraftAddonProxy
{
    public void registerRenderInformation()
    {
    	if(ConfigHandler.CustomRender)
    	RenderingRegistry.registerEntityRenderingHandler(EntityVillager.class, new RenderExtendedVillager());
    }
}
