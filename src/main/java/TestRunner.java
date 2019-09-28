import com.google.cloud.language.v1.AnalyzeSyntaxResponse;
import com.google.cloud.language.v1.Sentence;
import com.google.cloud.language.v1.Token;
import nlp.ExtendedSentence;
import nlp.Party;
import nlp.TextProcessor;
import skill.Skill;
import skill.SkillsService;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {
    public static void main(String[] args) {

        try {
            SkillsService skillsService = new SkillsService();
            List<Skill> skills = skillsService.loadSkills();

            String text = new String(Files.readAllBytes(Paths.get("interview.txt")));

            TextProcessor textProcessor = new TextProcessor();
            AnalyzeSyntaxResponse res = textProcessor.processSyntax(text);

            List<Sentence> sentences = res.getSentencesList();

            List<ExtendedSentence> ext = toExtendedSentences(sentences, true);

            for (ExtendedSentence sentence : ext) {
                String sent = sentence.getSentence().getText().getContent();
                String party = sentence.getParty().toString();
                String isQuestion = Boolean.toString(sentence.isQuestion());
                System.out.println(sent +  " " + party + " QUEST:" + isQuestion);

                System.out.println("---------------------------------------------------------------------------");
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    static private List<ExtendedSentence> toExtendedSentences(List<Sentence> sentences, boolean explicitParty) {
        List<ExtendedSentence> extendedSentences = new ArrayList<>();

        Party lastParty = Party.UNKNOWN;

        for (Sentence s : sentences) {
            String text = s.getText().getContent().toLowerCase();

            Party party;
            if (explicitParty && text.startsWith("candidate")) {
                lastParty = Party.CANDIDATE;
                party =  Party.CANDIDATE;
            } else if (explicitParty && text.startsWith("interviewer")) {
                lastParty = Party.INTERVIEWER;
                party =  Party.INTERVIEWER;
            } else if (explicitParty) {
                party =  lastParty;
            } else {
                party =  Party.UNKNOWN;
            }

            ExtendedSentence ext = new ExtendedSentence(s);
            ext.setParty(party);
            ext.setQuestion(text.endsWith("?"));
            extendedSentences.add(ext);
        }

        return extendedSentences;
    }


}
