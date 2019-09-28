import com.google.cloud.language.v1.*;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import nlp.ExtendedSentence;
import nlp.Party;
import nlp.TextProcessor;
import skill.Skill;
import skill.SkillsService;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TextAnalyzer {
    public static List<Token> analyze(String text) {

        SkillsService skillsService = new SkillsService();
        List<Skill> skills = skillsService.loadSkills();

        TextProcessor textProcessor = new TextProcessor();
        AnalyzeSyntaxResponse res = textProcessor.processSyntax(text);

        List<Sentence> sentences = res.getSentencesList();
        List<ExtendedSentence> extendedSentences = toExtendedSentences(sentences, true);

        List<Token> conversationContext = new ArrayList<>();
        List<Token> approvedSkillTokens = new ArrayList<>();

        for (ExtendedSentence sentence : extendedSentences) {
            // progress indicator
            System.out.print(".");

            // run NLP on sentence
            AnnotateTextResponse annotatedSentence = textProcessor.processAll(sentence.getSentence());

            List<Token> skillTokens = new ArrayList<>();
            boolean hasNegations = false;

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
            if (!hasNegations && !skills.isEmpty()) {
                approvedSkillTokens.addAll(skillTokens);
            } else if (!hasNegations && !conversationContext.isEmpty()) {
                approvedSkillTokens.addAll(conversationContext);
            }

            conversationContext.clear();
        }

        return approvedSkillTokens;


    }

    static Set<String> dedupeList(List<Token> tokens) {
        Set<String> tokensSet = new HashSet<>();

        for (Token token : tokens) {
            String tokenName = token.getText().getContent();
            tokensSet.add(tokenName.trim().replaceAll("[^a-zA-Z0-9 ]", ""));
        }

        return tokensSet;
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


}
