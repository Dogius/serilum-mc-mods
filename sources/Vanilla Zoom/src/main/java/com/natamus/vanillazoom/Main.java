/*
 * This is the latest source code of Vanilla Zoom.
 * Minecraft version: 1.19.3, mod version: 1.0.
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

package com.natamus.vanillazoom;

import com.natamus.collective.check.RegisterMod;
import com.natamus.vanillazoom.events.ZoomEvent;
import com.natamus.vanillazoom.util.Reference;
import com.natamus.vanillazoom.util.Variables;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(Reference.MOD_ID)
public class Main {
	public static Main instance;
	
    public Main() {
		if (!FMLEnvironment.dist.equals(Dist.CLIENT)) {
			return;
		}
    	
        instance = this;

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modEventBus.addListener(this::registerKeyBinding));

        modEventBus.addListener(this::loadComplete);

        RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
    }

	public void registerKeyBinding(final RegisterKeyMappingsEvent e) {
    	Variables.hotkey = new KeyMapping("key.vanillazoom.togglezoom.desc", 342, "key.categories.misc");
    	e.register(Variables.hotkey);
    }
	
    private void loadComplete(final FMLLoadCompleteEvent event) {
    	MinecraftForge.EVENT_BUS.register(new ZoomEvent());
	}
}
