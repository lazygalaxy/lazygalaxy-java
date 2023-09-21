package com.lazygalaxy.art.main.superherosportstars;

import com.google.gson.JsonObject;
import com.lazygalaxy.common.domain.Country;
import com.lazygalaxy.engine.helper.MongoHelper;
import com.lazygalaxy.engine.util.PropertiesUtil;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;


public class GenerateSuperheroSportStars {

    private static final Logger LOGGER = LogManager.getLogger(GenerateSuperheroSportStars.class);
    private static final int PAGE_SIZE = 10;

    private static final int PAGE = 0;

    public static void main(String[] args) throws Exception {
        // List<Country> countries = MongoHelper.getHelper(Country.class).getDocumentsByFilters(Sorts.ascending("name"), Filters.in("_id", "en"));
        List<Country> countries = MongoHelper.getHelper(Country.class).getDocumentsByFilters(Sorts.ascending("name"), Filters.exists("sportPeople"));
        int count = 0;
        for (Country country : countries) {
            for (String protagonist : country.sportPeople)
                if (count++ / PAGE_SIZE == PAGE) {
                    String prompt = getPrompt(protagonist, String.join(",", country.flagColors), country.name);
                    LOGGER.info(prompt);
                    generate(prompt);
                    Thread.sleep(5000);
                }
        }
    }

    private static String getPrompt(String protagonist, String colors, String country) {
        String posessive = StringUtils.containsAny(protagonist.toLowerCase(), "woman", "girl", "steffi", "serena", "mithali", "amanda", "ronda", "danica") ? "her" : "his";
        String pronoun = StringUtils.containsAny(protagonist.toLowerCase(), "woman", "girl", "steffi", "serena", "mithali", "amanda", "ronda", "danica") ? "she" : "he";
        return "Create a Marvel comic book style, stunning half body portrait of " + protagonist + " as a superhero. " + protagonist + " is a sport legend from " + country + ", so blend " + posessive + " iconic appearance with the superhero aesthetics of the Marvel comic book universe. " + pronoun + " stands confident in a dramatic action pose. " + protagonist + " is wearing " + colors + " high-tech superhero suit that incorporates elements inspired by classic Marvel heroes and the sport " + pronoun + " became famous for. The background is a dystopian landscape from " + country + ". The art style should capture the essence of Marvel comics, with bold lines and vivid colors in a comic book style. Utilize a 70mm lens for a balanced perspective. Opt for a medium shot view to emphasize the superhero details. The resolution should be 4K for exceptional detail.";
    }

    private static void generate(String prompt) throws Exception {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("prompt", prompt);
        jsonRequest.addProperty("negative_prompt", "((((ugly)))), two heads, 3 hands, male, extra head, man, dead eyes, ((text)), signature, (((duplicate))), ((morbid)), ((mutilated)), extra fingers, mutated hands, ((poorly drawn hands)), ((poorly drawn face)), (((mutation))), (((deformed))), blurry, ((bad anatomy)), ((bad proportions)), ((extra limbs)), cloned face, (((disfigured))), (malformed limbs), ((missing arms)), ((missing legs)), (((extra arms))), (((extra legs))), mutated arms, (((long neck))), extra leg, (bad face), (((bad eyes))), ((bad hands, bad feet, missing fingers, cropped)), blurry, old, wide face, ((fused fingers)), ((too many fingers)), amateur drawing, odd, fat, out of frame, obese, cross eyes, crooked eyes, logo, sample watermark, text, logo");
        jsonRequest.addProperty("modelId", "ac614f96-1082-45bf-be9d-757f2d31c174");
        jsonRequest.addProperty("height", 832);
        jsonRequest.addProperty("width", 640);
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
