package com.lazygalaxy.art.main.animebycountry;

import com.google.gson.JsonObject;
import com.lazygalaxy.common.domain.Country;
import com.lazygalaxy.engine.helper.MongoHelper;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;


public class GenerateAnimeByCountry {

    private static final Logger LOGGER = LogManager.getLogger(GenerateAnimeByCountry.class);
    private static final int PAGE_SIZE = 10;

    private static final int PAGE = 0;

    public static void main(String[] args) throws Exception {
        //List<Country> countries = MongoHelper.getHelper(Country.class).getDocumentsByFilters(Sorts.ascending("name"), Filters.eq("include", true));
        List<Country> countries = MongoHelper.getHelper(Country.class).getDocumentsByFilters(Sorts.ascending("name"), Filters.in("_id", "br", "ca", "eg", "kr", "ar", "en", "tr", "jp", "co", "de"));
        int count = 0;
        for (Country country : countries) {
            if (count++ / PAGE_SIZE == PAGE) {
                String femalePrompt = getPrompt("anime", "woman", country.name, country.capital, country.topAttractions.toArray()[0].toString(), country.conservativeDressCode);
                LOGGER.info(femalePrompt);
                generate(femalePrompt);
                //String malePrompt = getPrompt("anime", "man", country.name, country.capital, country.topAttractions.toArray()[1].toString(), country.conservativeDressCode);
                //LOGGER.info(malePrompt);
                //generate(malePrompt);
                Thread.sleep(10000);
            }
        }
    }

    private static String getPrompt(String style, String protagonist, String country, String captial, String landmark, boolean conservativeDressCode) {
        String posessive = StringUtils.equalsAny(protagonist, "woman", "girl") ? "her" : "his";
        String pronoun = StringUtils.equalsAny(protagonist, "woman", "girl") ? "she" : "he";
        // return "Half Body shot adventurous " + style + " style depiction of a " + protagonist + " from " + country + ",showcasing " + posessive + " heritage." + pronoun + " has " + style + " styled large eyes,hair and mouth and with skin color representative of " + country + " demographics.The " + protagonist + " has a lively demeanor and wears a dynamic and fantasy-inspired themed" + (conservativeDressCode ? " abaya " : " outfit ") + "from " + country + ",embodying " + posessive + " love for " + posessive + " country.The background immerses viewers in " + country + ",with breathtaking scenes of " + country + ". The environment is outdoor and the image type is a digital illustration in a " + style + " style with fantasy vivid colors.The art style combines " + style + " and mythological elements,presenting the " + protagonist + " as a brave hero on a mythical quest.The camera shot is a long shot,capturing the " + protagonist + " spirited pose against the mesmerizing landmark.The camera lens used is a wide-angle lens.The render style is highly detailed with a resolution of 8K.The lighting is a mix of vibrant sunlight and mystical glows.";
        return "Half Body shot adventurous " + style + " style depiction of a " + protagonist + " from " + country + ",showcasing " + posessive + " heritage." + pronoun + " has " + style + " styled large eyes,hair and mouth.The " + protagonist + " has a lively demeanor and wears a dynamic and fantasy-inspired themed" + (conservativeDressCode ? " abaya " : " outfit ") + "from " + country + ",embodying " + posessive + " love for " + posessive + " country.The background immerses viewers in " + country + ",with breathtaking scenes and landmarks.The " + landmark + " is clearly visible in the background.The environment is outdoor and the image type is a digital illustration in a " + style + " style with fantasy vivid colors.The art style combines " + style + " and mythological elements,presenting the " + protagonist + " as a brave hero on a mythical quest.The camera shot is a long shot,capturing the " + protagonist + " spirited pose against the mesmerizing landmark.The camera lens used is a wide-angle lens.The render style is highly detailed with a resolution of 8K.The lighting is a mix of vibrant sunlight and mystical glows.";
    }

    private static void generate(String prompt) throws Exception {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("prompt", prompt);
        jsonRequest.addProperty("negative_prompt", "((((ugly)))), two heads, 3 hands, male, extra head, man, dead eyes, ((text)), signature, (((duplicate))), ((morbid)), ((mutilated)), extra fingers, mutated hands, ((poorly drawn hands)), ((poorly drawn face)), (((mutation))), (((deformed))), blurry, ((bad anatomy)), ((bad proportions)), ((extra limbs)), cloned face, (((disfigured))), (malformed limbs), ((missing arms)), ((missing legs)), (((extra arms))), (((extra legs))), mutated arms, (((long neck))), extra leg, (bad face), (((bad eyes))), ((bad hands, bad feet, missing fingers, cropped)), blurry, old, wide face, ((fused fingers)), ((too many fingers)), amateur drawing, odd, fat, out of frame, obese, cross eyes, crooked eyes, logo, sample watermark");
        jsonRequest.addProperty("modelId", "ac614f96-1082-45bf-be9d-757f2d31c174");
        jsonRequest.addProperty("height", 832);
        jsonRequest.addProperty("width", 640);
        jsonRequest.addProperty("num_images", 2);
        jsonRequest.addProperty("guidance_scale", 7);
        jsonRequest.addProperty("init_strength", 0.4);
        jsonRequest.addProperty("presetStyle", "LEONARDO");
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
                .addHeader("authorization", "Bearer ecb8da6d-8b82-4fb8-86eb-0f563dbadb5a")
                .build();

        Response response = client.newCall(request).execute();
        LOGGER.info(response.body().string());
    }
}
