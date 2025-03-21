/*
 * This is the latest source code of Passive Endermen.
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

package com.natamus.passiveendermen.fabric.mixin;

import com.natamus.collective_fabric.functions.ConfigFunctions;
import com.natamus.passiveendermen.util.Reference;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net/minecraft/world/entity/monster/EnderMan$EndermanTakeBlockGoal", priority = 1001)
public class EndermanTakeBlockGoalMixin {
    private final boolean preventEndermenFromGriefing = ConfigFunctions.getDictValues(Reference.MOD_ID).get("preventEndermenFromGriefing").equals("true");

    @Inject(method = "canUse()Z", at = @At(value = "HEAD"), cancellable = true)
    public void canUse(CallbackInfoReturnable<Boolean> cir) {
        if (preventEndermenFromGriefing) {
            cir.setReturnValue(false);
        }
    }
}
