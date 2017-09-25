package KUU.FrameWorkComponent.OrderComponent.EditComponent.InputOrderPanel;

import KUU.BaseComponent.BaseFrame;
import KUU.GeneralComponent.GeneralBoolPanel;
import KUU.GeneralComponent.GeneralItemPanel;
import KUU.GeneralComponent.GeneralTextField;
import KUU.NewComponent.NewJDialog;
import KUU.NewComponent.NewJPanel;
import ProcessTerminal.VariableSettings.Variable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VariableSettingDialog extends NewJDialog {
    private GeneralItemPanel confirmLabel;

    private GeneralItemPanel[][] arrayIndicatePanel;
    private GeneralTextField[][] arrayTextFields;
    private GeneralBoolPanel[][] arrayBoolFields;
    private Variable.Type variableRapperType;
    private int arrayFirstSize;
    private int arraySecondSize;

    private JButton resetButton;

    private DialogBasePanel basePanel;
    private JPanel panel;
    private JScrollPane scrollPane;
    public VariableSettingDialog(BaseFrame frame, Variable.Type variableRapperType, String variableName, String variableArrayType, MouseEvent e) {
        super(frame);
        setLayout(new GridLayout(1,1));

        panel = new JPanel();
        panel.setLayout(null);

        this.variableRapperType = variableRapperType;

        /** 変数の型判定→配列の次元判定 */
        if (variableRapperType == Variable.Type.INT) {
            if (variableArrayType.equals("配列")) {
                arrayFirstSize = getFrame().getMasterTerminal().searchVariable(variableArrayType, variableName).getArraySize();
                arraySecondSize = 1;
                arrayTextFields = new GeneralTextField[arrayFirstSize][arraySecondSize];
                setTitle(variableArrayType + variableName + "[" + arrayFirstSize + "]の初期化");
            } else {
                arrayFirstSize = getFrame().getMasterTerminal().searchVariable(variableArrayType, variableName).getArraySize();
                arraySecondSize = getFrame().getMasterTerminal().searchVariable(variableArrayType, variableName).getArraySizeHorizontal();
                arrayTextFields = new GeneralTextField[arrayFirstSize][arraySecondSize];
                setTitle(variableArrayType + variableName + "[" + arrayFirstSize + "][" + arraySecondSize + "]の初期化");
            }
            for (int i = 0; i < arrayFirstSize; i++) {
                for (int j = 0; j < arraySecondSize; j++) {
                    panel.add(arrayTextFields[i][j] = new GeneralTextField());
                }
            }
        } else {
            if (variableArrayType.equals("配列")) {
                arrayFirstSize = getFrame().getMasterTerminal().searchVariable(variableArrayType, variableName).getArraySize();
                arraySecondSize = 1;
                arrayBoolFields = new GeneralBoolPanel[arrayFirstSize][arraySecondSize];
                setTitle(variableArrayType + variableName + "[" + arrayFirstSize + "]の初期化");
            } else {
                arrayFirstSize = getFrame().getMasterTerminal().searchVariable(variableArrayType, variableName).getArraySize();
                arraySecondSize = getFrame().getMasterTerminal().searchVariable(variableArrayType, variableName).getArraySizeHorizontal();
                arrayBoolFields = new GeneralBoolPanel[arrayFirstSize][arraySecondSize];
                setTitle(variableArrayType + variableName + "[" + arrayFirstSize + "][" + arraySecondSize + "]の初期化");
            }
            for (int i = 0; i < arrayFirstSize; i++) {
                for (int j = 0; j < arraySecondSize; j++) {
                    panel.add(arrayBoolFields[i][j] = new GeneralBoolPanel());
                    arrayBoolFields[i][j].setOpaque(true);
                }
            }
        }

        /** 枠の外側に[0]などの案内をつける */
        arrayIndicatePanel = new GeneralItemPanel[arrayFirstSize+1][arraySecondSize+1];
        for (int i = 0; i < arrayFirstSize+1; i++) {
            for (int j = 0; j < arraySecondSize+1; j++) {
                if (i==0 && j==0) {
                    arrayIndicatePanel[i][j] = new GeneralItemPanel(variableName);
                } else if (i==0){
                    arrayIndicatePanel[i][j] = new GeneralItemPanel("["+(j-1)+"]");
                } else if (j==0){
                    arrayIndicatePanel[i][j] = new GeneralItemPanel("["+(i-1)+"]");
                }
                if (arrayIndicatePanel[i][j] != null){
                    panel.add(arrayIndicatePanel[i][j]);
                    /** BOOLの場合、案内ラベルを押すとその列が反転するイベントを付ける */
                    if (variableRapperType==Variable.Type.BOOL){
                        int finalI = i;
                        int finalJ = j;
                        arrayIndicatePanel[finalI][finalJ].addMouseListener(new MouseAdapter() {
                            @Override
                            public void mousePressed(MouseEvent e) {
                                super.mousePressed(e);
                                if (finalI==0 && finalJ==0) {
                                    for (int k = 0; k < arrayFirstSize; k++) {
                                        for (int l = 0; l < arraySecondSize; l++) {
                                            arrayBoolFields[k][l].turn();
                                        }
                                    }
                                }
                                else if (finalI ==0){
                                    for (int k = 0; k < arrayFirstSize; k++) {
                                        arrayBoolFields[k][finalJ-1].turn();
                                    }
                                } else if (finalJ ==0){
                                    for (int k = 0; k < arraySecondSize; k++) {
                                        arrayBoolFields[finalI-1][k].turn();
                                    }
                                }
                            }
                        });
                    }
                }
            }
        }

        if (variableRapperType== Variable.Type.INT) {
            panel.add(resetButton = new JButton("全てに0を入れる"));
            resetButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    for (int i = 0; i < arrayFirstSize; i++) {
                        for (int j = 0; j < arraySecondSize; j++) {
                            arrayTextFields[i][j].setText("0");
                        }
                    }
                }
            });
        }

        scrollPane = new JScrollPane(panel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        basePanel = new DialogBasePanel(frame);
        basePanel.setLayout(null);
        basePanel.add(confirmLabel = new GeneralItemPanel(true,null,"確定"));
        basePanel.add(scrollPane,CENTER_ALIGNMENT);
        add(basePanel);

        confirmLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                /** execute処理 */

            }
        });

        int[] bounds = new int[4];
        bounds[0] = e.getXOnScreen() - ((arrayFirstSize<10)?arrayFirstSize*20:200);
        bounds[1] = e.getYOnScreen() - ((arraySecondSize<15)?arraySecondSize*30:450) - ((variableRapperType==Variable.Type.INT)?150:80);
        bounds[2] = ((arrayFirstSize<10)? arrayFirstSize*40+20:400+20) + ((variableRapperType==Variable.Type.INT)? 75:45);
        bounds[3] = ((arraySecondSize<15)? arraySecondSize*30+20:450+20) + ((variableRapperType==Variable.Type.INT)? 120:90);
        setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
    }

    private class DialogBasePanel extends NewJPanel implements ComponentListener {

        DialogBasePanel(BaseFrame frame) {
            super(frame);
            addComponentListener(this);
        }

        /** 生成時に呼ばれる */
        @Override
        public void handResize(int width, int height) {
            /** 下端のラベル */
            confirmLabel.setBounds(0, height - 30, width, 30);
            /** 内側のラベル */
            scrollPane.setBounds(0, 0, width, height - 30);

            /** Indicate */
            for (int i = 0; i < arrayFirstSize+1; i++) {
                for (int j = 0; j < arraySecondSize+1; j++) {
                    if (arrayIndicatePanel[i][j] != null){
                        arrayIndicatePanel[i][j].setBounds(i*40, j*30, 35, 25);
                    }
                }
            }
            /** Field */
            if (variableRapperType == Variable.Type.INT) {
                for (int i = 0; i < arrayFirstSize; i++) {
                    for (int j = 0; j < arraySecondSize; j++) {
                        arrayTextFields[i][j].setBounds(i*40 + 40, j*30 + 30, 35, 25);
                    }
                }
                resetButton.setBounds(width/2 - 40, arraySecondSize*30 + 30, 80, 25);
            } else {
                for (int i = 0; i < arrayFirstSize; i++) {
                    for (int j = 0; j < arraySecondSize; j++) {
                        arrayBoolFields[i][j].setBounds(i*40 + 40, j*30 + 30, 35, 25);
                    }
                }
            }
        }

        @Override
        public void componentResized(ComponentEvent e) {
            handResize(getWidth(),getHeight());
        }

        @Override
        public void componentMoved(ComponentEvent e) {

        }
        @Override
        public void componentShown(ComponentEvent e) {

        }
        @Override
        public void componentHidden(ComponentEvent e) {

        }
    }
}
