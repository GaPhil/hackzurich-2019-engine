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

import static spark.Spark.get;
import static spark.Spark.post;

public class Main {

    public static void main(String[] args) {
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
}