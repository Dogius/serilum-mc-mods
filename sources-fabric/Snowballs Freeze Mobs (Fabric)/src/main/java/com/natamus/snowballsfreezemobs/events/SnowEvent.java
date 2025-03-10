/*
 * This is the latest source code of Snowballs Freeze Mobs.
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

package com.natamus.snowballsfreezemobs.events;

import java.util.HashMap;
import java.util.Map;

import com.natamus.collective_fabric.functions.EntityFunctions;
import com.natamus.snowballsfreezemobs.config.ConfigHandler;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SnowEvent {
	private static Map<LivingEntity, Vec3> hitentities = new HashMap<LivingEntity, Vec3>();
	
	public static float onEntityHurt(Level world, Entity entity, DamageSource damageSource, float damageAmount) {
		if (world.isClientSide) {
			return damageAmount;
		}
		
		if (damageSource.getDirectEntity() instanceof Snowball == false) {
			return damageAmount;
		}
		
		if (entity instanceof Player) {
			return damageAmount;
		}
		
		EntityFunctions.addPotionEffect(entity, MobEffects.BLINDNESS, ConfigHandler.freezeTimeMs);
		hitentities.put((LivingEntity)entity, entity.position());
		return 0.0F;
	}
	
	public static void onLivingUpdate(Level world, Entity entity) {
		if (world.isClientSide) {
			return;
		}
		
		if(entity instanceof LivingEntity == false) {
			return;
		}
		LivingEntity le = (LivingEntity)entity;
		
		if (le.getEffect(MobEffects.BLINDNESS) != null) {
			if (hitentities.containsKey(le)) {
				Vec3 lastvec = hitentities.get(le);
				le.teleportTo(lastvec.x, le.getY(), lastvec.z);
			}
		}
		else {
			if (hitentities.containsKey(le)) {
				hitentities.remove(le);
			}
		}
	}
}
