package bladder;

import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Bladder.MOD_ID);

    public static final RegistryObject<BladderItem> emptybladder = ITEMS.register("emptybladder", () -> new BladderItem(Fluids.EMPTY, (new Item.Properties()).stacksTo(16).tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<BladderItem> fullbladder = ITEMS.register("fullbladder", () -> new BladderItem(Fluids.WATER, (new Item.Properties()).craftRemainder(emptybladder.get()).stacksTo(1).tab(ItemGroup.TAB_MISC)));

}
