package gui;

import api.TextRelated;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.undo.UndoManager;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CannotRedoException;

public class A5SectionPanel extends JPanel {

    private static final int MIN_AREAS = 1;
    private static final int MAX_AREAS = 5;

    private final List<JTextArea> textAreas = new ArrayList<>();

    private DocumentListener documentChangeListener;

    private final TextRelated textRelated = new TextRelated();

    public A5SectionPanel() {
        setBackground(Color.WHITE);

        setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );

        setAreaCount(1);
    }

    public void setAreaCount(int areaCount) {
        if (areaCount < MIN_AREAS || areaCount > MAX_AREAS) {
            throw new IllegalArgumentException(
                    "Area count must be between 1 and 5."
            );
        }

        List<String> previousTexts = getTexts();

        removeAll();
        textAreas.clear();

        setLayout(
                new GridLayout(
                        areaCount,
                        1,
                        0,
                        8
                )
        );

        for (int i = 0; i < areaCount; i++) {
            JTextArea textArea = createTextArea();

            if (i < previousTexts.size()) {
                textArea.setText(previousTexts.get(i));
            }

            textAreas.add(textArea);
            add(textArea);
        }

        revalidate();
        repaint();
    }

    private JTextArea createTextArea() {
        JTextArea textArea = new JTextArea();

        textArea.setFont(
                new Font(
                        Font.MONOSPACED,
                        Font.PLAIN,
                        16
                )
        );

        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        textArea.setMargin(new Insets(0, 0, 0, 0));

        textArea.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(210, 210, 210)
                        ),
                        BorderFactory.createEmptyBorder(
                                5,   // πάνω
                                8,  // αριστερά
                                5,   // κάτω
                                8   // δεξιά
                        )
                )
        );

        AbstractDocument document =
                (AbstractDocument) textArea.getDocument();

        UndoManager undoManager = new UndoManager();

        document.addUndoableEditListener(
                event -> undoManager.addEdit(event.getEdit())
        );

        textArea.putClientProperty(
                "undoManager",
                undoManager
        );

        document.setDocumentFilter(
                new TextAreaHeightLimitFilter(textArea)
        );

        if (documentChangeListener != null) {
            document.addDocumentListener(documentChangeListener);
        }

        textRelated.enableFreePositionClick(textArea);

        configureUndoRedoShortcuts(
                textArea,
                undoManager
        );

        return textArea;
    }

    public int getAreaCount() {
        return textAreas.size();
    }

    public List<JTextArea> getTextAreas() {
        return new ArrayList<>(textAreas);
    }

    public List<String> getTexts() {
        List<String> texts = new ArrayList<>();

        for (JTextArea textArea : textAreas) {
            texts.add(textArea.getText());
        }

        return texts;
    }

    public void setTexts(List<String> texts) {
        if (texts == null || texts.isEmpty()) {
            throw new IllegalArgumentException(
                    "Τα texts δεν μπορούν να είναι κενά."
            );
        }

        if (texts.size() < MIN_AREAS
                || texts.size() > MAX_AREAS) {
            throw new IllegalArgumentException(
                    "Το πλήθος των text areas πρέπει να είναι από 1 έως 5."
            );
        }

        removeAll();
        textAreas.clear();

        setLayout(
                new GridLayout(
                        texts.size(),
                        1,
                        0,
                        8
                )
        );

        for (String text : texts) {
            JTextArea textArea = createTextArea();
            textArea.setText(text);

            textAreas.add(textArea);
            add(textArea);
        }

        revalidate();
        repaint();
    }

    public void setDocumentChangeListener(
            DocumentListener documentChangeListener
    ) {
        this.documentChangeListener = documentChangeListener;

        for (JTextArea textArea : textAreas) {
            textArea.getDocument().addDocumentListener(
                    documentChangeListener
            );
        }
    }

    private void configureUndoRedoShortcuts(
            JTextArea textArea,
            UndoManager undoManager
    ) {
        InputMap inputMap =
                textArea.getInputMap(
                        JComponent.WHEN_FOCUSED
                );

        ActionMap actionMap =
                textArea.getActionMap();

        inputMap.put(
                KeyStroke.getKeyStroke("control Z"),
                "undo"
        );

        inputMap.put(
                KeyStroke.getKeyStroke("control Y"),
                "redo"
        );

        actionMap.put(
                "undo",
                new AbstractAction() {
                    @Override
                    public void actionPerformed(
                            java.awt.event.ActionEvent event
                    ) {
                        if (!undoManager.canUndo()) {
                            Toolkit.getDefaultToolkit().beep();
                            return;
                        }

                        try {
                            undoManager.undo();
                        } catch (CannotUndoException exception) {
                            Toolkit.getDefaultToolkit().beep();
                        }
                    }
                }
        );

        actionMap.put(
                "redo",
                new AbstractAction() {
                    @Override
                    public void actionPerformed(
                            java.awt.event.ActionEvent event
                    ) {
                        if (!undoManager.canRedo()) {
                            Toolkit.getDefaultToolkit().beep();
                            return;
                        }

                        try {
                            undoManager.redo();
                        } catch (CannotRedoException exception) {
                            Toolkit.getDefaultToolkit().beep();
                        }
                    }
                }
        );
    }
}