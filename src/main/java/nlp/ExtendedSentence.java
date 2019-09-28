package nlp;

import com.google.cloud.language.v1.Sentence;

public class ExtendedSentence {

    private Sentence sentence;
    private Party party;
    private boolean isQuestion;

    public ExtendedSentence(Sentence sentence) {
        this.sentence = sentence;
    }

    public Sentence getSentence() {
        return sentence;
    }

    public void setSentence(Sentence sentence) {
        this.sentence = sentence;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public boolean isQuestion() {
        return isQuestion;
    }

    public void setQuestion(boolean question) {
        isQuestion = question;
    }
}