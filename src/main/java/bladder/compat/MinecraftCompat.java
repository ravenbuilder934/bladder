package bladder.compat;

import bladder.api.data.ObtainableEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class MinecraftCompat extends ModCompat.Mod implements ModCompat.MobMilkingMod, ModCompat.EntityTypeObtainingMod
{

    private static final ResourceLocation advancement = new ResourceLocation("minecraft", "husbandry/tactical_fishing");

    List<ObtainableEntityType> obtainableEntityTypes = new ArrayList<>();

    public MinecraftCompat()
    {
        super("minecraft");
        this.addFish(EntityType.PUFFERFISH);
        this.addFish(EntityType.SALMON);
        this.addFish(EntityType.COD);
        this.addFish(EntityType.TROPICAL_FISH);
    }

    private void addFish(EntityType<?> fish)
    {
        this.obtainableEntityTypes.add(new ObtainableEntityType.Builder(fish, Fluids.WATER).addFluidTag(FluidTags.WATER).build());
    }

    @Override
    public boolean isLoaded()
    {
        return true;
    }

    @Override
    public boolean canEntityBeMilked(Entity entity)
    {
        return entity instanceof CowEntity;
    }

    @Override
    public List<ObtainableEntityType> getObtainableEntityTypes()
    {
        return this.obtainableEntityTypes;
    }

    @Override
    public ResourceLocation getEntityObtainingAdvancement(@Nonnull Fluid fluid, @Nonnull Entity entity)
    {
        return advancement;
    }
}
