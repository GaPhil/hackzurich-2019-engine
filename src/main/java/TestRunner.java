import com.google.cloud.language.v1.*;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import nlp.ExtendedSentence;
import nlp.Party;
import nlp.TextProcessor;
import skill.Skill;
import skill.SkillsService;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

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


            // create set with skills tokens
            Set<Token> skillTokens = new HashSet<>();
            for (ExtendedSentence sentence : ext) {
                String sent = sentence.getSentence().getText().getContent();
                String party = sentence.getParty().toString();
                Boolean isQuestion = sentence.isQuestion();
                System.out.println(sent + " " + party + " QUEST: " + isQuestion);

                AnnotateTextResponse annotatedSentence = textProcessor.processAll(sentence.getSentence().getText().getContent());

                Token mostRecentlyAdded = null;
                boolean doAddNextToken = true;
                for (Token token : annotatedSentence.getTokensList()) {

                    if (token.getDependencyEdge().getLabel().equals(DependencyEdge.Label.NEG)) {
                        System.out.println("will not add token: " + token); // TODO should not add token
                        doAddNextToken = false;
                    }

                    for (Skill skill : skills) {
                        int score = FuzzySearch.ratio(token.getText().getContent().toLowerCase(), skill.getName().toLowerCase());
                        if (token.getText().getContent().toLowerCase().contains(skill.getName().toLowerCase()) || score > 90) {
                            System.out.println(token);
                        }
                    }

                    if (doAddNextToken && token.getDependencyEdge().getLabel().equals(DependencyEdge.Label.NEG)) {
                        System.out.println("will remove token: " + mostRecentlyAdded); // TODO we should remove token
                    }

                    doAddNextToken = true;
                }

//                // evaluate semantic graph and remove negated tokens
//                for (Token token : annotatedSentence.getTokensList()) {
//                    if (token.getDependencyEdge().getLabel().equals(DependencyEdge.Label.NEG)) {
//                        System.out.println("token: " + mostRecentlyAdded);
//
//                    }
//                }

                System.out.println("---------------------------------------------------------------------------");
            }

            System.out.println("here come all the tokens");
            for (Token tok : skillTokens) {
                System.out.println(tok.getText().getContent());
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    static private void crawlSemanticGraph(Token token, List<Token> tokens) {
        if (token.getDependencyEdge().getLabel().equals(DependencyEdge.Label.ROOT)) {
            System.out.print(token.getText().getContent() + "(" + token.getDependencyEdge().getLabel() + ")\n\n");
            return;
        }

        if (token.getDependencyEdge().getLabel().equals(DependencyEdge.Label.NEG)) {
            System.out.println(">>>>> Removed token ");
        }

        System.out.print(token.getText().getContent() + "(" + token.getDependencyEdge().getLabel() + ")" + " -> ");
        crawlSemanticGraph(tokens.get(token.getDependencyEdge().getHeadTokenIndex()), tokens);
    }

    static private List<ExtendedSentence> toExtendedSentences(List<Sentence> sentences, boolean explicitParty) {
        List<ExtendedSentence> extendedSentences = new ArrayList<>();

        Party lastParty = Party.UNKNOWN;

        for (Sentence s : sentences) {
            String text = s.getText().getContent().toLowerCase();

            Party party;
            if (explicitParty && text.startsWith("candidate")) {
                lastParty = Party.CANDIDATE;
                party = Party.CANDIDATE;
            } else if (explicitParty && text.startsWith("interviewer")) {
                lastParty = Party.INTERVIEWER;
                party = Party.INTERVIEWER;
            } else if (explicitParty) {
                party = lastParty;
            } else {
                party = Party.UNKNOWN;
            }

            ExtendedSentence ext = new ExtendedSentence(s);
            ext.setParty(party);
            ext.setQuestion(text.endsWith("?"));
            extendedSentences.add(ext);
        }

        return extendedSentences;
    }


}
