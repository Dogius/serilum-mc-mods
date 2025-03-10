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

package com.natamus.thevanillaexperience.mods.recipecommands.cmds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.natamus.collective.functions.StringFunctions;
import com.natamus.thevanillaexperience.mods.recipecommands.util.Recipes;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class CommandRecipes {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    	dispatcher.register(Commands.literal("recipes")
			.requires((iCommandSender) -> iCommandSender.getEntity() instanceof ServerPlayer)
			.executes((command) -> {
				CommandSourceStack source = command.getSource();
				
				sendUsage(source);
				return 1;
			})
			.then(Commands.argument("recipe", ResourceLocationArgument.id()).suggests(SuggestionProviders.ALL_RECIPES)
			.executes((command) -> {
				CommandSourceStack source = command.getSource();
				
				try {
					sendRecipe(command);
				}
				catch (CommandSyntaxException ex) {
					StringFunctions.sendMessage(source, "Unable to find recipe.", ChatFormatting.RED);
				}
				return 1;
			}))
		);
    	dispatcher.register(Commands.literal("rec")
    			.requires((iCommandSender) -> iCommandSender.getEntity() instanceof ServerPlayer)
    			.executes((command) -> {
    				CommandSourceStack source = command.getSource();
    				
    				sendUsage(source);
    				return 1;
    			})
    			.then(Commands.argument("recipe", ResourceLocationArgument.id()).suggests(SuggestionProviders.ALL_RECIPES)
    			.executes((command) -> {
    				CommandSourceStack source = command.getSource();
    				
    				try {
    					sendRecipe(command);
    				}
    				catch (CommandSyntaxException ex) {
    					StringFunctions.sendMessage(source, "Unable to find recipe.", ChatFormatting.RED);
    				}
    				return 1;
    			}))
    		);
    }
    
    private static void sendUsage(CommandSourceStack source) {
    	StringFunctions.sendMessage(source, "Recipe Commands Usage:", ChatFormatting.DARK_GREEN);
    	StringFunctions.sendMessage(source, " /rec <recipe>", ChatFormatting.DARK_GREEN);
    }
    
    @SuppressWarnings("unchecked")
	private static void sendRecipe(CommandContext<CommandSourceStack> command) throws CommandSyntaxException {
    	CommandSourceStack source = command.getSource();
    	Player player = source.getPlayerOrException();
    	Level world = player.getCommandSenderWorld();
    	if (world.isClientSide) {
    		return;
    	}
    	
    	Recipe<?> recipe = ResourceLocationArgument.getRecipe(command, "recipe");
    	ResourceLocation recipelocation = recipe.getId();
    	String recipename = recipelocation.getPath().toString();
    	
    	List<String> items = new ArrayList<String>();
    	HashMap<String, Integer> itemcount = new HashMap<String, Integer>();
    	
    	List<Ingredient> ingredients = recipe.getIngredients();
    	for (Ingredient ingredient : ingredients) {
    		ItemStack[] possiblestacks = ingredient.getItems();
    		if (possiblestacks.length <= 0) {
    			continue;
    		}
    		
    		ItemStack itemstack = possiblestacks[0];
			Item item = itemstack.getItem();
			String itemname = item.toString();
    		if (possiblestacks.length > 1 && !itemname.equalsIgnoreCase("cobblestone")) {
    			Set<ResourceLocation> tags = item.getTags();
    			if (tags.size() > 0) {
    				ResourceLocation tag = tags.iterator().next();
    				itemname = tag.getPath();
    			}
    		}
    		
    		itemname = StringFunctions.capitalizeEveryWord(itemname);
    		
			if (items.contains(itemname)) {
				int currentcount = itemcount.get(itemname);
				itemcount.put(itemname, currentcount+1);
				continue;
			}
			
			items.add(itemname);
			itemcount.put(itemname, 1);
    	}
    	
    	Collections.sort(items);
    	
    	RecipeSerializer<?> serializer = recipe.getSerializer();
    	ResourceLocation srl = serializer.getRegistryName();

    	List<String> pattern = new ArrayList<String>();
    	HashMap<String, String> itemkeys = new HashMap<String, String>();
    	
    	String shape = "shapeless";
    	if (srl != null) {
	    	if (srl.toString().equalsIgnoreCase("minecraft:crafting_shaped")) {
	    		shape = "shaped";
	    		
	    		if (Recipes.jsonrecipes.containsKey(recipename)) {
	    			String jsonrecipe = Recipes.jsonrecipes.get(recipename);
	    			Gson gson = new Gson();
	    			Map<String, ?> map = gson.fromJson(jsonrecipe, Map.class);
	    			String rawjson = map.toString();
	    			
	    			pattern = (List<String>) map.get("pattern");
	    			
	    			String[] spl1 = rawjson.split("key=\\{");
	    			if (spl1.length > 1) {
	    				String keys = spl1[1].split("\\}},")[0];
	    				for (String keyraw : keys.split(", ")) {
	    					String[] keyspl = keyraw.split("=[\\{,\\[]");
	    					if (keyspl.length <= 1) {
	    						continue;
	    					}
	    					
	    					String key = keyspl[0];
	    					if (Recipes.replacekeys.containsKey(key)) {
	    						key = Recipes.replacekeys.get(key);
	    					}
	    					String itemvalue = keyspl[1].split(":")[1].replaceAll("\\}", "");
	    					itemkeys.put(itemvalue, key);
	    				}
	    			}
	    		}
	    	}
    	}
    	
    	ItemStack output = recipe.getResultItem();
    	String outputname = output.getItem().toString();
    	outputname = StringFunctions.capitalizeEveryWord(outputname.replace("_", " "));
		
    	StringFunctions.sendMessage(source, outputname + " has a " + shape + " recipe.", ChatFormatting.DARK_GREEN, true);
    	StringFunctions.sendMessage(source, " Ingredients:", ChatFormatting.DARK_GREEN);
    	for (String itemname : items) {
    		int count = itemcount.get(itemname);
    		String todisplayname = itemname;
    		if (shape.equalsIgnoreCase("shaped")) {
    			String shapeditemname = itemname.toLowerCase().replace(" ", "_").split("\\/")[0];
    			if (itemkeys.containsKey(shapeditemname)) {
    				String itemkey = itemkeys.get(shapeditemname);
    				todisplayname += " (" + itemkey + ")";
    			}
    		}
    		
    		todisplayname = todisplayname.replace("_", " ");
    		
        	StringFunctions.sendMessage(source, "  " + count + "x " + todisplayname, ChatFormatting.DARK_GREEN);
    	}
    	
    	if (shape.equalsIgnoreCase("shaped")) { 		
    		if (pattern.size() > 0) {
    			StringFunctions.sendMessage(source, " Pattern:", ChatFormatting.DARK_GREEN);
    			
        		for (String line : pattern) {
        			for (String toreplace : Recipes.replacekeys.keySet()) {
        				line = line.replaceAll(toreplace, Recipes.replacekeys.get(toreplace));
        			}
        			StringFunctions.sendMessage(source, "  " + line.replace(" ", "_"), ChatFormatting.DARK_GREEN);
        		}
    		}
    	}
    }
}