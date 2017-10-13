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

    private GeneralItemPanel[][][][] arrayIndicatePanel;
    private GeneralTextField[][] arrayTextFields;
    private GeneralBoolPanel[][] arrayBoolFields;
    private Variable.Type variableRapperType;
    private int arrayFirstSize;
    private int arraySecondSize;

    private GeneralItemPanel upButton;
    private GeneralItemPanel downButton;
    private GeneralItemPanel leftButton;
    private GeneralItemPanel rightButton;
    private int panelRow;
    private int panelCol;

    private GeneralItemPanel resetButton;
    private GeneralItemPanel setEmptyButton;

    private DialogBasePanel basePanel;
    private JPanel[][] panel;
    private int panelI;
    private int panelJ;

    public VariableSettingDialog(BaseFrame frame, Variable.Type variableRapperType, String variableName, String variableArrayType, MouseEvent e) {
        super(frame);
        setLayout(new GridLayout(1,1));

        arrayFirstSize = getFrame().getMasterTerminal().searchVariable(variableArrayType, variableName).getArraySize();
        arraySecondSize = (variableArrayType.equals("配列"))?1:getFrame().getMasterTerminal().searchVariable(variableArrayType, variableName).getArraySizeHorizontal();
        /** 1ページに縦の要素数は10、横の要素数は15 */
        panelI = (arraySecondSize-1)/10+1;
        panelJ = (arrayFirstSize-1)/15+1;
        panel = new JPanel[panelI][panelJ];
        for (int i = 0; i < panelI; i++) {
            for (int j = 0; j < panelJ; j++) {
                panel[i][j] = new JPanel();
                panel[i][j].setLayout(null);
            }
        }
        this.variableRapperType = variableRapperType;

        /** 型に合わせたFieldの配置 */
        if (variableRapperType == Variable.Type.INT) {
            setTitle(variableArrayType + variableName + "[" + arrayFirstSize + "]" + (variableArrayType.equals("配列")?"":"[" + arraySecondSize + "]") + "の初期化");
            arrayTextFields = new GeneralTextField[arraySecondSize][arrayFirstSize];
            for (int i = 0; i < arraySecondSize; i++) {
                for (int j = 0; j < arrayFirstSize; j++) {
                    arrayTextFields[i][j] = new GeneralTextField();
                    panel[i/10][j/15].add(arrayTextFields[i][j]);
                }
            }
        } else {
            setTitle(variableArrayType + variableName + "[" + arrayFirstSize + "]" + (variableArrayType.equals("配列")?"":"[" + arraySecondSize + "]") + "の初期化");
            arrayBoolFields = new GeneralBoolPanel[arraySecondSize][arrayFirstSize];
            for (int i = 0; i < arraySecondSize; i++) {
                for (int j = 0; j < arrayFirstSize; j++) {
                    panel[i/10][j/15].add(arrayBoolFields[i][j] = new GeneralBoolPanel());
                    arrayBoolFields[i][j].setOpaque(true);
                }
            }
        }

        /** 枠の外側にある[0]など案内の生成 */
        arrayIndicatePanel = new GeneralItemPanel[panelI][panelJ][11][16];
        for (int k = 0; k < panelI; k++) {
            for (int l = 0; l < panelJ; l++) {
                /** 変数名 */
                panel[k][l].add(arrayIndicatePanel[k][l][0][0] = new GeneralItemPanel(variableName));
                for (int i = 1; i < 11; i++) {
                    for (int j = 1; j < 16; j++) {
                        /** 上側 */
                        if(j+15*l<=arrayFirstSize)panel[k][l].add(arrayIndicatePanel[k][l][0][j] = new GeneralItemPanel("["+(j-1+15*l)+"]"));
                        /** 左側 */
                        if(i+10*k<=arraySecondSize)panel[k][l].add(arrayIndicatePanel[k][l][i][0] = new GeneralItemPanel("["+(i-1+10*k)+"]"));
                        if (arrayIndicatePanel[k][l][i][j] != null){
                            /** BOOLの場合、案内ラベルを押すとその列が反転するイベントを付ける */
                            if (variableRapperType==Variable.Type.BOOL) {
                                int finalI = i;
                                int finalJ = j;
                                arrayIndicatePanel[k][l][finalI][finalJ].addMouseListener(new MouseAdapter() {
                                    @Override
                                    public void mousePressed(MouseEvent e) {
                                        super.mousePressed(e);
                                        if (finalI == 0 && finalJ == 0) {
                                            for (int i = 0; i < arraySecondSize; i++) {
                                                for (int j = 0; j < arrayFirstSize; j++) {
                                                    arrayBoolFields[i][j].turn();
                                                }
                                            }
                                        } else if (finalI == 0) {
                                            for (int j = 0; j < arraySecondSize; j++) {
                                                arrayBoolFields[j][finalJ - 1].turn();
                                            }
                                        } else if (finalJ == 0) {
                                            for (int i = 0; i < arrayFirstSize; i++) {
                                                arrayBoolFields[finalI - 1][i].turn();
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            }
        }

        /** 初期値を格納 */
//        if (variableRapperType == Variable.Type.INT){
//            /** INT 値を格納 */
//            if (variableArrayType.equals("配列")){
//                for (int j = 0; j < arrayFirstSize; j++) {
//                    arrayTextFields[0][j].setText(String.valueOf(getFrame().getMasterTerminal().searchVariable("配列",variableName).getStartingValue(j)));
//                }
//            } else {
//                for (int i = 0; i < arraySecondSize; i++) {
//                    for (int j = 0; j < arrayFirstSize; j++) {
//                        arrayTextFields[i][j].setText(String.valueOf(getFrame().getMasterTerminal().searchVariable("2次元配列",variableName).getStartingValue(i,j)));
//                    }
//                }
//            }
//        } else {
//            /** BOOL 値を参照し反転 */
//            if (variableArrayType.equals("配列")){
//                for (int j = 0; j < arrayFirstSize; j++) {
//                    if (getFrame().getMasterTerminal().searchVariable("配列",variableName).getStartingValue(j)==1)arrayBoolFields[0][j].turn();
//                }
//            } else {
//                for (int i = 0; i < arraySecondSize; i++) {
//                    for (int j = 0; j < arrayFirstSize; j++) {
//                        if (getFrame().getMasterTerminal().searchVariable("2次元配列",variableName).getStartingValue(i,j)==1)arrayBoolFields[i][j].turn();
//                    }
//                }
//            }
//        }

        /** ここからbasePanel */
        basePanel = new DialogBasePanel(frame);
        basePanel.setLayout(null);

        /** INTの場合数値セットボタンを配置 */
        if (variableRapperType== Variable.Type.INT) {
            basePanel.add(resetButton = new GeneralItemPanel(true,null,"全て0にする"));
            resetButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    for (int i = 0; i < arraySecondSize; i++) {
                        for (int j = 0; j < arrayFirstSize; j++) {
                            arrayTextFields[i][j].setText("0");
                        }
                    }
                }
            });
            basePanel.add(setEmptyButton = new GeneralItemPanel(true,null,"全て空にする"));
            setEmptyButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    for (int i = 0; i < arraySecondSize; i++) {
                        for (int j = 0; j < arrayFirstSize; j++) {
                            arrayTextFields[i][j].setText("");
                        }
                    }
                }
            });
        }

        /** ページ送りボタンの配置 */
        if (arrayFirstSize>=16){
            basePanel.add(leftButton  = new GeneralItemPanel("←"));
            basePanel.add(rightButton = new GeneralItemPanel("→"));
            leftButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    changePanel("left");
                }
            });
            rightButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    changePanel("right");
                }
            });
        }
        if (arraySecondSize>=11){
            basePanel.add(upButton   = new GeneralItemPanel("↑"));
            basePanel.add(downButton = new GeneralItemPanel("↓"));
            upButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    changePanel("up");
                }
            });
            downButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    changePanel("down");
                }
            });
        }

        basePanel.add(confirmLabel = new GeneralItemPanel(true,null,"確定"));
        confirmLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    if (variableArrayType.equals("配列")) {
                        int[] tmp = new int[arrayFirstSize];
                        if (variableRapperType == Variable.Type.BOOL) {
                            for (int j = 0; j < arrayFirstSize; j++) {
                                tmp[j] = arrayBoolFields[0][j].getNum();
                            }
                        } else {
                            for (int j = 0; j < arrayFirstSize; j++) {
                                try {
                                    tmp[j] = Integer.parseInt(arrayTextFields[0][j].getText());
                                } catch (Exception e1){
                                    if (("").equals(arrayTextFields[0][j].getText()))throw new Exception();
                                    tmp[j] = 0;
                                }
                            }
                        }
                        getFrame().getMasterTerminal().searchVariable(variableArrayType, variableName).setAllStartingArrays(tmp);
                    } else {
                        int[][] tmp = new int[arraySecondSize][arrayFirstSize];
                        if (variableRapperType == Variable.Type.BOOL) {
                            for (int i = 0; i < arraySecondSize; i++) {
                                for (int j = 0; j < arrayFirstSize; j++) {
                                    tmp[i][j] = arrayBoolFields[i][j].getNum();
                                }
                            }
                        } else {
                            for (int i = 0; i < arraySecondSize; i++) {
                                for (int j = 0; j < arrayFirstSize; j++) {
                                    try {
                                        tmp[i][j] = Integer.parseInt(arrayTextFields[i][j].getText());
                                    } catch (Exception e1){
                                        if (("").equals(arrayTextFields[i][j].getText()))throw new Exception();
                                        tmp[i][j] = 0;
                                    }
                                }
                            }
                        }
                        getFrame().getMasterTerminal().searchVariable(variableArrayType, variableName).setAllStartingArrays(tmp);
                    }
                    JOptionPane.showMessageDialog(confirmLabel, "変数"+variableName+"の初期化が完了しました。");
                    getFrame().updateOrderPanel(false);
                    dispose();
                }catch (Exception e1){
                    JOptionPane.showMessageDialog(confirmLabel, "未入力のマスがあります！");
                }
            }
        });

        for (int i = 0; i < panelI; i++) {
            for (int j = 0; j < panelJ; j++) {
                basePanel.add(panel[i][j]);
            }
        }
        panelRow=0;
        panelCol=0;
        changePanel("");
        add(basePanel);


        int[] bounds = new int[4];
        bounds[0] = e.getXOnScreen() - (arrayFirstSize<=15? arrayFirstSize*20:315);
        bounds[1] = e.getYOnScreen() - (arraySecondSize<=10? arraySecondSize*30:315) - (variableRapperType==Variable.Type.INT? 150:80);
        bounds[2] = (arrayFirstSize<=15? arrayFirstSize*40:595)+45 + (arraySecondSize<=10? 0:35);
        bounds[3] = (arraySecondSize<=10? arraySecondSize*30:300) + ((variableRapperType==Variable.Type.INT||arrayFirstSize>15)? 120:90);
        setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
    }

    /** ページ送り時に呼び出されるメソッド
     *  向きを与え変更可能ならば変更する */
    private void changePanel(String direction){
        panel[panelCol][panelRow].setVisible(false);
        switch (direction){
            case "up":
                if (panelCol!=0)panelCol--;
                break;
            case "down":
                if (panelCol<panelI-1)panelCol++;
                break;
            case "left":
                if (panelRow!=0)panelRow--;
                break;
            case "right":
                if (panelRow<panelJ-1)panelRow++;
                break;
            default:
                for (int i = 0; i < panelI; i++) {
                    for (int j = 0; j < panelJ; j++) {
                        panel[i][j].setVisible(false);
                    }
                }
                panel[0][0].setVisible(true);
        }
        panel[panelCol][panelRow].setVisible(true);
    }

    private class DialogBasePanel extends NewJPanel implements ComponentListener {
        DialogBasePanel(BaseFrame frame) {
            super(frame);
            addComponentListener(this);
        }

        /** 生成時に呼ばれる */
        @Override
        public void handResize(int width, int height) {
            /** 外側のラベル */
            confirmLabel.setBounds(0, height - 30, width, 30);
            /** 数値セットボタン */
            if (variableRapperType == Variable.Type.INT){
                setEmptyButton.setBounds(width/2 + 5, height - 60, 100, 25);
                resetButton.setBounds(width/2 - 105, height - 60, 100, 25);
            }
            /** 左右ページ切り替えボタン */
            if (arrayFirstSize>=16){
                leftButton.setBounds(10, height - 60, 25, 25);
                rightButton.setBounds(width - 60, height - 60, 25, 25);
            }
            /** 上下ページ切り替えボタン */
            if (arraySecondSize>=11){
                upButton.setBounds(width - 30, 10, 25, 25);
                downButton.setBounds(width - 30, height - 90, 25, 25);
            }

            /** 内側のパネル Visibleで切り替えるため場所は同じ */
            for (int i = 0; i < panelI; i++) {
                for (int j = 0; j < panelJ; j++) {
                    panel[i][j].setBounds(0, 0, width - (arraySecondSize>15?30:0), height - 60);
                }
            }

            /** Indicate */
            for (int k = 0; k < panelI; k++) {
                for (int l = 0; l < panelJ; l++) {
                    /** 変数名 */
                    arrayIndicatePanel[k][l][0][0].setBounds(0, 0, 35, 25);
                    for (int i = 1; i < 11; i++) {
                        for (int j = 1; j < 16; j++) {
                            /** 上側 */
                            if(j+15*l<=arrayFirstSize)arrayIndicatePanel[k][l][0][j].setBounds(((j-1)%15+1)*40, 0, 35, 25);
                            /** 左側 */
                            if(i+10*k<=arraySecondSize)arrayIndicatePanel[k][l][i][0].setBounds(0, ((i-1)%10+1)*30, 35, 25);
                        }
                    }
                }
            }
            /** Field */
            if (variableRapperType == Variable.Type.INT) {
                for (int i = 0; i < arraySecondSize; i++) {
                    for (int j = 0; j < arrayFirstSize; j++) {
                        arrayTextFields[i][j].setBounds(j%15*40 + 40, i%10*30 + 30, 35, 25);
                    }
                }
            } else {
                for (int i = 0; i < arraySecondSize; i++) {
                    for (int j = 0; j < arrayFirstSize; j++) {
                        arrayBoolFields[i][j].setBounds(j%15*40 + 40, i%10*30 + 30, 35, 25);
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