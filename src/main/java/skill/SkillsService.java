package skill;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SkillsService {

    /**
     * Load all skills from txt file.
     * @return List<Skill>
     */
    public List<Skill> loadSkills() {
        BufferedReader reader;
        ArrayList<Skill> skills = new ArrayList<Skill>();

        try {
            // load file
            reader = new BufferedReader(new FileReader("src/main/java/skill/skills.txt"));

            // create a Skill for each line
            String value = reader.readLine();
            while (value != null) {
                skills.add(new Skill(value.trim()));
                value = reader.readLine();
            }

            // cleanup
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return skills;
    }
}
