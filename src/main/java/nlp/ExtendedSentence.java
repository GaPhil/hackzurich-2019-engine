package nlp;

public class ExtendedSentence {

    private String sentence;
    private Party party;
    private boolean isQuestion;

    public ExtendedSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
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

    public boolean isInterviewer() {
        return this.party.equals(Party.INTERVIEWER);
    }

    public boolean isCandidate() {
        return this.party.equals(Party.CANDIDATE);
    }
}
