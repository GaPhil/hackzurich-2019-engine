package skill;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SkillsService {
    public List<Skill> loadSkills() {
        BufferedReader reader;
        ArrayList<Skill> skills = new ArrayList<Skill>();

        try {
            // load file
            reader = new BufferedReader(new FileReader("src/main/resources/skills.txt"));

            // create a Skill for each line
            String value = reader.readLine();
            while (value != null) {
                skills.add(new Skill(value.trim().toLowerCase()));
                value = reader.readLine();
            }
            // cleanup
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return skills;
    }

    public List<String> loadSkillsStrings() {
        BufferedReader reader;
        ArrayList<String> skills = new ArrayList<String>();

        try {
            // load file
            reader = new BufferedReader(new FileReader("src/main/resources/skills.txt"));

            // create a Skill for each line
            String value = reader.readLine();
            while (value != null) {
                skills.add(value);
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
