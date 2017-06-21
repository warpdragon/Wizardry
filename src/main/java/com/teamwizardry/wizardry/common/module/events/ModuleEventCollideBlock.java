package com.teamwizardry.wizardry.common.module.events;

import com.teamwizardry.wizardry.api.spell.Module;
import com.teamwizardry.wizardry.api.spell.ModuleEvent;
import com.teamwizardry.wizardry.api.spell.RegisterModule;
import com.teamwizardry.wizardry.api.spell.SpellData;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

import static com.teamwizardry.wizardry.api.spell.SpellData.DefaultKeys.BLOCK_HIT;
import static com.teamwizardry.wizardry.api.spell.SpellData.DefaultKeys.ENTITY_HIT;

/**
 * Created by LordSaad.
 */
@RegisterModule
public class ModuleEventCollideBlock extends ModuleEvent {

	@Nonnull
	@Override
	public String getID() {
		return "event_collide_block";
	}

	@Nonnull
	@Override
	public String getReadableName() {
		return "On Collide Block";
	}

	@Nonnull
	@Override
	public String getDescription() {
		return "Triggered when the spell collides with a block";
	}

	@Override
	public boolean run(@Nonnull SpellData spell) {
		BlockPos pos = spell.getData(BLOCK_HIT);
		spell.removeData(ENTITY_HIT);
		return pos != null && nextModule != null && nextModule.run(spell);
	}

	@Override
	public void runClient(@Nonnull SpellData spell) {

	}

	@Nonnull
	@Override
	public Module copy() {
		return cloneModule(new ModuleEventCollideBlock());
	}
}
