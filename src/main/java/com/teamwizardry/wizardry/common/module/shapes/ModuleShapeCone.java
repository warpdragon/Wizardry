package com.teamwizardry.wizardry.common.module.shapes;

import com.teamwizardry.librarianlib.client.fx.particle.ParticleBuilder;
import com.teamwizardry.librarianlib.client.fx.particle.ParticleSpawner;
import com.teamwizardry.librarianlib.client.fx.particle.functions.InterpFadeInOut;
import com.teamwizardry.librarianlib.common.util.math.Matrix4;
import com.teamwizardry.librarianlib.common.util.math.interpolate.StaticInterp;
import com.teamwizardry.librarianlib.common.util.math.interpolate.position.InterpCircle;
import com.teamwizardry.librarianlib.common.util.math.interpolate.position.InterpLine;
import com.teamwizardry.wizardry.Wizardry;
import com.teamwizardry.wizardry.api.Constants;
import com.teamwizardry.wizardry.api.spell.Attributes;
import com.teamwizardry.wizardry.api.spell.Module;
import com.teamwizardry.wizardry.api.spell.ModuleType;
import com.teamwizardry.wizardry.api.spell.RegisterModule;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


/**
 * Created by LordSaad.
 */
@RegisterModule
public class ModuleShapeCone extends Module {

	public ModuleShapeCone() {
	}

	@NotNull
	@Override
	public ItemStack getRequiredStack() {
		return new ItemStack(Items.GUNPOWDER);
	}

	@Override
	public double getManaToConsume() {
		return 50;
	}

	@Override
	public double getBurnoutToFill() {
		return 80;
	}

	@NotNull
	@Override
	public ModuleType getModuleType() {
		return ModuleType.SHAPE;
	}

	@NotNull
	@Override
	public String getID() {
		return "shape_cone";
	}

	@NotNull
	@Override
	public String getReadableName() {
		return "Cone";
	}

	@NotNull
	@Override
	public String getDescription() {
		return "Will run the spell in a circular arc in front of the caster";
	}

	@Override
	public boolean run(@NotNull World world, @Nullable EntityLivingBase caster) {
		if (caster == null) return false;
		if (nextModule == null) return true;

		double range = 5;
		if (attributes.hasKey(Attributes.EXTEND)) range += attributes.getDouble(Attributes.EXTEND);
		float offX = 0.5f * (float) Math.sin(Math.toRadians(-90.0f - caster.rotationYaw));
		float offZ = 0.5f * (float) Math.cos(Math.toRadians(-90.0f - caster.rotationYaw));
		Vec3d origin = new Vec3d(offX, caster.getEyeHeight(), offZ).add(caster.getPositionVector());

		setTargetPosition(this, caster.getLookVec());
		for (int i = 0; i < range * 10; i++) {
			Matrix4 matrix = new Matrix4();
			Vec3d cross = caster.getLookVec().crossProduct(new Vec3d(0, 1, 0));
			matrix.rotate(Math.toRadians(ThreadLocalRandom.current().nextDouble(-range * 10, range * 10)), caster.getLookVec());
			Vec3d normal = matrix.apply(cross).normalize();

			Matrix4 matrix2 = new Matrix4();
			matrix2.rotate(Math.toRadians(ThreadLocalRandom.current().nextDouble(-range * 10, range * 10)), normal);
			Vec3d target = matrix2.apply(caster.getLookVec()).scale(ThreadLocalRandom.current().nextDouble(range)).add(origin);
			nextModule.run(world, caster, target);

			List<Entity> entityList = world.getEntitiesWithinAABBExcludingEntity(caster, new AxisAlignedBB(new BlockPos(target)));
			if (entityList.isEmpty()) continue;
			for (Entity entity : entityList) {
				if (entity == null) continue;
				nextModule.run(world, caster, entity);
			}
		}
		return true;
	}

	@Override
	public void runClient(@NotNull World world, @Nullable ItemStack stack, @Nullable EntityLivingBase caster, @NotNull Vec3d pos) {
		if (caster == null) return;
		Color color = getColor();
		if (color == null) color = Color.WHITE;
		double range = 5;
		if (attributes.hasKey(Attributes.EXTEND)) range += attributes.getDouble(Attributes.EXTEND);
		float offX = 0.5f * (float) Math.sin(Math.toRadians(-90.0f - caster.rotationYaw));
		float offZ = 0.5f * (float) Math.cos(Math.toRadians(-90.0f - caster.rotationYaw));
		Vec3d origin = new Vec3d(offX, caster.getEyeHeight(), offZ).add(caster.getPositionVector());

		ParticleBuilder glitter = new ParticleBuilder(25);
		glitter.setRender(new ResourceLocation(Wizardry.MODID, Constants.MISC.SPARKLE_BLURRED));
		glitter.setCollision(true);

		double finalRange = range;
		Color finalColor = color;
		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(origin), (int) (range * 50), 0, (aFloat, particleBuilder) -> {
			double radius = 0.5;
			double theta = 2.0f * (float) Math.PI * ThreadLocalRandom.current().nextFloat();
			double r = radius * ThreadLocalRandom.current().nextFloat();
			double x = r * MathHelper.cos((float) theta);
			double z = r * MathHelper.sin((float) theta);
			glitter.setPositionOffset(new Vec3d(x, ThreadLocalRandom.current().nextDouble(0.5), z));
			glitter.setColor(new Color(
					Math.min(255, finalColor.getRed() + ThreadLocalRandom.current().nextInt(20, 50)),
					Math.min(255, finalColor.getGreen() + ThreadLocalRandom.current().nextInt(20, 50)),
					Math.min(255, finalColor.getBlue() + ThreadLocalRandom.current().nextInt(20, 50)),
					100));
			glitter.setScale(1);
			glitter.setAlphaFunction(new InterpFadeInOut(0.3f, (float) ThreadLocalRandom.current().nextDouble(0.3, 1)));
			InterpCircle circle = new InterpCircle(caster.getLookVec().scale(finalRange), caster.getLookVec(), (float) finalRange, 1, ThreadLocalRandom.current().nextFloat());
			glitter.setPositionFunction(new InterpLine(Vec3d.ZERO, circle.get(0)));
		});
	}

	@NotNull
	@Override
	public ModuleShapeCone copy() {
		ModuleShapeCone module = new ModuleShapeCone();
		module.deserializeNBT(serializeNBT());
		process(module);
		return module;
	}
}
