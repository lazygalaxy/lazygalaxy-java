package com.lazygalaxy.art.main.animalselfies;

import com.google.gson.JsonObject;
import com.lazygalaxy.common.domain.Country;
import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.engine.util.PropertiesUtil;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;


public class GenerateAnimalSefies {

    private static final Logger LOGGER = LogManager.getLogger(GenerateAnimalSefies.class);
    private static final int PAGE_SIZE = 9;

    private static final int PAGE = 2;

    public static void main(String[] args) throws Exception {
        List<Country> countries = MongoHelper.getHelper(Country.class).getDocumentsByFilters(Sorts.ascending("name"), Filters.exists("nationalAnimal"), Filters.exists("topAttractions"));
        int count = 0;
        for (Country country : countries) {
            for (String attraction : country.topAttractions) {
                if (count++ / PAGE_SIZE == PAGE) {
                    String prompt = getPrompt(country.nationalAnimal, attraction, country.name);
                    LOGGER.info(prompt);
                    generate(prompt);
                    Thread.sleep(5000);
                }
            }
        }
    }

    private static String getPrompt(String protagonist, String attraction, String country) {
        return "A smiling " + protagonist + " animal taking a selfie photo of himself, with the " + attraction + " in " + country + " showing in the background. The photo should be a zoomed in head shot of the animal.  Create a detailed digital illustration that combines realism and fantasy. Use a wide lens placed approximately 50 millimeters away from the animal.";
    }

    private static void generate(String prompt) throws Exception {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("prompt", prompt);
        jsonRequest.addProperty("negative_prompt", "((((ugly)))), two heads, 3 hands, male, extra head, man, dead eyes, ((text)), signature, (((duplicate))), ((morbid)), ((mutilated)), extra fingers, mutated hands, ((poorly drawn hands)), ((poorly drawn face)), (((mutation))), (((deformed))), blurry, ((bad anatomy)), ((bad proportions)), ((extra limbs)), cloned face, (((disfigured))), (malformed limbs), ((missing arms)), ((missing legs)), (((extra arms))), (((extra legs))), mutated arms, (((long neck))), extra leg, (bad face), (((bad eyes))), ((bad hands, bad feet, missing fingers, cropped)), blurry, old, wide face, ((fused fingers)), ((too many fingers)), amateur drawing, odd, fat, out of frame, obese, cross eyes, crooked eyes, logo, sample watermark, text, logo");
        jsonRequest.addProperty("modelId", "ac614f96-1082-45bf-be9d-757f2d31c174");
        jsonRequest.addProperty("height", 832);
        jsonRequest.addProperty("width", 832);
        jsonRequest.addProperty("num_images", 2);
        jsonRequest.addProperty("guidance_scale", 7);
        jsonRequest.addProperty("init_strength", 0.4);
        jsonRequest.addProperty("presetStyle", "LEONARDO");
        jsonRequest.addProperty("scheduler", "LEONARDO");
        jsonRequest.addProperty("public", false);
        jsonRequest.addProperty("promptMagic", false);
        jsonRequest.addProperty("tiling", false);
        RequestBody body = RequestBody.create(mediaType, jsonRequest.toString());
        LOGGER.info(jsonRequest.toString());

        Request request = new Request.Builder()
                .url("https://cloud.leonardo.ai/api/rest/v1/generations")
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .addHeader("authorization", "Bearer " + PropertiesUtil.getLeonardoToken())
                .build();

        Response response = client.newCall(request).execute();
        LOGGER.info(response.body().string());
    }
}
