package mrp_v2.mrplibrary.datagen;

import mrp_v2.mrplibrary.MrpLibrary;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = MrpLibrary.ID, bus = Mod.EventBusSubscriber.Bus.MOD) public class DataGenHandler
{
    @SubscribeEvent public static void gatherDataEvent(GatherDataEvent event)
    {
        DataGeneratorHelper helper = new DataGeneratorHelper(event, MrpLibrary.ID);
        helper.addBlockStateProvider(TintedBlockStateGenerator::new);
    }
}
