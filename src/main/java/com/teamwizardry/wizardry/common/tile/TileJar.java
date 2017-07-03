package com.teamwizardry.wizardry.common.tile;

import com.teamwizardry.librarianlib.features.autoregister.TileRegister;
import com.teamwizardry.librarianlib.features.base.block.TileMod;
import com.teamwizardry.librarianlib.features.saving.Save;

import java.awt.*;

/**
 * Created by LordSaad.
 */
@TileRegister("jar")
public class TileJar extends TileMod {

	@Save
	public boolean hasFairy = false;
	@Save
	public Color color = Color.WHITE;
	@Save
	public int age = 0;
}