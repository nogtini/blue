package blue.soundObject.editor;

import blue.Arrangement;
import blue.BlueSystem;
import blue.CompileData;
import blue.Tables;
import blue.gui.ExceptionDialog;
import blue.gui.InfoDialog;
import blue.orchestra.blueSynthBuilder.BSBObjectRegistry;
import blue.orchestra.editor.blueSynthBuilder.BSBInterfaceEditor;
import blue.plugin.ScoreObjectEditorPlugin;
import blue.score.ScoreObject;
import blue.soundObject.NoteList;
import blue.soundObject.Sound;
import blue.soundObject.SoundObject;
import blue.ui.core.orchestra.editor.BlueSynthBuilderEditor;
import blue.ui.nbutilities.MimeTypeEditorComponent;
import blue.ui.utilities.SimpleDocumentListener;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.undo.UndoManager;
import org.openide.awt.UndoRedo;

/**
 * Title: blue Description: an object composition environment for csound
 * Copyright: Copyright (c) 2001 Company: steven yi music
 *
 * @author steven yi
 * @version 1.0
 */
@ScoreObjectEditorPlugin(scoreObjectType = Sound.class)
public class SoundEditor extends ScoreObjectEditor {

    Sound sObj;

    MimeTypeEditorComponent scoreEditPane = new MimeTypeEditorComponent("text/x-csound-orc");

    JLabel editorLabel = new JLabel();

    JPanel topPanel = new JPanel();

    JButton testButton = new JButton();

    UndoManager undo = new UndoRedo.Manager();

//    private final BSBInterfaceEditor interfaceEditor = new BSBInterfaceEditor(
//            BSBObjectRegistry.getBSBObjects(), false);
    
            BlueSynthBuilderEditor editor = new BlueSynthBuilderEditor();


    public SoundEditor() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        
        this.setLayout(new BorderLayout());
        this.add(editor);
        editor.setLabelText("[ Sound ]");
//        this.setLayout(new BorderLayout());
//        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
//        
//        JTabbedPane tabs = new JTabbedPane();
//        tabs.add(BlueSystem.getString("instrument.interface"), interfaceEditor);
//        tabs.add(BlueSystem.getString("instrument.code"), scoreEditPane);
//
//        scoreEditPane.getDocument().addDocumentListener(new SimpleDocumentListener() {
//
//            @Override
//            public void documentChanged(DocumentEvent e) {
//                if (sObj != null) {
//                    sObj.setText(scoreEditPane.getText());
//                }
//            }
//        });
//
//        initActions();
//
//        editorLabel.setText("[ Sound ]");
//
//        testButton.setText(BlueSystem.getString("common.test"));
//        testButton.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                testSoundObject();
//            }
//        });
//
//        topPanel.setLayout(new BorderLayout());
//        topPanel.add(editorLabel, BorderLayout.CENTER);
//        topPanel.add(testButton, BorderLayout.EAST);
//
//        this.add(tabs, BorderLayout.CENTER);
//        this.add(topPanel, BorderLayout.NORTH);
//
//        scoreEditPane.getDocument().addUndoableEditListener(undo);
//        scoreEditPane.setUndoManager(undo);
//
//        undo.setLimit(1000);
    }

//    private void initActions() {
//        InputMap inputMap = scoreEditPane.getInputMap();
//        ActionMap actions = scoreEditPane.getActionMap();
//
//        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_T, BlueSystem
//                .getMenuShortcutKey()), "testSoundObject");
//
//        actions.put("testSoundObject", new AbstractAction() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                testSoundObject();
//            }
//
//        });
//
//    }

    @Override
    public final void editScoreObject(ScoreObject sObj) {
        if (sObj == null) {
            this.sObj = null;
            editorLabel.setText("no editor available");
            scoreEditPane.setText("null soundObject");
            scoreEditPane.getJEditorPane().setEnabled(false);
            editor.editInstrument(null);
            return;
        }

        if (!(sObj instanceof Sound)) {
            this.sObj = null;
            editorLabel.setText("no editor available");
            scoreEditPane
                    .setText("[ERROR] GenericEditor::editSoundObject - not instance of GenericEditable");
            scoreEditPane.getJEditorPane().setEnabled(false);
            editor.editInstrument(null);

            return;
        }

        this.sObj = (Sound) sObj;
//        scoreEditPane.setText(this.sObj.getText());
        scoreEditPane.getJEditorPane().setEnabled(true);
        scoreEditPane.getJEditorPane().setCaretPosition(0);
        editor.editInstrument(this.sObj.getBlueSynthBuilder());


        undo.discardAllEdits();
    }

    public final void testSoundObject() {
        if (this.sObj == null) {
            return;
        }

        NoteList notes = null;

        try {
            notes = ((SoundObject) this.sObj).generateForCSD(new CompileData(new Arrangement(), new Tables()), 0.0f, -1.0f);
        } catch (Exception e) {
            ExceptionDialog.showExceptionDialog(SwingUtilities.getRoot(this), e);
        }

        if (notes != null) {
            InfoDialog.showInformationDialog(SwingUtilities.getRoot(this),
                    notes.toString(), BlueSystem
                    .getString("soundObject.generatedScore"));
        }
    }
}
