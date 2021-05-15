package bladder.init;

import bladder.api.item.BladderItems;
import bladder.item.BladderItem;
import bladder.item.FilledBladderItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import static bladder.Bladder.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems
{

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        BladderItems.EMPTY_BLADDER = registerItem("empty_bladder", new BladderItem((new Item.Properties()).stacksTo(16).tab(ItemGroup.TAB_MISC)));
        BladderItems.FULL_BLADDER = registerItem("full_bladder", new FilledBladderItem((new Item.Properties()).stacksTo(1).tab(ItemGroup.TAB_MISC)));
    }

    private static Item registerItem(String name, Item item)
    {
        item.setRegistryName(name);
        ForgeRegistries.ITEMS.register(item);
        return item;
    }

}
