package com.lazygalaxy.art.main.animalselfies;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lazygalaxy.engine.util.PropertiesUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;

public class DownloadAnimalSelfies {
    private static final Logger LOGGER = LogManager.getLogger(DownloadAnimalSelfies.class);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    public static void main(String[] args) throws Exception {
        LOGGER.info(getGenerations());
    }

    private static String getGenerations() throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://cloud.leonardo.ai/api/rest/v1/generations/user/5f70e682-bd65-443b-8288-4b69b4bb2d81")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("authorization", "Bearer " + PropertiesUtil.getLeonardoToken())
                .build();

        Response response = client.newCall(request).execute();
        JsonObject jsonObject = new JsonParser().parse(response.body().string()).getAsJsonObject();
        JsonArray generationElements = jsonObject.getAsJsonArray("generations");

        for (JsonElement generationElement : generationElements.getAsJsonArray().asList()) {
            JsonObject generationObject = generationElement.getAsJsonObject();
            String prompt = generationObject.get("prompt").getAsString();
            if (StringUtils.contains(prompt, "animal taking a selfie photo of himself")) {
                LOGGER.info(prompt);
                String protagonist = StringUtils.substringBetween(prompt, "A smiling ", " animal taking a selfie photo ").trim();
                String country = StringUtils.substringBetween(prompt, " in ", " showing in the background.").trim();
                String attraction = StringUtils.substringBetween(prompt, " with the ", " in " + country).trim();
                JsonArray imageElements = generationObject.getAsJsonArray("generated_images");
                for (JsonElement imageElement : imageElements.getAsJsonArray().asList()) {
                    JsonObject imageObject = imageElement.getAsJsonObject();
                    String url = imageObject.get("url").getAsString();
                    String id = imageObject.get("id").getAsString();
                    LOGGER.info(url);

                    String filename = "C:\\Users\\vangos\\Desktop\\Projects\\Animal Selfie World Tour\\Collection\\" + (protagonist + "_" + country + "_" + attraction + "_" + id).toLowerCase() + ".jpg";
                    LOGGER.info(filename);

                    FileUtils.copyURLToFile(
                            new URL(url),
                            new File(filename),
                            5000,
                            10000);
                }
            }
        }
        return "";
    }
}
