/**
 * Created by GaPhil on 2019-09-27.
 */

import com.google.cloud.language.v1.AnalyzeEntitiesResponse;
import com.google.cloud.language.v1.Entity;
import nlp.TextProcessor;
import skill.Skill;
import skill.SkillsService;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static spark.Spark.get;

public class Main {
    public static void main(String[] args) throws Exception {
        get("/hello", (req, res) -> "Hello World");
//
//
//        try {
//            String text = new String(Files.readAllBytes(Paths.get("interview.txt")));
//
//            SkillsService skillsService = new SkillsService();
//            List<Skill> skills = skillsService.loadSkills();
//
//            TextProcessor textProcessor = new TextProcessor();
//            AnalyzeEntitiesResponse response = textProcessor.processEntities(text);
//
//            Set<String> entities = new HashSet<String>();
//
//            // Print the response
//            for (Entity entity : response.getEntitiesList()) {
//                for (Skill skill : skills) {
//                    if (entity.getName().toLowerCase().contains(skill.getValue().toLowerCase())) {
//                        entities.add(entity.getName().toLowerCase());
//                    }
//                }
//            }
//
//            for (String ent : entities) {
//                System.out.println("This is what we found: " + ent);
//            }
//        } catch (Exception exception) {
//            System.out.print(exception);
//        }
    }
}