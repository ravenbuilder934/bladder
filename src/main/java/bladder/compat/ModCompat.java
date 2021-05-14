package bladder.compat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.fml.ModList;

public class ModCompat
{

    public static final Mod[] MODS = {
            new MinecraftCompat(),
            new MilkAllTheMobs()
    };

    public static boolean canEntityBeMilked(Entity entity)
    {
        for (Mod mod : MODS)
        {
            if (mod.isLoaded() && mod instanceof MobMilkingMod && ((MobMilkingMod) mod).canEntityBeMilked(entity)
                && (!(entity instanceof LivingEntity) || !((LivingEntity) entity).isChild()))
            {
                return true;
            }
        }
        return false;
    }

    public interface MobMilkingMod
    {
        boolean canEntityBeMilked(Entity entity);
    }

    public static class Mod
    {

        protected String name;

        public Mod(String name)
        {
            this.name = name;
        }

        public boolean isLoaded()
        {
            return ModList.get().isLoaded(this.name);
        }

    }

}
