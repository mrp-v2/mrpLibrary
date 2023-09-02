package mrp_v2.mrplibrary.datagen;

import mrp_v2.mrplibrary.MrpLibrary;
import mrp_v2.mrplibrary.datagen.providers.BlockStateProvider;
import mrp_v2.mrplibrary.datagen.providers.util.ModelJsonParser;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class TintedBlockStateGenerator extends BlockStateProvider
{
    public static final ResourceLocation CUBE_TINTED = makeTintedBlockLoc("cube");
    public static final ResourceLocation CUBE_ALL_TINTED = makeTintedBlockLoc("cube_all");
    public static final ResourceLocation CUBE_BOTTOM_TOP_TINTED = makeTintedBlockLoc("cube_bottom_top");
    public static final ResourceLocation CUBE_COLUMN_TINTED = makeTintedBlockLoc("cube_column");
    public static final ResourceLocation CUBE_COLUMN_HORIZONTAL_TINTED =
            makeTintedBlockLoc("cube_column_horizontal_tinted");
    public static final ResourceLocation CUBE_DIRECTIONAL_TINTED = makeTintedBlockLoc("cube_directional");
    public static final ResourceLocation CUBE_MIRRORED_TINTED = makeTintedBlockLoc("cube_mirrored");
    public static final ResourceLocation CUBE_MIRRORED_ALL_TINTED = makeTintedBlockLoc("cube_mirrored_all");
    public static final ResourceLocation CUBE_TOP_TINTED = makeTintedBlockLoc("cube_top");
    public static final ResourceLocation FENCE_GATE_TINTED = makeTintedBlockLoc("fence_gate");
    public static final ResourceLocation FENCE_GATE_OPEN_TINTED = makeTintedBlockLoc("fence_gate_open");
    public static final ResourceLocation FENCE_GATE_WALL_TINTED = makeTintedBlockLoc("fence_gate_wall");
    public static final ResourceLocation FENCE_GATE_WALL_OPEN_TINTED = makeTintedBlockLoc("fence_gate_wall_open");
    public static final ResourceLocation FENCE_INVENTORY_TINTED = makeTintedBlockLoc("fence_inventory");
    public static final ResourceLocation FENCE_POST_TINTED = makeTintedBlockLoc("fence_post");
    public static final ResourceLocation FENCE_SIDE_TINTED = makeTintedBlockLoc("fence_side");
    public static final ResourceLocation ORIENTABLE_TINTED = makeTintedBlockLoc("orientable");
    public static final ResourceLocation ORIENTABLE_VERTICAL_TINTED = makeTintedBlockLoc("orientable_vertical");
    public static final ResourceLocation ORIENTABLE_WITH_BOTTOM_TINTED = makeTintedBlockLoc("orientable_with_bottom");
    public static final ResourceLocation SLAB_TINTED = makeTintedBlockLoc("slab");
    public static final ResourceLocation SLAB_TOP_TINTED = makeTintedBlockLoc("slab_top");
    public static final ResourceLocation STAIRS_TINTED = makeTintedBlockLoc("stairs");
    public static final ResourceLocation STAIRS_INNER_TINTED = makeTintedBlockLoc("stairs_inner");
    public static final ResourceLocation STAIRS_OUTER_TINTED = makeTintedBlockLoc("stairs_outer");
    public static final ResourceLocation WALL_INVENTORY_TINTED = makeTintedBlockLoc("wall_inventory");
    public static final ResourceLocation WALL_POST_TINTED = makeTintedBlockLoc("wall_post");
    public static final ResourceLocation WALL_SIDE_TINTED = makeTintedBlockLoc("wall_side");
    public static final ResourceLocation WALL_SIDE_TALL_TINTED = makeTintedBlockLoc("wall_side_tall");
    public static final ResourceLocation TEMPLATE_ORIENTABLE_TRAPDOOR_BOTTOM_TINTED =
            makeTintedBlockLoc("template_orientable_trapdoor_bottom");
    public static final ResourceLocation TEMPLATE_ORIENTABLE_TRAPDOOR_TOP_TINTED =
            makeTintedBlockLoc("template_orientable_trapdoor_top");
    public static final ResourceLocation TEMPLATE_ORIENTABLE_TRAPDOOR_OPEN_TINTED =
            makeTintedBlockLoc("template_orientable_trapdoor_open");
    public static final ResourceLocation TEMPLATE_TRAPDOOR_BOTTOM_TINTED =
            makeTintedBlockLoc("template_trapdoor_bottom");
    public static final ResourceLocation TEMPLATE_TRAPDOOR_TOP_TINTED = makeTintedBlockLoc("template_trapdoor_top");
    public static final ResourceLocation TEMPLATE_TRAPDOOR_OPEN_TINTED = makeTintedBlockLoc("template_trapdoor_open");
    public static final HashMap<Direction, Pair<Integer, Integer>> FACE_ROTATION_MAP;

    static
    {
        FACE_ROTATION_MAP = new HashMap<>();
        FACE_ROTATION_MAP.put(Direction.NORTH, Pair.of(0, 0));
        FACE_ROTATION_MAP.put(Direction.EAST, Pair.of(0, 90));
        FACE_ROTATION_MAP.put(Direction.SOUTH, Pair.of(0, 180));
        FACE_ROTATION_MAP.put(Direction.WEST, Pair.of(0, -90));
        FACE_ROTATION_MAP.put(Direction.UP, Pair.of(-90, 0));
        FACE_ROTATION_MAP.put(Direction.DOWN, Pair.of(90, 0));
    }

    public final ModelJsonParser parser;

    public TintedBlockStateGenerator(PackOutput gen, String modid, ExistingFileHelper exFileHelper)
    {
        super(gen, modid, exFileHelper);
        parser = new ModelJsonParser(models());
    }

    private static ResourceLocation makeTintedBlockLoc(String loc)
    {
        return makeBlockLoc("tinted/" + loc);
    }

    private static ResourceLocation makeBlockLoc(String loc)
    {
        return new ResourceLocation(MrpLibrary.ID, "block/" + loc);
    }

    @Override protected void registerStatesAndModels()
    {
        registerFullBlockModels();
        registerSlabModels();
        registerStairModels();
        registerFenceModels();
        registerFenceGateModels();
        registerWallModels();
        registerTrapDoorModels();
    }

    @Nonnull @Override public String getName()
    {
        return super.getName() + ": Tinted Blocks";
    }

    private void registerTrapDoorModels()
    {
        forEachElement(parser.buildBlockModel(mcLoc("template_orientable_trapdoor_bottom"),
                TEMPLATE_ORIENTABLE_TRAPDOOR_BOTTOM_TINTED), this::addTintsToElement);
        forEachElement(parser.buildBlockModel(mcLoc("template_orientable_trapdoor_open"),
                TEMPLATE_ORIENTABLE_TRAPDOOR_OPEN_TINTED), this::addTintsToElement);
        forEachElement(parser.buildBlockModel(mcLoc("template_orientable_trapdoor_top"),
                TEMPLATE_ORIENTABLE_TRAPDOOR_TOP_TINTED), this::addTintsToElement);
        forEachElement(parser.buildBlockModel(mcLoc("template_trapdoor_bottom"), TEMPLATE_TRAPDOOR_BOTTOM_TINTED),
                this::addTintsToElement);
        forEachElement(parser.buildBlockModel(mcLoc("template_trapdoor_open"), TEMPLATE_TRAPDOOR_OPEN_TINTED),
                this::addTintsToElement);
        forEachElement(parser.buildBlockModel(mcLoc("template_trapdoor_top"), TEMPLATE_TRAPDOOR_TOP_TINTED),
                this::addTintsToElement);
    }

    private void registerFenceModels()
    {
        forEachElement(parser.buildBlockModel(mcLoc("fence_inventory"), FENCE_INVENTORY_TINTED),
                this::addTintsToElement);
        forEachElement(parser.buildBlockModel(mcLoc("fence_post"), FENCE_POST_TINTED), this::addTintsToElement);
        forEachElement(parser.buildBlockModel(mcLoc("fence_side"), FENCE_SIDE_TINTED), this::addTintsToElement);
    }

    private void registerFenceGateModels()
    {
        forEachElement(parser.buildBlockModel(mcLoc("template_fence_gate"), FENCE_GATE_TINTED),
                this::addTintsToElement);
        forEachElement(parser.buildBlockModel(mcLoc("template_fence_gate_open"), FENCE_GATE_OPEN_TINTED),
                this::addTintsToElement);
        forEachElement(parser.buildBlockModel(mcLoc("template_fence_gate_wall"), FENCE_GATE_WALL_TINTED),
                this::addTintsToElement);
        forEachElement(parser.buildBlockModel(mcLoc("template_fence_gate_wall_open"), FENCE_GATE_WALL_OPEN_TINTED),
                this::addTintsToElement);
    }

    private void registerFullBlockModels()
    {
        ModelFile cubeTinted =
                forEachElement(parser.buildBlockModel(mcLoc("cube"), CUBE_TINTED), this::addTintsToElement);
        parser.buildBlockModel(mcLoc("cube_all"), CUBE_ALL_TINTED).parent(cubeTinted);
        parser.buildBlockModel(mcLoc("cube_bottom_top"), CUBE_BOTTOM_TOP_TINTED).parent(cubeTinted);
        parser.buildBlockModel(mcLoc("cube_column"), CUBE_COLUMN_TINTED).parent(cubeTinted);
        forEachElement(parser.buildBlockModel(mcLoc("cube_column_horizontal"), CUBE_COLUMN_HORIZONTAL_TINTED),
                this::addTintsToElement);
        forEachElement(parser.buildBlockModel(mcLoc("cube_directional"), CUBE_DIRECTIONAL_TINTED),
                this::addTintsToElement);
        ModelFile cubeMirroredTinted =
                forEachElement(parser.buildBlockModel(mcLoc("cube_mirrored"), CUBE_MIRRORED_TINTED),
                        this::addTintsToElement);
        parser.buildBlockModel(mcLoc("cube_mirrored_all"), CUBE_MIRRORED_ALL_TINTED).parent(cubeMirroredTinted);
        parser.buildBlockModel(mcLoc("cube_top"), CUBE_TOP_TINTED).parent(cubeTinted);
        ModelFile orientableWithBottom =
                parser.buildBlockModel(mcLoc("orientable_with_bottom"), ORIENTABLE_WITH_BOTTOM_TINTED)
                        .parent(cubeTinted);
        parser.buildBlockModel(mcLoc("orientable"), ORIENTABLE_TINTED).parent(orientableWithBottom);
        parser.buildBlockModel(mcLoc("orientable_vertical"), ORIENTABLE_VERTICAL_TINTED).parent(cubeTinted);
    }

    protected <T extends ModelBuilder<T>> void addTintsToElement(ModelBuilder<T>.ElementBuilder builder)
    {
        builder.faces(this::addTintsToFace);
    }

    protected <T extends ModelBuilder<T>> void addTintsToFace(Direction dir,
            ModelBuilder<T>.ElementBuilder.FaceBuilder faceBuilder)
    {
        faceBuilder.tintindex(0);
    }

    private void registerWallModels()
    {
        forEachElement(parser.buildBlockModel(mcLoc("wall_inventory"), WALL_INVENTORY_TINTED), this::addTintsToElement);
        forEachElement(parser.buildBlockModel(mcLoc("template_wall_post"), WALL_POST_TINTED), this::addTintsToElement);
        forEachElement(parser.buildBlockModel(mcLoc("template_wall_side"), WALL_SIDE_TINTED), this::addTintsToElement);
        forEachElement(parser.buildBlockModel(mcLoc("template_wall_side_tall"), WALL_SIDE_TALL_TINTED),
                this::addTintsToElement);
    }

    private void registerSlabModels()
    {
        forEachElement(parser.buildBlockModel(mcLoc("slab"), SLAB_TINTED), this::addTintsToElement);
        forEachElement(parser.buildBlockModel(mcLoc("slab_top"), SLAB_TOP_TINTED), this::addTintsToElement);
    }

    private void registerStairModels()
    {
        forEachElement(parser.buildBlockModel(mcLoc("stairs"), STAIRS_TINTED), this::addTintsToElement);
        forEachElement(parser.buildBlockModel(mcLoc("inner_stairs"), STAIRS_INNER_TINTED), this::addTintsToElement);
        forEachElement(parser.buildBlockModel(mcLoc("outer_stairs"), STAIRS_OUTER_TINTED), this::addTintsToElement);
    }
}
