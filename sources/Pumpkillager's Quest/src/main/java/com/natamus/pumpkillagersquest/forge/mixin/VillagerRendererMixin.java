/*
 * This is the latest source code of Pumpkillager's Quest.
 * Minecraft version: 1.19.3, mod version: 2.3.
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

package com.natamus.pumpkillagersquest.forge.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.natamus.pumpkillagersquest.util.Util;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = VillagerRenderer.class, priority = 1001)
public class VillagerRendererMixin {
    @Inject(method = "scale(Lnet/minecraft/world/entity/npc/Villager;Lcom/mojang/blaze3d/vertex/PoseStack;F)V", at = @At(value = "HEAD"), cancellable = true)
    protected void scale(Villager pLivingEntity, PoseStack pMatrixStack, float pPartialTickTime, CallbackInfo ci) {
        if (Util.isPumpkillager(pLivingEntity)) {
			ItemStack footStack = pLivingEntity.getItemBySlot(EquipmentSlot.FEET);
			if (footStack.getItem().equals(Items.BARRIER)) {
				String scaleFloatString = footStack.getHoverName().getString();

				try {
					float scale = Float.parseFloat(scaleFloatString);
					pMatrixStack.scale(scale, scale, scale);
					ci.cancel();
				} catch (NumberFormatException ignored) { }
			}
		}
    }
}
