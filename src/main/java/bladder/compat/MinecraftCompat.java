package bladder.compat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.CowEntity;

public class MinecraftCompat extends ModCompat.Mod implements ModCompat.MobMilkingMod
{

    public MinecraftCompat()
    {
        super("minecraft");
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
}
