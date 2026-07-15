package api;

import model.NoteDocument;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class LoadService {

    private static final String FILE_HEADER = "NOTE_APP_V1";

    public NoteDocument load(Path filePath) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(
                filePath,
                StandardCharsets.UTF_8
        )) {
            String header = reader.readLine();

            if (!FILE_HEADER.equals(header)) {
                throw new IOException(
                        "Το αρχείο δεν είναι έγκυρο Note App αρχείο."
                );
            }

            int leftCount = readCount(
                    reader.readLine(),
                    "LEFT_COUNT"
            );

            int rightCount = readCount(
                    reader.readLine(),
                    "RIGHT_COUNT"
            );

            if (leftCount < 1 || leftCount > 5) {
                throw new IOException(
                        "Μη έγκυρος αριθμός αριστερών text areas."
                );
            }

            if (rightCount < 1 || rightCount > 5) {
                throw new IOException(
                        "Μη έγκυρος αριθμός δεξιών text areas."
                );
            }

            List<String> leftTexts =
                    createEmptyTextList(leftCount);

            List<String> rightTexts =
                    createEmptyTextList(rightCount);

            String line;

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                readTextLine(
                        line,
                        leftTexts,
                        rightTexts
                );
            }

            return new NoteDocument(
                    leftTexts,
                    rightTexts
            );
        }
    }

    private int readCount(
            String line,
            String expectedKey
    ) throws IOException {

        if (line == null) {
            throw new IOException(
                    "Το αρχείο είναι ελλιπές."
            );
        }

        String prefix = expectedKey + "=";

        if (!line.startsWith(prefix)) {
            throw new IOException(
                    "Αναμενόταν η τιμή " + expectedKey + "."
            );
        }

        String value = line.substring(prefix.length());

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            throw new IOException(
                    "Μη έγκυρη τιμή για " + expectedKey + ".",
                    exception
            );
        }
    }

    private List<String> createEmptyTextList(int count) {
        List<String> texts = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            texts.add("");
        }

        return texts;
    }

    private void readTextLine(
            String line,
            List<String> leftTexts,
            List<String> rightTexts
    ) throws IOException {

        int equalsIndex = line.indexOf('=');

        if (equalsIndex < 0) {
            throw new IOException(
                    "Μη έγκυρη γραμμή μέσα στο αρχείο."
            );
        }

        String key = line.substring(0, equalsIndex);
        String encodedText =
                line.substring(equalsIndex + 1);

        String decodedText;

        try {
            byte[] decodedBytes =
                    Base64.getDecoder().decode(encodedText);

            decodedText = new String(
                    decodedBytes,
                    StandardCharsets.UTF_8
            );

        } catch (IllegalArgumentException exception) {
            throw new IOException(
                    "Το αποθηκευμένο κείμενο είναι κατεστραμμένο.",
                    exception
            );
        }

        if (key.startsWith("LEFT_")) {
            int index = readTextIndex(key, "LEFT_");

            if (index >= leftTexts.size()) {
                throw new IOException(
                        "Μη έγκυρο αριστερό text area."
                );
            }

            leftTexts.set(index, decodedText);
            return;
        }

        if (key.startsWith("RIGHT_")) {
            int index = readTextIndex(key, "RIGHT_");

            if (index >= rightTexts.size()) {
                throw new IOException(
                        "Μη έγκυρο δεξί text area."
                );
            }

            rightTexts.set(index, decodedText);
            return;
        }

        throw new IOException(
                "Άγνωστο πεδίο μέσα στο αρχείο: " + key
        );
    }

    private int readTextIndex(
            String key,
            String prefix
    ) throws IOException {

        String indexText = key.substring(prefix.length());

        try {
            int index = Integer.parseInt(indexText);

            if (index < 0) {
                throw new IOException(
                        "Μη έγκυρο text area index."
                );
            }

            return index;

        } catch (NumberFormatException exception) {
            throw new IOException(
                    "Μη έγκυρο text area index.",
                    exception
            );
        }
    }
}