package bladder.compat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.horse.*;

public class MilkAllTheMobs extends ModCompat.Mod implements ModCompat.MobMilkingMod
{

    public MilkAllTheMobs()
    {
        super("milkatmobs");
    }

    @Override
    public boolean canEntityBeMilked(Entity entity)
    {
        return entity instanceof SheepEntity || entity instanceof LlamaEntity || entity instanceof PigEntity || entity instanceof DonkeyEntity || entity instanceof HorseEntity || entity instanceof MuleEntity;
    }
}
