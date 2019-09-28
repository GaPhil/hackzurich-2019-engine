import com.google.cloud.language.v1.Token;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TestRunner {
    public static void main(String[] args) {

        try {
            String text = new String(Files.readAllBytes(Paths.get("interview.txt")));

            List<Token> approvedSkillTokens = TextAnalyzer.analyze(text);

            System.out.println("\n\nDetected tokens:");
            for (String tok : TextAnalyzer.dedupeList(approvedSkillTokens)) {
                System.out.println(tok);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
