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

package com.natamus.thevanillaexperience.mods.guiclock.events;

import java.awt.Color;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.thevanillaexperience.mods.guiclock.config.GUIClockConfigHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GUIClockGUIEvent extends Gui {
	private static Minecraft mc;
	private static String daystring = "";

	public GUIClockGUIEvent(Minecraft mc){
		super(mc);
		GUIClockGUIEvent.mc = mc; 
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void renderOverlay(RenderGameOverlayEvent.Post e){
		ElementType type = e.getType();
		if (type != ElementType.TEXT) {
			return;
		}
		
		boolean gametimeb = GUIClockConfigHandler.GENERAL.mustHaveClockInInventoryForGameTime.get();
		boolean realtimeb = GUIClockConfigHandler.GENERAL.mustHaveClockInInventoryForRealTime.get();
		boolean found = true;
		
		if (gametimeb || realtimeb) {
			found = false;
			Inventory inv = mc.player.getInventory();
			for (int n = 0; n <= 35; n++) {
				if (inv.getItem(n).getItem().equals(Items.CLOCK)) {
					found = true;
					break;
				}
			}
		}
		
		PoseStack posestack = e.getMatrixStack();
		posestack.pushPose();
		
		Font fontRender = mc.font;
		Window scaled = mc.getWindow();
		int width = scaled.getGuiScaledWidth();
		
		int heightoffset = GUIClockConfigHandler.GENERAL.clockHeightOffset.get();
		if (heightoffset < 5) {
			heightoffset = 5;
		}
		
		int xcoord = 0;
		int daycoord = 0;
		if (GUIClockConfigHandler.GENERAL.showOnlyMinecraftClockIcon.get()) {
			if (gametimeb) {
				if (!found) {
					return;
				}
			}
			
			if (GUIClockConfigHandler.GENERAL.clockPositionIsLeft.get()) {
				xcoord = 20;
			}
			else if (GUIClockConfigHandler.GENERAL.clockPositionIsCenter.get()) {
				xcoord = (width/2) - 8;
			}
			else {
				xcoord = width - 20;
			}
			
			xcoord += GUIClockConfigHandler.GENERAL.clockWidthOffset.get();
			
			ItemRenderer itemrenderer = mc.getItemRenderer();
			itemrenderer.renderAndDecorateItem(new ItemStack(Items.CLOCK), xcoord, heightoffset);
		}
		else {
			String time = "";
			String realtime = StringFunctions.getPCLocalTime(GUIClockConfigHandler.GENERAL._24hourformat.get(), GUIClockConfigHandler.GENERAL.showRealTimeSeconds.get());
			if (GUIClockConfigHandler.GENERAL.showBothTimes.get()) {
				if (gametimeb && realtimeb) {
					if (!found) {
						return;
					}
					time = getGameTime() + " | " + realtime;
				}
				else if (!found && gametimeb) {
					time = realtime;
				}
				else if (!found && realtimeb) {
					time = getGameTime();
				}
				else {
					time = getGameTime() + " | " + realtime;
				}
			}
			else if (GUIClockConfigHandler.GENERAL.showRealTime.get()) {
				if (realtimeb) {
					if (!found) {
						return;
					}
				}
				time = realtime;
			}
			else {
				if (gametimeb) {
					if (!found) {
						return;
					}
				}
				time = getGameTime();
			}
			
			if (time == "") {
				return;
			}
			
			int stringWidth = fontRender.width(time);
			int daystringWidth = fontRender.width(daystring);
			
			Color colour = new Color(GUIClockConfigHandler.GENERAL.RGB_R.get(), GUIClockConfigHandler.GENERAL.RGB_G.get(), GUIClockConfigHandler.GENERAL.RGB_B.get(), 255);
			
			if (GUIClockConfigHandler.GENERAL.clockPositionIsLeft.get()) {
				xcoord = 5;
				daycoord = 5;
			}
			else if (GUIClockConfigHandler.GENERAL.clockPositionIsCenter.get()) {
				xcoord = (width/2) - (stringWidth/2);
				daycoord = (width/2) - (daystringWidth/2);
			}
			else {
				xcoord = width - stringWidth - 5;
				daycoord = width - daystringWidth - 5;
			}
			
			xcoord += GUIClockConfigHandler.GENERAL.clockWidthOffset.get();
			daycoord += GUIClockConfigHandler.GENERAL.clockWidthOffset.get();
			
			fontRender.draw(posestack, time, xcoord, heightoffset, colour.getRGB());
			if (daystring != "") {
				fontRender.draw(posestack, daystring, daycoord, heightoffset+10, colour.getRGB());
			}
		}
		
		posestack.popPose();
	}
	
	private static String getGameTime() {
		int time = 0;
		int gametime = (int)mc.level.getDayTime();
		int daysplayed = 0;
		
		while (gametime >= 24000) {
			gametime-=24000;
			daysplayed += 1;
		}
		
		if (GUIClockConfigHandler.GENERAL.showDaysPlayedWorld.get()) {
			daystring = "Day " + daysplayed;
		}

		if (gametime >= 18000) {
			time = gametime-18000;
		}
		else {
			time = 6000+gametime;
		}
		
		String suffix = "";
		if (!GUIClockConfigHandler.GENERAL._24hourformat.get()) {
			if (time >= 13000) {
				time = time - 12000;
				suffix = " PM";
			}
			else {
				if (time >= 12000) {
					suffix = " PM";
				}
				else {
					suffix = " AM";
					if (time <= 999) {
						time += 12000;
					}
				}
			}
		}
		
		String stringtime = time/10 + "";
		for (int n = stringtime.length(); n < 4; n++) {
			stringtime = "0" + stringtime;
		}
		
		
		String[] strsplit = stringtime.split("");
		
		int minutes = (int)Math.floor(Double.parseDouble(strsplit[2] + strsplit[3])/100*60);
		String sm = minutes + "";
		if (minutes < 10) {
			sm = "0" + minutes;
		}
		
		if (!GUIClockConfigHandler.GENERAL._24hourformat.get() && strsplit[0].equals("0")) {
			stringtime = strsplit[1] + ":" + sm.charAt(0) + sm.charAt(1);
		}
		else {
			stringtime = strsplit[0] + strsplit[1] + ":" + sm.charAt(0) + sm.charAt(1);
		}
		
		return stringtime + suffix;
	}
}