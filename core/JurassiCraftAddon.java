package com.extviljuraaddon.core;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.structure.MapGenStructureIO;

import com.extvil.extendedvillages.evcore.ExtendedVillages;
import com.extvil.extendedvillages.evcore.handler.ConfigHandler;
import com.extviljuraaddon.worldgen.components.ComponentPalLab;
import com.extviljuraaddon.worldgen.structurehandlers.PalLabHandler;
import com.extviljuraaddon.worldgen.tradehandlers.VillagerPaleontologistTradeHandler;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.VillagerRegistry;



@Mod (modid = "extviljura", name = "Extended_Villages_JurassiCraft_addon", version = "1.7.10-0.1", dependencies = "required-after:fossilhunting;required-after:extvil;required-after:bygoneage;required-after:jurassicraft;required-after:llib")


public class JurassiCraftAddon
{
	public static String modid = "extviljura";	
	@SidedProxy(clientSide = "com.extviljuraaddon.core.JurassiCraftAddonClient", serverSide = "com.extviljuraaddon.core.JurassiCraftAddonProxy")
	public static JurassiCraftAddonProxy proxy;

	@Mod.Instance("extvillom")
	public static JurassiCraftAddon instance;
	
    public static final ResourceLocation HPAL_TEXTURE = new ResourceLocation("extviljura:textures/entities/villager/HPal.png");
    public static final ResourceLocation PAL_TEXTURE = new ResourceLocation("extviljura:textures/entities/villager/Pal.png");
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{ 
		
	}
	
	@Mod.EventHandler
	public void Init(FMLInitializationEvent event)
	{	
		int paleontologistvillager = ConfigHandler.PalID;
        VillagerRegistry.instance().registerVillagerId(paleontologistvillager);
        VillagerRegistry.instance().registerVillageTradeHandler(paleontologistvillager, new VillagerPaleontologistTradeHandler());
		VillagerRegistry.instance().registerVillageCreationHandler(new PalLabHandler());
		
		MapGenStructureIO.func_143031_a(ComponentPalLab.class, "extvil:PalLab");
		
        if(ExtendedVillages.isHalloween)
        {
        	VillagerRegistry.instance().registerVillagerSkin(paleontologistvillager, this.HPAL_TEXTURE);
        }
        else
        {
    		VillagerRegistry.instance().registerVillagerSkin(paleontologistvillager, this.PAL_TEXTURE);
        }
		proxy.registerRenderInformation();
	}
	
}
