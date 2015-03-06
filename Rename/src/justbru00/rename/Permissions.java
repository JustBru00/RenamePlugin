package justbru00.rename;

import org.bukkit.permissions.Permission;

/**
 *******************************************
 * @author Justin Brubaker
 * Plugin name: EpicRename
 *
 *             Copyright (C) 2015 Justin Brubaker
 *
 *             This program is free software; you can redistribute it and/or
 *             modify it under the terms of the GNU General Public License as
 *             published by the Free Software Foundation; either version 2 of
 *             the License, or (at your option) any later version.
 *
 *             This program is distributed in the hope that it will be useful,
 *             but WITHOUT ANY WARRANTY; without even the implied warranty of
 *             MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *             General Public License for more details.
 *
 *             You should have received a copy of the GNU General Public License
 *             along with this program; if not, write to the Free Software
 *             Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 *             02110-1301 USA. 
 *             
 *             You can contact the author @ justbru00@gmail.com
 */
public class Permissions {

	public Permission rename = new Permission("rename.use");
	public Permission renameany = new Permission("rename.any");
	public Permission lore = new Permission("rename.lore");

}
