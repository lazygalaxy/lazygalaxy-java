package com.lazygalaxy.art.main.superheroparents;

import com.google.gson.JsonObject;
import com.lazygalaxy.engine.util.PropertiesUtil;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class GenerateSuperheroParents {

    private static final Logger LOGGER = LogManager.getLogger(GenerateSuperheroParents.class);
    private static final int PAGE_SIZE = 10;

    private static final int PAGE = 0;

    private static String[] protagonists = new String[]{"Batman", "Spiderman", "Superman", "Wonder Woman", "Incredible Hulk", "Captain America", "Iron Man", "Wolverine", "Joker", "Doctor Strange", "Thor", "Aquaman", "The Flash", "Black Adam", "Deadpool", "Supergirl", "Green Arrow", "DareDevil", "Green Lantern", "Green Goblin"};

    private static String[] actions = new String[]{"reading a bedtime story to kid", "picking up toys in the kids room"};


    public static void main(String[] args) throws Exception {
        int count = 0;
        for (String protagonist : protagonists) {
            for (String action : actions) {
                if (count++ / PAGE_SIZE == PAGE) {
                    String prompt = getPrompt(protagonist, action);
                    LOGGER.info(prompt);
                    generate(prompt);
                    Thread.sleep(5000);
                }
            }
        }
    }

    private static String getPrompt(String protagonist, String action) {
        return "Digital illustration of " + protagonist + " as a parent, " + action + ". Adopt a stylized, comic book art approach influenced by artists like Steve Ditko and John Romita Sr. Use a (((wide-angle lens))) for a dynamic shot, showcasing environment. Blend comic book styles to create a visually engaging composition. Experiment with fantasy vivid colors to enhance the playful atmosphere. Consider adding subtle superhero-themed elements to emphasize the character's identity. Render with a mix of detailed and stylized features.";
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
