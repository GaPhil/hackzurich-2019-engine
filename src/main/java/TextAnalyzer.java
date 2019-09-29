import com.google.cloud.language.v1.*;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import nlp.ExtendedSentence;
import nlp.Party;
import nlp.TextProcessor;
import skill.Skill;
import skill.SkillsService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TextAnalyzer {
    public static List<Skill> analyze(String text) {

        SkillsService skillsService = new SkillsService();
        List<Skill> skills = skillsService.loadSkills();

        TextProcessor textProcessor = new TextProcessor();
        AnalyzeSyntaxResponse res = textProcessor.processSyntax(text);

        List<Sentence> sentences = res.getSentencesList();
        List<ExtendedSentence> extendedSentences = toExtendedSentences(sentences, true);

        List<Token> conversationContext = new ArrayList<>();
        List<Skill> approvedSkills = new ArrayList<>();

        for (ExtendedSentence sentence : extendedSentences) {
            // progress indicator
            System.out.print(".");

            // run NLP on sentence
            AnnotateTextResponse annotatedSentence = textProcessor.processAll(sentence.getSentence());

            List<Token> skillTokens = new ArrayList<>();
            boolean hasNegations = false;

            double confidence = getSentenceConfidence(sentence.getSentence());

            for (Token token : annotatedSentence.getTokensList()) {
                // get all tokens which are whitelisted skills
                for (Skill skill : skills) {
                    int score = FuzzySearch.ratio(token.getText().getContent().toLowerCase(), skill.getName().toLowerCase());
                    if (token.getText().getContent().toLowerCase().contains(skill.getName().toLowerCase()) || score > 90) {
                        skillTokens.add(token);
                    }
                }

                // check if answer contains negations
                if (token.getDependencyEdge().getLabel().equals(DependencyEdge.Label.NEG)) {
                    hasNegations = true;
                }
            }

            // if skills introduced by interviewer or in candidate question then add to context
            if (sentence.isQuestion() || sentence.isInterviewer()) {
                conversationContext.addAll(skillTokens);
                continue;
            }

            // select either current tokens or from conversation context
            if (!hasNegations && !skillTokens.isEmpty()) {
                for (Token tk : skillTokens) {
                    approvedSkills.add(new Skill(tk, confidence));
                }
            } else if (!hasNegations && !conversationContext.isEmpty()) {
                for (Token tk : conversationContext) {
                    approvedSkills.add(new Skill(tk, confidence));
                }
            }

            conversationContext.clear();
        }

        return approvedSkills;
    }

    static HashMap<String, Double> dedupeList(List<Skill> skills) {
        HashMap<String, Double> skillSet = new HashMap<>();

        for (Skill skill : skills) {
            String normalizedSkillName = skill.getName().trim().replaceAll("[^a-zA-Z0-9+]", "");
            if (skillSet.containsKey(normalizedSkillName)) {
                skillSet.replace(normalizedSkillName, skill.getValue() * skillSet.get(normalizedSkillName));
            } else {
                skillSet.put(normalizedSkillName, skill.getValue());
            }
        }

        return skillSet;
    }

    static private List<ExtendedSentence> toExtendedSentences(List<Sentence> sentences, boolean explicitParty) {
        List<ExtendedSentence> extendedSentences = new ArrayList<>();

        Party lastParty = Party.UNKNOWN;

        for (Sentence s : sentences) {
            String text = s.getText().getContent().toLowerCase();

            Party party;
            if (explicitParty && text.startsWith("candidate:")) {
                text = text.replaceFirst("candidate: ", "");
                lastParty = Party.CANDIDATE;
                party = Party.CANDIDATE;
            } else if (explicitParty && text.startsWith("interviewer:")) {
                text = text.replaceFirst("interviewer: ", "");
                lastParty = Party.INTERVIEWER;
                party = Party.INTERVIEWER;
            } else if (explicitParty) {
                party = lastParty;
            } else {
                party = Party.UNKNOWN;
            }
            ExtendedSentence ext = new ExtendedSentence(text);
            ext.setParty(party);
            ext.setQuestion(text.endsWith("?"));
            extendedSentences.add(ext);
        }

        return extendedSentences;
    }

    static private double getSentenceConfidence(String sentence) {
        double confidence = 1;

        // extensible list of modifiers
        if (sentence.contains("a bit")) confidence = confidence - 0.3;
        else if (sentence.contains("a little")) confidence = confidence - 0.5;
        else if (sentence.contains("less")) confidence = confidence - 0.2;
        else if (sentence.contains("very little"))
            confidence = confidence - 0.8;
        else if (sentence.contains("a lot")) confidence = confidence + 0.7;
        else if (sentence.contains("very much")) confidence = confidence + 0.8;
        else if (sentence.contains("more")) confidence = confidence + 0.2;

        return confidence;
    }
}
