/**
 * Created by GaPhil on 2019-09-27.
 */

import com.google.cloud.language.v1.AnalyzeEntitiesResponse;
import com.google.cloud.language.v1.Entity;
import nlp.TextProcessor;
import skill.Skill;
import skill.SkillsService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        enableCORS("*", "*", "");

        SkillsService skillsService = new SkillsService();
        List<Skill> skills = skillsService.loadSkills();

        get("/api/skills", (request, response) -> skills, new JsonTransformer());

        post("/api/analyse", (request, response) -> {
            TextProcessor textProcessor = new TextProcessor();
            AnalyzeEntitiesResponse res = textProcessor.processEntities(request.body());
            Set<String> entities = new HashSet<>();
            for (Entity entity : res.getEntitiesList()) {
                for (Skill skill : skills) {
                    if (entity.getName().toLowerCase().contains(skill.getValue().toLowerCase())) {
                        entities.add(entity.getName().toLowerCase());
                    }
                }
            }
            return entities;
        }, new JsonTransformer());
    }


    // Enables CORS on requests. This method is an initialization method and should be called once.
    private static void enableCORS(final String origin, final String methods, final String headers) {

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
            // Note: this may or may not be necessary in your particular application
            response.type("application/json");
        });
    }
}