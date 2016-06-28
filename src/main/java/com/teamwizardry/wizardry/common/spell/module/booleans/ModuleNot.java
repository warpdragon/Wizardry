package com.teamwizardry.wizardry.common.spell.module.booleans;

import com.teamwizardry.wizardry.api.module.Module;
import com.teamwizardry.wizardry.api.spell.ModuleType;

public class ModuleNot extends Module
{
	@Override
	public ModuleType getType()
	{
		return ModuleType.BOOLEAN;
	}
	
	@Override
	public String getDescription()
	{
		return "Will pass condition if it is false.";
	}
}
