/**
 * Created by GaPhil on 2019-09-27.
 */

import com.google.cloud.language.v1.*;
import com.google.cloud.language.v1.Document.Type;
import skill.Skill;
import skill.SkillsService;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws Exception {
        // Instantiates a client
        try {
            String text = new String(Files.readAllBytes(Paths.get("interview.txt")));

            SkillsService skillsService = new SkillsService();
            List<Skill> skills = skillsService.loadSkills();
            
            Document doc = Document.newBuilder()
                    .setContent(text)
                    .setType(Type.PLAIN_TEXT)
                    .build();

            LanguageServiceClient language = LanguageServiceClient.create();

            AnalyzeEntitiesRequest request = AnalyzeEntitiesRequest.newBuilder()
                    .setDocument(doc)
                    .setEncodingType(EncodingType.UTF16)
                    .build();

            AnalyzeEntitiesResponse response = language.analyzeEntities(request);

            Set<String> entities = new HashSet<String>();

            // Print the response
            for (Entity entity : response.getEntitiesList()) {
                for (Skill skill : skills) {
                    if (entity.getName().toLowerCase().contains(skill.getValue().toLowerCase())) {
                        entities.add(entity.getName().toLowerCase());
                    }
                }
            }

            for (String ent : entities) {
                System.out.println("This is what we found: " + ent);
            }
        } catch (Exception exception) {
            System.out.print(exception);
        }
    }
}