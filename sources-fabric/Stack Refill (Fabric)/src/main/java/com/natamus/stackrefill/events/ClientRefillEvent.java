/*
 * This is the latest source code of Stack Refill.
 * Minecraft version: 1.19.3, mod version: 3.3.
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

package com.natamus.stackrefill.events;

import net.minecraft.client.Minecraft;

public class ClientRefillEvent {
    public static void onClientTick(Minecraft mc) {
        RefillEvent.processTick(true);
    }
}
