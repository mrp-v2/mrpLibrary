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
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.ModelBuilder;

import javax.annotation.Nullable;
import java.io.IOException;
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
            json = PARSER.parse(new InputStreamReader(resource.getInputStream()));
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
            ModelBuilder.Perspective perspective = null;
            switch (entry.getKey())
            {
                case "thirdperson_righthand":
                    perspective = ModelBuilder.Perspective.THIRDPERSON_RIGHT;
                    break;
                case "thirdperson_lefthand":
                    perspective = ModelBuilder.Perspective.THIRDPERSON_LEFT;
                    break;
                case "firstperson_righthand":
                    perspective = ModelBuilder.Perspective.FIRSTPERSON_RIGHT;
                    break;
                case "firstperson_lefthand":
                    perspective = ModelBuilder.Perspective.FIRSTPERSON_LEFT;
                    break;
                case "head":
                    perspective = ModelBuilder.Perspective.HEAD;
                    break;
                case "gui":
                    perspective = ModelBuilder.Perspective.GUI;
                    break;
                case "ground":
                    perspective = ModelBuilder.Perspective.GROUND;
                    break;
                case "fixed":
                    perspective = ModelBuilder.Perspective.FIXED;
            }
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
            switch (entry.getKey())
            {
                case "rotation":
                    modelDisplayTransformBuilder.rotation(jsonArray.get(0).getAsFloat(), jsonArray.get(1).getAsFloat(),
                            jsonArray.get(2).getAsFloat());
                    break;
                case "translation":
                    modelDisplayTransformBuilder
                            .translation(jsonArray.get(0).getAsFloat(), jsonArray.get(1).getAsFloat(),
                                    jsonArray.get(2).getAsFloat());
                    break;
                case "scale":
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
                switch (entry.getKey())
                {
                    case "from":
                        JsonArray jsonArray = entry.getValue().getAsJsonArray();
                        elementBuilder.from(jsonArray.get(0).getAsFloat(), jsonArray.get(1).getAsFloat(),
                                jsonArray.get(2).getAsFloat());
                        break;
                    case "to":
                        jsonArray = entry.getValue().getAsJsonArray();
                        elementBuilder.to(jsonArray.get(0).getAsFloat(), jsonArray.get(1).getAsFloat(),
                                jsonArray.get(2).getAsFloat());
                        break;
                    case "rotation":
                        consumeElementRotationJson(elementBuilder.rotation(), entry.getValue().getAsJsonObject());
                        break;
                    case "shade":
                        elementBuilder.shade(entry.getValue().getAsBoolean());
                        break;
                    case "faces":
                        consumeElementFacesJson(elementBuilder, entry.getValue().getAsJsonObject());
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
            switch (entry.getKey())
            {
                case "origin":
                    JsonArray jsonArray = entry.getValue().getAsJsonArray();
                    modelElementRotationBuilder.origin(jsonArray.get(0).getAsFloat(), jsonArray.get(1).getAsFloat(),
                            jsonArray.get(2).getAsFloat());
                    break;
                case "axis":
                    modelElementRotationBuilder.axis(Direction.Axis.valueOf(entry.getValue().getAsString()));
                    break;
                case "angle":
                    modelElementRotationBuilder.angle(entry.getValue().getAsFloat());
                    break;
                case "rescale":
                    modelElementRotationBuilder.rescale(entry.getValue().getAsBoolean());
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
                switch (facePart.getKey())
                {
                    case "uv":
                        JsonArray jsonArray = facePart.getValue().getAsJsonArray();
                        faceBuilder.uvs(jsonArray.get(0).getAsFloat(), jsonArray.get(1).getAsFloat(),
                                jsonArray.get(2).getAsFloat(), jsonArray.get(3).getAsFloat());
                        break;
                    case "texture":
                        faceBuilder.texture(facePart.getValue().getAsString());
                        break;
                    case "cullface":
                        faceBuilder.cullface(Direction.byName(facePart.getValue().getAsString()));
                        break;
                    case "rotation":
                        ModelBuilder.FaceRotation rotation = null;
                        switch (facePart.getValue().getAsInt())
                        {
                            case 0:
                                rotation = ModelBuilder.FaceRotation.ZERO;
                                break;
                            case 90:
                                rotation = ModelBuilder.FaceRotation.CLOCKWISE_90;
                                break;
                            case 180:
                                rotation = ModelBuilder.FaceRotation.UPSIDE_DOWN;
                                break;
                            case 270:
                                rotation = ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90;
                        }
                        faceBuilder.rotation(rotation);
                        break;
                    case "tintindex":
                        faceBuilder.tintindex(facePart.getValue().getAsInt());
                }
            }
        }
    }
}
