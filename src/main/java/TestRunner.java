import skill.Skill;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class TestRunner {
    public static void main(String[] args) {

        try {
            String text = new String(Files.readAllBytes(Paths.get("interview.txt")));

            List<Skill> approvedSkillTokens = TextAnalyzer.analyze(text);

            System.out.println("\n\nDetected tokens:");
            for (Map.Entry<String, Double> entry : TextAnalyzer.dedupeList(approvedSkillTokens).entrySet()) {
                System.out.println("key: " + entry.getKey() + " - " + "value: " + entry.getValue());
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}