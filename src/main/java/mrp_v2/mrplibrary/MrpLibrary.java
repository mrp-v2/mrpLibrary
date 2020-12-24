package mrp_v2.mrplibrary;

import mrp_v2.mrplibrary.util.ObjectHolder;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MrpLibrary.ID) public class MrpLibrary
{
    public static final String ID = "mrp" + "library";

    public MrpLibrary()
    {
        ObjectHolder.registerListeners(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
