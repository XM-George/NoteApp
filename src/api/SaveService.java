package api;

import model.NoteDocument;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;

public class SaveService {

    private static final String FILE_HEADER = "NOTE_APP_V1";

    public void save(
            NoteDocument document,
            Path filePath
    ) throws IOException {

        try (BufferedWriter writer = Files.newBufferedWriter(
                filePath,
                StandardCharsets.UTF_8
        )) {
            writer.write(FILE_HEADER);
            writer.newLine();

            writer.write(
                    "LEFT_COUNT=" + document.getLeftAreaCount()
            );
            writer.newLine();

            writer.write(
                    "RIGHT_COUNT=" + document.getRightAreaCount()
            );
            writer.newLine();

            writeTexts(
                    writer,
                    "LEFT",
                    document.getLeftTexts()
            );

            writeTexts(
                    writer,
                    "RIGHT",
                    document.getRightTexts()
            );
        }
    }

    private void writeTexts(
            BufferedWriter writer,
            String side,
            List<String> texts
    ) throws IOException {

        for (int i = 0; i < texts.size(); i++) {
            String encodedText = Base64.getEncoder()
                    .encodeToString(
                            texts.get(i).getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );

            writer.write(
                    side + "_" + i + "=" + encodedText
            );

            writer.newLine();
        }
    }
}