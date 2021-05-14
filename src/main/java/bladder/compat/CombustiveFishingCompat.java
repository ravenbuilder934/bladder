package bladder.compat;

import bladder.api.data.ObtainableEntityType;
import net.minecraft.fluid.Fluids;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class CombustiveFishingCompat extends ModCompat.Mod implements ModCompat.EntityTypeObtainingMod
{

    private final List<ObtainableEntityType> obtainableEntityTypes = new ArrayList<>();

    public CombustiveFishingCompat()
    {
        super("combustivefishing");
        this.obtainableEntityTypes.add(new ObtainableEntityType.Builder(new ResourceLocation(this.name, "combustive_cod"), Fluids.LAVA).addFluidTag(FluidTags.LAVA).build());
    }

    @Override
    public List<ObtainableEntityType> getObtainableEntityTypes()
    {
        return this.obtainableEntityTypes;
    }

}
