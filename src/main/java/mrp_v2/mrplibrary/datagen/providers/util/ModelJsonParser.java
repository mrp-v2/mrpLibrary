package mrp_v2.mrplibrary.datagen.providers.util;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.ModelBuilder;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class ModelJsonParser
{
    private static final JsonParser PARSER = new JsonParser();
    BlockModelProvider models;

    public ModelJsonParser(BlockModelProvider models)
    {
        this.models = models;
    }

    @Nullable public ModelBuilder<BlockModelBuilder> buildBlockModel(ResourceLocation model, ResourceLocation newModel)
    {
        model = new ResourceLocation(model.getNamespace(), "models/block/" + model.getPath() + ".json");
        Preconditions.checkArgument(models.existingFileHelper.exists(model, PackType.CLIENT_RESOURCES),
                "Model %s does not exist in any known resource pack", model);
        JsonElement json;
        try
        {
            Resource resource = models.existingFileHelper.getResource(model, PackType.CLIENT_RESOURCES);
            InputStream stream = resource.open();
            InputStreamReader reader = new InputStreamReader(stream);
            json = PARSER.parse(reader);
            reader.close();
            stream.close();
        } catch (IOException e)
        {
            throw new RuntimeException(String.format("Error while reading model %s", model), e);
        }
        ModelBuilder<BlockModelBuilder> modelBuilder = models.getBuilder(newModel.toString());
        consumeJson(modelBuilder, json);
        return modelBuilder;
    }

    private void consumeJson(ModelBuilder<BlockModelBuilder> modelBuilder, JsonElement jsonElement)
    {
        if (jsonElement.isJsonObject())
        {
            JsonObject obj = jsonElement.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : obj.entrySet())
            {
                switch (entry.getKey())
                {
                    case "parent":
                        modelBuilder
                                .parent(models.getExistingFile(new ResourceLocation(entry.getValue().getAsString())));
                        break;
                    case "ambientocclusion":
                        modelBuilder.ao(entry.getValue().getAsBoolean());
                        break;
                    case "display":
                        consumeDisplayJson(modelBuilder.transforms(), entry.getValue().getAsJsonObject());
                        break;
                    case "textures":
                        consumeTexturesJson(modelBuilder, entry.getValue().getAsJsonObject());
                        break;
                    case "elements":
                        consumeElementsJson(modelBuilder, entry.getValue().getAsJsonArray());
                }
            }
        }
    }

    private void consumeDisplayJson(ModelBuilder<BlockModelBuilder>.TransformsBuilder modelDisplayBuilder,
            JsonObject displayJson)
    {
        for (Map.Entry<String, JsonElement> entry : displayJson.entrySet())
        {
            ItemDisplayContext perspective = switch (entry.getKey()) {
                case "thirdperson_righthand" -> ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
                case "thirdperson_lefthand" -> ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
                case "firstperson_righthand" -> ItemDisplayContext.FIRST_PERSON_RIGHT_HAND;
                case "firstperson_lefthand" -> ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
                case "head" -> ItemDisplayContext.HEAD;
                case "gui" -> ItemDisplayContext.GUI;
                case "ground" -> ItemDisplayContext.GROUND;
                case "fixed" -> ItemDisplayContext.FIXED;
                default -> null;
            };
            consumeDisplayTransformJson(modelDisplayBuilder.transform(perspective), entry.getValue().getAsJsonObject());
        }
    }

    private void consumeDisplayTransformJson(
            ModelBuilder<BlockModelBuilder>.TransformsBuilder.TransformVecBuilder modelDisplayTransformBuilder,
            JsonObject displayTransformJson)
    {
        for (Map.Entry<String, JsonElement> entry : displayTransformJson.entrySet())
        {
            JsonArray jsonArray = entry.getValue().getAsJsonArray();
            switch (entry.getKey()) {
                case "rotation" ->
                        modelDisplayTransformBuilder.rotation(jsonArray.get(0).getAsFloat(), jsonArray.get(1).getAsFloat(),
                                jsonArray.get(2).getAsFloat());
                case "translation" -> modelDisplayTransformBuilder
                        .translation(jsonArray.get(0).getAsFloat(), jsonArray.get(1).getAsFloat(),
                                jsonArray.get(2).getAsFloat());
                case "scale" ->
                        modelDisplayTransformBuilder.scale(jsonArray.get(0).getAsFloat(), jsonArray.get(1).getAsFloat(),
                                jsonArray.get(2).getAsFloat());
            }
        }
    }

    private void consumeTexturesJson(ModelBuilder<BlockModelBuilder> modelBuilder, JsonObject texturesJson)
    {
        for (Map.Entry<String, JsonElement> entry : texturesJson.entrySet())
        {
            modelBuilder.texture(entry.getKey(), entry.getValue().getAsString());
        }
    }

    private void consumeElementsJson(ModelBuilder<BlockModelBuilder> modelBuilder, JsonArray elementsJson)
    {
        for (JsonElement elementElement : elementsJson)
        {
            ModelBuilder<BlockModelBuilder>.ElementBuilder elementBuilder = modelBuilder.element();
            JsonObject elementJson = elementElement.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : elementJson.entrySet())
            {
                switch (entry.getKey()) {
                    case "from" -> {
                        JsonArray fromArray = entry.getValue().getAsJsonArray();
                        elementBuilder.from(fromArray.get(0).getAsFloat(), fromArray.get(1).getAsFloat(),
                                fromArray.get(2).getAsFloat());
                    }
                    case "to" -> {
                        JsonArray toArray = entry.getValue().getAsJsonArray();
                        elementBuilder.to(toArray.get(0).getAsFloat(), toArray.get(1).getAsFloat(),
                                toArray.get(2).getAsFloat());
                    }
                    case "rotation" ->
                            consumeElementRotationJson(elementBuilder.rotation(), entry.getValue().getAsJsonObject());
                    case "shade" -> elementBuilder.shade(entry.getValue().getAsBoolean());
                    case "faces" -> consumeElementFacesJson(elementBuilder, entry.getValue().getAsJsonObject());
                }
            }
        }
    }

    private void consumeElementRotationJson(
            ModelBuilder<BlockModelBuilder>.ElementBuilder.RotationBuilder modelElementRotationBuilder,
            JsonObject elementRotationJson)
    {
        for (Map.Entry<String, JsonElement> entry : elementRotationJson.entrySet())
        {
            switch (entry.getKey()) {
                case "origin" -> {
                    JsonArray jsonArray = entry.getValue().getAsJsonArray();
                    modelElementRotationBuilder.origin(jsonArray.get(0).getAsFloat(), jsonArray.get(1).getAsFloat(),
                            jsonArray.get(2).getAsFloat());
                }
                case "axis" -> modelElementRotationBuilder.axis(Direction.Axis.valueOf(entry.getValue().getAsString()));
                case "angle" -> modelElementRotationBuilder.angle(entry.getValue().getAsFloat());
                case "rescale" -> modelElementRotationBuilder.rescale(entry.getValue().getAsBoolean());
            }
        }
    }

    private void consumeElementFacesJson(ModelBuilder<BlockModelBuilder>.ElementBuilder modelElementsBuilder,
            JsonObject elementFacesJson)
    {
        for (Map.Entry<String, JsonElement> face : elementFacesJson.entrySet())
        {
            ModelBuilder<BlockModelBuilder>.ElementBuilder.FaceBuilder faceBuilder =
                    modelElementsBuilder.face(Direction.byName(face.getKey()));
            for (Map.Entry<String, JsonElement> facePart : face.getValue().getAsJsonObject().entrySet())
            {
                switch (facePart.getKey()) {
                    case "uv" -> {
                        JsonArray jsonArray = facePart.getValue().getAsJsonArray();
                        faceBuilder.uvs(jsonArray.get(0).getAsFloat(), jsonArray.get(1).getAsFloat(),
                                jsonArray.get(2).getAsFloat(), jsonArray.get(3).getAsFloat());
                    }
                    case "texture" -> faceBuilder.texture(facePart.getValue().getAsString());
                    case "cullface" -> faceBuilder.cullface(Direction.byName(facePart.getValue().getAsString()));
                    case "rotation" -> {
                        ModelBuilder.FaceRotation rotation = switch (facePart.getValue().getAsInt()) {
                            case 0 -> ModelBuilder.FaceRotation.ZERO;
                            case 90 -> ModelBuilder.FaceRotation.CLOCKWISE_90;
                            case 180 -> ModelBuilder.FaceRotation.UPSIDE_DOWN;
                            case 270 -> ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90;
                            default -> null;
                        };
                        faceBuilder.rotation(rotation);
                    }
                    case "tintindex" -> faceBuilder.tintindex(facePart.getValue().getAsInt());
                }
            }
        }
    }
}
