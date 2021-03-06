package com.teamwizardry.wizardry.client.gui.book;

import com.teamwizardry.librarianlib.features.gui.provided.book.IBookGui;
import com.teamwizardry.librarianlib.features.gui.provided.book.context.Bookmark;
import com.teamwizardry.librarianlib.features.gui.provided.book.context.ComponentBookMark;
import com.teamwizardry.librarianlib.features.gui.provided.book.structure.ComponentMaterialsBar;
import com.teamwizardry.librarianlib.features.gui.provided.book.structure.StructureMaterials;
import com.teamwizardry.wizardry.api.block.WizardryStructureRenderCompanion;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.structure.template.Template;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BookmarkWizardryStructure implements Bookmark {

	private WizardryStructureRenderCompanion structure;

	public BookmarkWizardryStructure(WizardryStructureRenderCompanion structure) {
		this.structure = structure;
	}

	@NotNull
	@Override
	public ComponentBookMark createBookmarkComponent(@NotNull IBookGui book, int bookmarkIndex) {
		HashMap<List<IBlockState>, Integer> map = new HashMap<>();
		if (structure != null)
			for (Template.BlockInfo info : structure.getBlockInfos()) {
				if (info.blockState.getBlock() == Blocks.AIR) continue;

				List<IBlockState> list = new ArrayList<>();
				list.add(info.blockState);
				map.put(list, map.getOrDefault(list, 0) + 1);
			}

		return new ComponentMaterialsBar(book, bookmarkIndex, new StructureMaterials(map));
	}
}
