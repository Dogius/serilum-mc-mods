/*
 * This is the latest source code of Areas.
 * Minecraft version: 1.19.3, mod version: 3.0.
 *
 * Please don't distribute without permission.
 * For all Minecraft modding projects, feel free to visit my profile page on CurseForge or Modrinth.
 *  CurseForge: https://curseforge.com/members/serilum/projects
 *  Modrinth: https://modrinth.com/user/serilum
 *  Overview: https://serilum.com/
 *
 * If you are feeling generous and would like to support the development of the mods, you can!
 *  https://ricksouth.com/donate contains all the information. <3
 *
 * Thanks for looking at the source code! Hope it's of some use to your project. Happy modding!
 */

package com.natamus.areas;

import com.natamus.areas.cmds.CommandAreas;
import com.natamus.areas.config.ConfigHandler;
import com.natamus.areas.events.AreaEvent;
import com.natamus.areas.events.GUIEvent;
import com.natamus.areas.network.PacketToClientShowGUI;
import com.natamus.areas.util.Reference;
import com.natamus.areas.util.Util;
import com.natamus.collective.check.RegisterMod;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkRegistry;

@Mod(Reference.MOD_ID)
public class Main {
	public static Main instance;
	
    public Main() {		
        instance = this;

        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    	
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::loadComplete);
        modLoadingContext.registerConfig(ModConfig.Type.COMMON, ConfigHandler.spec);

        RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
		Util.network = NetworkRegistry.newSimpleChannel(new ResourceLocation(Reference.MOD_ID, Reference.MOD_ID), () -> "1.0", s -> true, s -> true);
		Util.network.registerMessage(1, PacketToClientShowGUI.class, PacketToClientShowGUI::toBytes, PacketToClientShowGUI::new, PacketToClientShowGUI::handle);
    }
    
    private void loadComplete(final FMLLoadCompleteEvent event) {
		if (FMLEnvironment.dist.equals(Dist.CLIENT)) {
			MinecraftForge.EVENT_BUS.register(new GUIEvent(Minecraft.getInstance(), Minecraft.getInstance().getItemRenderer()));
		}
    	MinecraftForge.EVENT_BUS.register(new AreaEvent());
	}
    
    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent e) {
    	CommandAreas.register(e.getDispatcher());
    }
}