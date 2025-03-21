/*
 * This is the latest source code of Naturally Charged Creepers.
 * Minecraft version: 1.19.3, mod version: 2.5.
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

package com.natamus.naturallychargedcreepers.config;

import com.natamus.naturallychargedcreepers.util.Reference;
import com.natamus.collective_fabric.config.DuskConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class NaturallyChargedCreepersModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> DuskConfig.getScreen(parent, Reference.MOD_ID);
    }
}
