package model;

import java.util.ArrayList;
import java.util.List;

public class NoteDocument {

    private final List<String> leftTexts;
    private final List<String> rightTexts;

    public NoteDocument(
            List<String> leftTexts,
            List<String> rightTexts
    ) {
        this.leftTexts = new ArrayList<>(leftTexts);
        this.rightTexts = new ArrayList<>(rightTexts);
    }

    public List<String> getLeftTexts() {
        return new ArrayList<>(leftTexts);
    }

    public List<String> getRightTexts() {
        return new ArrayList<>(rightTexts);
    }

    public int getLeftAreaCount() {
        return leftTexts.size();
    }

    public int getRightAreaCount() {
        return rightTexts.size();
    }
}