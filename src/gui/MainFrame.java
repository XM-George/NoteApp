package gui;

import javax.swing.*;
import java.awt.*;

import api.SaveService;
import api.LoadService;
import api.PrintService;
import model.NoteDocument;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class MainFrame extends JFrame {

    private A4PagePanel pagePanel;

    private final SaveService saveService = new SaveService();

    private Path currentFile;

    private final LoadService loadService = new LoadService();

    private final PrintService printService = new PrintService();

    private boolean documentModified = false;
    private boolean suppressModificationTracking = false;

    private int currentSearchAreaIndex = 0;
    private int currentSearchPosition = 0;
    private String lastSearchText = "";

    public MainFrame() {
        setTitle("Note App");

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                closeApplication();
            }
        });
        setSize(1100, 750);
        setMinimumSize(new Dimension(900, 650));
        setLocationRelativeTo(null);

        setImageIcon();
        initialiseUI();
        initialiseMenuBar();
        initialiseFindShortcut();
        initialiseDocumentChangeTracking();
        updateWindowTitle();
    }

    private void setImageIcon() {
        java.net.URL iconUrl =
                getClass().getResource("/icons/appIcon.png");

        if (iconUrl == null) {
            System.err.println(
                    "Application icon not found: /icons/appIcon.png"
            );
            return;
        }

        Image iconImage =
                Toolkit.getDefaultToolkit().getImage(iconUrl);

        setIconImage(iconImage);
    }

    private void initialiseUI() {
        setLayout(new BorderLayout());

        JPanel backgroundPanel =
                new JPanel(new GridBagLayout());

        backgroundPanel.setBackground(
                new Color(70, 70, 70)
        );

        pagePanel = new A4PagePanel();

        backgroundPanel.add(pagePanel);

        JScrollPane scrollPane =
                new JScrollPane(backgroundPanel);

        scrollPane.setBorder(null);

        scrollPane.getViewport().setBackground(
                new Color(70, 70, 70)
        );

        add(scrollPane, BorderLayout.CENTER);
    }

    private void initialiseMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        JMenuItem newMenuItem =
                new JMenuItem("New");

        JMenuItem openMenuItem =
                new JMenuItem("Open...");

        JMenuItem saveMenuItem =
                new JMenuItem("Save");

        JMenuItem saveAsMenuItem =
                new JMenuItem("Save As...");

        newMenuItem.setAccelerator(
                KeyStroke.getKeyStroke("control N")
        );

        openMenuItem.setAccelerator(
                KeyStroke.getKeyStroke("control O")
        );

        saveMenuItem.setAccelerator(
                KeyStroke.getKeyStroke("control S")
        );

        saveAsMenuItem.setAccelerator(
                KeyStroke.getKeyStroke("control shift S")
        );

        newMenuItem.addActionListener(
                event -> newDocument()
        );

        openMenuItem.addActionListener(
                event -> openDocument()
        );

        saveMenuItem.addActionListener(
                event -> saveDocument()
        );

        saveAsMenuItem.addActionListener(
                event -> saveDocumentAs()
        );

        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);

        JButton undoButton = new JButton("Undo");
        JButton redoButton = new JButton("Redo");

        undoButton.setFocusable(false);
        redoButton.setFocusable(false);

        undoButton.setBorderPainted(false);
        undoButton.setContentAreaFilled(false);

        redoButton.setBorderPainted(false);
        redoButton.setContentAreaFilled(false);

        undoButton.setMargin(
                new Insets(2, 10, 2, 10)
        );

        redoButton.setMargin(
                new Insets(2, 10, 2, 10)
        );

        undoButton.addActionListener(
                event -> undoFocusedTextArea()
        );

        redoButton.addActionListener(
                event -> redoFocusedTextArea()
        );

        JMenu printMenu = new JMenu("Print");

        JMenuItem previewMenuItem =
                new JMenuItem("Print Preview...");

        JMenuItem printMenuItem =
                new JMenuItem("Print Document...");

        previewMenuItem.setAccelerator(
                KeyStroke.getKeyStroke("control shift P")
        );

        printMenuItem.setAccelerator(
                KeyStroke.getKeyStroke("control P")
        );

        previewMenuItem.addActionListener(
                event -> showPrintPreview()
        );

        printMenuItem.addActionListener(
                event -> printDocument()
        );

        printMenu.add(printMenuItem);
        printMenu.addSeparator();
        printMenu.add(previewMenuItem);

        JMenu layoutMenu = new JMenu("Layout");

        JMenu leftMenu = createAreaCountMenu(
                "Left A5",
                pagePanel.getLeftSection()
        );

        JMenu rightMenu = createAreaCountMenu(
                "Right A5",
                pagePanel.getRightSection()
        );

        JButton findButton = new JButton("Find");

        findButton.setFocusable(false);
        findButton.setBorderPainted(false);
        findButton.setContentAreaFilled(false);
        findButton.setMargin(
                new Insets(2, 10, 2, 10)
        );

        findButton.addActionListener(
                event -> showFindDialog()
        );

        layoutMenu.add(leftMenu);
        layoutMenu.add(rightMenu);

        menuBar.add(fileMenu);
        menuBar.add(undoButton);
        menuBar.add(redoButton);
        menuBar.add(printMenu);
        menuBar.add(layoutMenu);
        menuBar.add(findButton);

        setJMenuBar(menuBar);
    }

    private JMenu createAreaCountMenu(
            String title,
            A5SectionPanel sectionPanel
    ) {
        JMenu menu = new JMenu(title);

        ButtonGroup buttonGroup = new ButtonGroup();

        for (int count = 1; count <= 5; count++) {
            int selectedCount = count;

            JRadioButtonMenuItem menuItem =
                    new JRadioButtonMenuItem(
                            count == 1
                                    ? "1 Text Area"
                                    : count + " Text Areas"
                    );

            if (count == sectionPanel.getAreaCount()) {
                menuItem.setSelected(true);
            }

            menuItem.addActionListener(event -> {
                if (sectionPanel.getAreaCount()
                        == selectedCount) {
                    return;
                }

                sectionPanel.setAreaCount(selectedCount);
                markDocumentModified();
            });

            buttonGroup.add(menuItem);
            menu.add(menuItem);
        }

        return menu;
    }

    private NoteDocument createDocumentFromUI() {
        List<String> leftTexts =
                pagePanel.getLeftSection().getTexts();

        List<String> rightTexts =
                pagePanel.getRightSection().getTexts();

        return new NoteDocument(
                leftTexts,
                rightTexts
        );
    }

    private void saveDocument() {
        saveDocumentAndReturnResult();
    }

    private void saveDocumentAs() {
        saveDocumentAsAndReturnResult();
    }

    private boolean saveToFile(Path filePath) {
        NoteDocument document = createDocumentFromUI();

        try {
            saveService.save(document, filePath);

            currentFile = filePath;
            documentModified = false;

            updateWindowTitle();

            return true;

        } catch (IOException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    "The note could not be saved.\n"
                            + exception.getMessage(),
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return false;
        }
    }

    private void updateWindowTitle() {
        String documentName;

        if (currentFile == null) {
            documentName = "Untitled";
        } else {
            documentName =
                    currentFile.getFileName().toString();
        }

        String modifiedMarker =
                documentModified ? "*" : "";

        setTitle(
                modifiedMarker
                        + documentName
                        + " - Note App"
        );
    }

    private void openDocument() {
        if (!confirmDiscardUnsavedChanges()) {
            return;
        }

        FileDialog fileDialog = new FileDialog(
                this,
                "Open Note",
                FileDialog.LOAD
        );

        fileDialog.setFile("*.noteapp");
        fileDialog.setVisible(true);

        String selectedFile =
                fileDialog.getFile();

        String selectedDirectory =
                fileDialog.getDirectory();

        if (selectedFile == null
                || selectedDirectory == null) {
            return;
        }

        File file = new File(
                selectedDirectory,
                selectedFile
        );

        loadFromFile(file.toPath());
    }

    private void loadFromFile(Path filePath) {
        try {
            NoteDocument document =
                    loadService.load(filePath);

            suppressModificationTracking = true;

            try {
                pagePanel.getLeftSection().setTexts(
                        document.getLeftTexts()
                );

                pagePanel.getRightSection().setTexts(
                        document.getRightTexts()
                );

                currentFile = filePath;
                documentModified = false;

                refreshLayoutMenu();
                updateWindowTitle();

            } finally {
                suppressModificationTracking = false;
            }

        } catch (IOException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    "The file could not be opened.\n"
                            + exception.getMessage(),
                    "Open Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void refreshLayoutMenu() {
        initialiseMenuBar();

        revalidate();
        repaint();
    }

    private void newDocument() {
        if (!confirmDiscardUnsavedChanges()) {
            return;
        }

        suppressModificationTracking = true;

        try {
            pagePanel.getLeftSection().setTexts(
                    List.of("")
            );

            pagePanel.getRightSection().setTexts(
                    List.of("")
            );

            currentFile = null;
            documentModified = false;

            refreshLayoutMenu();
            updateWindowTitle();

        } finally {
            suppressModificationTracking = false;
        }
    }

    private void printDocument() {
        preparePageForRendering();

        printService.printComponent(
                pagePanel,
                this
        );
    }

    private void layoutChildrenRecursively(
            Container container
    ) {
        container.doLayout();

        for (Component component
                : container.getComponents()) {

            if (component instanceof Container child) {
                layoutChildrenRecursively(child);
            }
        }
    }

    private void initialiseDocumentChangeTracking() {
        DocumentListener listener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                markDocumentModified();
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                markDocumentModified();
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                markDocumentModified();
            }
        };

        pagePanel.getLeftSection()
                .setDocumentChangeListener(listener);

        pagePanel.getRightSection()
                .setDocumentChangeListener(listener);
    }

    private void markDocumentModified() {
        if (suppressModificationTracking) {
            return;
        }

        if (!documentModified) {
            documentModified = true;
            updateWindowTitle();
        }
    }

    private boolean confirmDiscardUnsavedChanges() {
        if (!documentModified) {
            return true;
        }

        int result = JOptionPane.showConfirmDialog(
                this,
                "The document has unsaved changes.\n"
                        + "Do you want to save them?",
                "Unsaved Changes",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (result == JOptionPane.CANCEL_OPTION
                || result == JOptionPane.CLOSED_OPTION) {
            return false;
        }

        if (result == JOptionPane.NO_OPTION) {
            return true;
        }

        return saveDocumentAndReturnResult();
    }

    private boolean saveDocumentAndReturnResult() {
        if (currentFile == null) {
            return saveDocumentAsAndReturnResult();
        }

        return saveToFile(currentFile);
    }

    private boolean saveDocumentAsAndReturnResult() {
        FileDialog fileDialog = new FileDialog(
                this,
                "Save Note",
                FileDialog.SAVE
        );

        fileDialog.setFile("untitled.noteapp");
        fileDialog.setVisible(true);

        String selectedFile = fileDialog.getFile();
        String selectedDirectory = fileDialog.getDirectory();

        if (selectedFile == null
                || selectedDirectory == null) {
            return false;
        }

        if (!selectedFile.toLowerCase()
                .endsWith(".noteapp")) {
            selectedFile += ".noteapp";
        }

        File file = new File(
                selectedDirectory,
                selectedFile
        );

        Path selectedPath = file.toPath();

        if (Files.exists(selectedPath)) {
            int overwriteResult =
                    JOptionPane.showConfirmDialog(
                            this,
                            "The file already exists. Replace it?",
                            "Confirm Overwrite",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

            if (overwriteResult
                    != JOptionPane.YES_OPTION) {
                return false;
            }
        }

        return saveToFile(selectedPath);
    }

    private void closeApplication() {
        if (!confirmDiscardUnsavedChanges()) {
            return;
        }

        dispose();
        System.exit(0);
    }

    private void showPrintPreview() {
        preparePageForRendering();

        List<JTextArea> textAreas = getAllTextAreas();
        List<Boolean> previousCaretStates = new java.util.ArrayList<>();

        for (JTextArea textArea : textAreas) {
            previousCaretStates.add(
                    textArea.getCaret().isVisible()
            );

            textArea.getCaret().setVisible(false);
        }

        try {
            BufferedImage previewImage =
                    createPagePreviewImage();

            PrintPreviewDialog previewDialog =
                    new PrintPreviewDialog(
                            this,
                            previewImage,
                            this::printDocument
                    );

            previewDialog.setVisible(true);

        } finally {
            for (int i = 0; i < textAreas.size(); i++) {
                textAreas.get(i)
                        .getCaret()
                        .setVisible(previousCaretStates.get(i));
            }
        }
    }

    private BufferedImage createPagePreviewImage() {
        int width = pagePanel.getWidth();
        int height = pagePanel.getHeight();

        BufferedImage image =
                new BufferedImage(
                        width,
                        height,
                        BufferedImage.TYPE_INT_RGB
                );

        Graphics2D graphics2D =
                image.createGraphics();

        try {
            graphics2D.setColor(Color.WHITE);
            graphics2D.fillRect(0, 0, width, height);

            graphics2D.setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON
            );

            graphics2D.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            pagePanel.printAll(graphics2D);

        } finally {
            graphics2D.dispose();
        }

        return image;
    }

    private void preparePageForRendering() {
        pagePanel.setSize(
                pagePanel.getPreferredSize()
        );

        pagePanel.doLayout();

        layoutChildrenRecursively(pagePanel);
    }

    private JTextArea getFocusedTextArea() {
        Component focusedComponent =
                KeyboardFocusManager
                        .getCurrentKeyboardFocusManager()
                        .getFocusOwner();

        if (focusedComponent instanceof JTextArea textArea) {
            return textArea;
        }

        return null;
    }

    private void undoFocusedTextArea() {
        JTextArea textArea = getFocusedTextArea();

        if (textArea == null) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }

        Object value =
                textArea.getClientProperty("undoManager");

        if (!(value instanceof UndoManager undoManager)
                || !undoManager.canUndo()) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }

        try {
            undoManager.undo();
        } catch (CannotUndoException exception) {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private void redoFocusedTextArea() {
        JTextArea textArea = getFocusedTextArea();

        if (textArea == null) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }

        Object value =
                textArea.getClientProperty("undoManager");

        if (!(value instanceof UndoManager undoManager)
                || !undoManager.canRedo()) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }

        try {
            undoManager.redo();
        } catch (CannotRedoException exception) {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private void showFindDialog() {
        JDialog dialog = new JDialog(
                this,
                "Find Text",
                false
        );

        JTextField searchField =
                new JTextField(20);

        JButton findNextButton =
                new JButton("Find Next");

        JPanel panel =
                new JPanel(new FlowLayout());

        panel.add(new JLabel("Find:"));
        panel.add(searchField);
        panel.add(findNextButton);

        dialog.add(panel);

        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);

        findNextButton.addActionListener(
                event -> findNextText(
                        searchField.getText()
                )
        );

        searchField.addActionListener(
                event -> findNextText(
                        searchField.getText()
                )
        );

        dialog.setVisible(true);
        searchField.requestFocusInWindow();
    }

    private void findNextText(String searchText) {
        if (searchText == null || searchText.isBlank()) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }

        List<JTextArea> allTextAreas =
                getAllTextAreas();

        if (allTextAreas.isEmpty()) {
            return;
        }

        if (!searchText.equalsIgnoreCase(lastSearchText)) {
            currentSearchAreaIndex = 0;
            currentSearchPosition = 0;
            lastSearchText = searchText;
        }

        String searchLower =
                searchText.toLowerCase();

        int checkedAreas = 0;

        while (checkedAreas < allTextAreas.size()) {
            JTextArea textArea =
                    allTextAreas.get(
                            currentSearchAreaIndex
                    );

            String content =
                    textArea.getText().toLowerCase();

            int foundIndex =
                    content.indexOf(
                            searchLower,
                            currentSearchPosition
                    );

            if (foundIndex >= 0) {
                textArea.requestFocusInWindow();

                textArea.select(
                        foundIndex,
                        foundIndex + searchText.length()
                );

                currentSearchPosition =
                        foundIndex + searchText.length();

                return;
            }

            currentSearchAreaIndex =
                    (currentSearchAreaIndex + 1)
                            % allTextAreas.size();

            currentSearchPosition = 0;
            checkedAreas++;
        }

        Toolkit.getDefaultToolkit().beep();

        JOptionPane.showMessageDialog(
                this,
                "No more matches found.",
                "Find",
                JOptionPane.INFORMATION_MESSAGE
        );

        currentSearchAreaIndex = 0;
        currentSearchPosition = 0;
    }

    private List<JTextArea> getAllTextAreas() {
        List<JTextArea> allTextAreas =
                new java.util.ArrayList<>();

        allTextAreas.addAll(
                pagePanel.getLeftSection()
                        .getTextAreas()
        );

        allTextAreas.addAll(
                pagePanel.getRightSection()
                        .getTextAreas()
        );

        return allTextAreas;
    }

    private void initialiseFindShortcut() {
        InputMap inputMap =
                getRootPane().getInputMap(
                        JComponent.WHEN_IN_FOCUSED_WINDOW
                );

        ActionMap actionMap =
                getRootPane().getActionMap();

        inputMap.put(
                KeyStroke.getKeyStroke("control F"),
                "showFindDialog"
        );

        actionMap.put(
                "showFindDialog",
                new AbstractAction() {
                    @Override
                    public void actionPerformed(
                            java.awt.event.ActionEvent event
                    ) {
                        showFindDialog();
                    }
                }
        );
    }
}