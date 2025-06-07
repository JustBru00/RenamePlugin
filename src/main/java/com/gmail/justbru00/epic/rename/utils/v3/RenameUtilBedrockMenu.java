package com.gmail.justbru00.epic.rename.utils.v3;

import com.gmail.justbru00.epic.rename.enums.v3.EpicRenameCommands;
import net.weheba.wlib.menus.bedrock.ActionButtonComponent;
import net.weheba.wlib.menus.bedrock.SimpleBedrockMenu;
import net.weheba.wlib.menus.bedrock.formbuilders.CustomFormBuilder;
import net.weheba.wlib.menus.bedrock.formbuilders.SimpleFormBuilder;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.cumulus.form.Form;
import org.geysermc.cumulus.util.FormImage;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public  class RenameUtilBedrockMenu extends SimpleBedrockMenu{

	private final JavaPlugin plugin;

	public RenameUtilBedrockMenu( JavaPlugin plugin) {
		super(plugin, "renamemenu.yml");
		this.plugin = plugin;
	}
	/*
	 * Builds out the colored form taking in config
	 *
	 * @param a Player
	 *
	 * @return Ready Form that will be opened when .Open() is called.
	 */
	protected Form buildForm(Player player)
	{
		String title = config.getString("title", "`name-input.text` not set");

		String nameInputText = config.getString("name-input.text", "`name-input.text` not set");
		String nameInputPlaceholder = config.getString("name-input.placeholder", "`name-input.placeholder` not set");

		CustomFormBuilder formBuilder = CustomFormBuilder.create()
				.title(title);

		formBuilder.input("name", nameInputText, nameInputPlaceholder)
				.onSubmit((response, data) -> {
					String name = (String) data.get("name");
					CompletableFuture.runAsync(() -> {
						RenameUtil.renameHandle(player, new String[]{name}, EpicRenameCommands.RENAMEMENU);
					});
				});


		return formBuilder.build();
	}
}