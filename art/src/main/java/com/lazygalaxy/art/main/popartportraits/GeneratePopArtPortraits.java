package com.lazygalaxy.art.main.popartportraits;

import com.google.gson.JsonObject;
import com.lazygalaxy.engine.util.PropertiesUtil;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class GeneratePopArtPortraits {

    private static final Logger LOGGER = LogManager.getLogger(GeneratePopArtPortraits.class);
    private static final int PAGE_SIZE = 10;

    private static final int PAGE = 3;

    private static String[] protagonists = new String[]{"Albert Einstein", "Marie Curie", "Isaac Newton", "Charles Darwin", "Nikola Tesla", "Galileo Galilei", "Ada Lovelace", "Pythagoras of Samos", "Carl Linnaeus", "Rosalind Franklin",
            "Elon Musk", "Leonardo da Vinci", "Thomas Edison", "Louis Pasteur", "Michael Faraday", "Archimedes", "Aristotle", "Stephen Hawking", "Jane Goodall", "Nicolaus Copernicus",
            "Neil deGrasse Tyson", "Rachel Carson", "George Washington Carver", "Alan Turing", "Alexander Graham Bell", "Tim Berners-Lee", "Linus Pauling", "Mary Anning", "Robert Boyle", "Johannes Kepler",
            "Grace Hopper", "Steve Jobs", "Plato", "Bill Gates", "Henry Ford", "Benjamin Franklin", "Wright brothers", "Zhang Heng", "Tu Youyou", "William Shakespeare"
    };

    public static void main(String[] args) throws Exception {
        int count = 0;
        for (String protagonist : protagonists) {
            if (count++ / PAGE_SIZE == PAGE) {
                String prompt = getPrompt(protagonist);
                LOGGER.info(prompt);
                generate(prompt);
                Thread.sleep(5000);
            }
        }
    }

    private static String getPrompt(String protagonist) {
        return "Generate an artistic image of " + protagonist + ", inspired by Andy Warhol's pop art style. Create a portrait that uses Warhol's iconic techniques with vibrant pastel color combinations. The image should capture " + protagonist + "'s personality and influence within the industry. Use a 35mm lens for a close-up shot, and emphasize the visual impact of Warhol's style through vivid colors and bold composition.";
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
        jsonRequest.addProperty("num_images", 4);
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
