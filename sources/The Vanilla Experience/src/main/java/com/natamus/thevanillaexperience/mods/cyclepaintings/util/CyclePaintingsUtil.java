/*
 * This is the latest source code of The Vanilla Experience.
 * Minecraft version: 1.17.1, mod version: 1.4.
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

package com.natamus.thevanillaexperience.mods.cyclepaintings.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.entity.decoration.Motive;
import net.minecraftforge.registries.ForgeRegistries;

public class CyclePaintingsUtil {
	private static List<Motive> paintingtypes;
	
	public static void setPaintings () {
		paintingtypes = new ArrayList<>(ForgeRegistries.PAINTING_TYPES.getValues());
	}
	
	public static List<Motive> getSimilarArt(Motive currentart) {
		List<Motive> similarart = new ArrayList<Motive>();
		int xsize = currentart.getWidth();
		int ysize = currentart.getHeight();
		
		for (Motive aa : paintingtypes) {
			if (aa.getWidth() == xsize && aa.getHeight() == ysize) {
				similarart.add(aa);
			}
		}
		
		return similarart;
	}
}
