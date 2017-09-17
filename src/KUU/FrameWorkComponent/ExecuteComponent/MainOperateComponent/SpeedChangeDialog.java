package KUU.FrameWorkComponent.ExecuteComponent.MainOperateComponent;

import KUU.BaseComponent.BaseFrame;
import KUU.GeneralComponent.GeneralItemPanel;
import KUU.GeneralComponent.GeneralTextField;
import Master.ColorMaster.*;
import KUU.NewComponent.NewJDialog;
import KUU.NewComponent.NewJPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 実行速度を変更するためのダイアログ。
 */
public class SpeedChangeDialog extends NewJDialog implements MouseListener{
    private JRadioButton            speedRadioButton1;
    private JRadioButton            speedRadioButton2;
    private ButtonGroup             speedButtonGroup;
    private GeneralItemPanel        speedCustomIndicateLabel;
    private GeneralItemPanel speedCustomLabel;
    private GeneralTextField speedCustomText;
    private GeneralItemPanel speedConfirmLabel;
    private DialogBasePanel         panel;

    private GeneralItemPanel verySlowLabel;
    private GeneralItemPanel slowLabel;
    private GeneralItemPanel normalLabel;
    private GeneralItemPanel fastLabel;
    private GeneralItemPanel veryFastLabel;
    private String speedMode;


    public SpeedChangeDialog(BaseFrame frame, MouseEvent e){
        super(frame);
        setLayout(new GridLayout(1,1));

        panel = new DialogBasePanel(frame);
        panel.setLayout(null);

        /** カスタム設定 */
        panel.add(speedRadioButton1 = new JRadioButton("リストから選択"));
        panel.add(speedRadioButton2 = new JRadioButton("カスタム設定"));
        speedButtonGroup = new ButtonGroup();
        speedButtonGroup.add(speedRadioButton1);
        speedButtonGroup.add(speedRadioButton2);
        panel.add(speedCustomLabel  = new GeneralItemPanel("カスタム"));
        panel.add(speedCustomIndicateLabel = new GeneralItemPanel("1～100(1が最速)で入力"));
        panel.add(speedCustomText = new GeneralTextField());
        panel.add(speedConfirmLabel = new GeneralItemPanel(true,null,"確定"));


        /** リスト設定 */
        panel.add(verySlowLabel = new GeneralItemPanel(null,null,"とても遅い"));
        panel.add(slowLabel = new GeneralItemPanel(null,null,"遅い"));
        panel.add(normalLabel = new GeneralItemPanel(null,null,"普通"));
        panel.add(fastLabel = new GeneralItemPanel(null,null,"速い"));
        panel.add(veryFastLabel = new GeneralItemPanel(null,null,"とても速い"));

        verySlowLabel.addMouseListener(this);
        slowLabel.addMouseListener(this);
        normalLabel.addMouseListener(this);
        fastLabel.addMouseListener(this);
        veryFastLabel.addMouseListener(this);

        /** ラジオボタン */
        /** リスト設定が有効 */
        speedRadioButton1.setSelected(true);
        speedRadioButton1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                /** ラジオボタンの背景 */
                speedRadioButton1.setBackground(ColorMaster.getSelectedColor());
                speedRadioButton2.setBackground(ColorMaster.getNotSelectedColor());

                /** リスト設定オン */
                verySlowLabel.setEnabled(true);
                slowLabel.setEnabled(true);
                normalLabel.setEnabled(true);
                fastLabel.setEnabled(true);
                veryFastLabel.setEnabled(true);
                verySlowLabel.setBackground(ColorMaster.getNotSelectedColor());
                slowLabel.setBackground(ColorMaster.getNotSelectedColor());
                normalLabel.setBackground(ColorMaster.getNotSelectedColor());
                fastLabel.setBackground(ColorMaster.getNotSelectedColor());
                veryFastLabel.setBackground(ColorMaster.getNotSelectedColor());

                /** 選ばれているモードに色を付ける */
                switch (speedMode) {
                    case "とても遅い":
                        verySlowLabel.setBackground(ColorMaster.getSelectedColor());
                        break;
                    case "遅い":
                        slowLabel.setBackground(ColorMaster.getSelectedColor());
                        break;
                    case "普通":
                        normalLabel.setBackground(ColorMaster.getSelectedColor());
                        break;
                    case "速い":
                        fastLabel.setBackground(ColorMaster.getSelectedColor());
                        break;
                    case "とても速い":
                        veryFastLabel.setBackground(ColorMaster.getSelectedColor());
                        break;
                }

                /** カスタム設定オフ */
                speedCustomLabel.setEnabled(false);
                speedCustomText.setEnabled(false);
                speedCustomLabel.setBackground(ColorMaster.getNotSelectedColor());
                speedCustomText.setBackground(ColorMaster.getNotSelectedColor());
            }
        });
        /** カスタム設定が有効 */
        speedRadioButton2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                /** ラジオボタンの背景 */
                speedRadioButton1.setBackground(ColorMaster.getNotSelectedColor());
                speedRadioButton2.setBackground(ColorMaster.getSelectedColor());

                /** リスト設定オン */
                verySlowLabel.setEnabled(false);
                slowLabel.setEnabled(false);
                normalLabel.setEnabled(false);
                fastLabel.setEnabled(false);
                veryFastLabel.setEnabled(false);
                verySlowLabel.setBackground(ColorMaster.getNotSelectedColor());
                slowLabel.setBackground(ColorMaster.getNotSelectedColor());
                normalLabel.setBackground(ColorMaster.getNotSelectedColor());
                fastLabel.setBackground(ColorMaster.getNotSelectedColor());
                veryFastLabel.setBackground(ColorMaster.getNotSelectedColor());

                /** カスタム設定オフ */
                speedCustomLabel.setEnabled(true);
                speedCustomText.setEnabled(true);
                speedCustomLabel.setBackground(ColorMaster.getSelectedColor());
                speedCustomText.setBackground(Color.white);
            }
        });

        /** 確定ボタン */
        speedConfirmLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                /** リスト設定 */
                if (speedRadioButton1.isSelected()) {
                    getFrame().getBasePanel().getMainExecutePanel().getSpeedIndicateLabel().setText(speedMode);
                    getFrame().getBasePanel().getMainExecutePanel().setSpeedMode(speedMode);
                    getFrame().getBasePanel().getMainExecutePanel().getSpeedIndicateLabel().repaint();
                    /* 速度変化を適用 */
                    switch (speedMode) {
                        case "とても遅い":
                            getFrame().getBasePanel().getEditExecutePanel().setExeSpeed(500);
                            break;
                        case "遅い":
                            getFrame().getBasePanel().getEditExecutePanel().setExeSpeed(200);
                            break;
                        case "普通":
                            getFrame().getBasePanel().getEditExecutePanel().setExeSpeed(100);
                            break;
                        case "速い":
                            getFrame().getBasePanel().getEditExecutePanel().setExeSpeed(10);
                            break;
                        case "とても速い":
                            getFrame().getBasePanel().getEditExecutePanel().setExeSpeed(5);
                            break;
                    }
                    dispose();
                }
                /** カスタム設定 */
                else {
                    /** カスタム設定フィールドが1-100の範囲で設定されているか */
                    try {
                        int input = Integer.parseInt(speedCustomText.getText());
                        if (1<=input && input<=100) {
                            getFrame().getBasePanel().getMainExecutePanel().getSpeedIndicateLabel().setText("カスタム設定:" + speedCustomText.getText());
                            getFrame().getBasePanel().getMainExecutePanel().setSpeedMode(speedCustomText.getText());
                            getFrame().getBasePanel().getMainExecutePanel().getSpeedIndicateLabel().repaint();
                            /* 速度変化を適用 */
                            getFrame().getBasePanel().getEditExecutePanel().setExeSpeed(input);
                            dispose();
                        }else {
                            throw new Exception();
                        }
                    }catch (Exception e1) {
                        JLabel label = new JLabel("1～100の整数で入力してください。");
                        JOptionPane.showMessageDialog(speedConfirmLabel,label);
                    }
                }
                speedCustomText.setText("");
            }

        });

        add(panel);

        /** 現在の実行速度を取得し
         *  数値ならカスタム設定
         *  文字ならリスト設定と判定し初期化する */
        try {
            Integer.parseInt(getFrame().getBasePanel().getMainExecutePanel().getSpeedMode());
            speedRadioButton2.setSelected(true);
            /** ラジオボタンの背景 */
            speedRadioButton1.setBackground(ColorMaster.getNotSelectedColor());
            speedRadioButton2.setBackground(ColorMaster.getSelectedColor());

            /** リスト設定オフ */
            verySlowLabel.setEnabled(false);
            slowLabel.setEnabled(false);
            normalLabel.setEnabled(false);
            fastLabel.setEnabled(false);
            veryFastLabel.setEnabled(false);
            verySlowLabel.setBackground(ColorMaster.getNotSelectedColor());
            slowLabel.setBackground(ColorMaster.getNotSelectedColor());
            normalLabel.setBackground(ColorMaster.getNotSelectedColor());
            fastLabel.setBackground(ColorMaster.getNotSelectedColor());
            veryFastLabel.setBackground(ColorMaster.getNotSelectedColor());

            /** カスタム設定オン */
            speedCustomLabel.setEnabled(true);
            speedCustomText.setEnabled(true);
            speedCustomLabel.setBackground(ColorMaster.getSelectedColor());
            speedCustomText.setBackground(Color.WHITE);

            /** 現在のカスタム設定を格納 */
            speedCustomText.setText(getFrame().getBasePanel().getMainExecutePanel().getSpeedMode());
        }catch (Exception e1) {
            /** ラジオボタンの背景 */
            speedRadioButton1.setBackground(ColorMaster.getSelectedColor());
            speedRadioButton2.setBackground(ColorMaster.getNotSelectedColor());

            /** リスト設定オン */
            verySlowLabel.setBackground(ColorMaster.getNotSelectedColor());
            slowLabel.setBackground(ColorMaster.getNotSelectedColor());
            normalLabel.setBackground(ColorMaster.getNotSelectedColor());
            fastLabel.setBackground(ColorMaster.getNotSelectedColor());
            veryFastLabel.setBackground(ColorMaster.getNotSelectedColor());

            switch (getFrame().getBasePanel().getMainExecutePanel().getSpeedMode()) {
                case "とても遅い":
                    verySlowLabel.setBackground(ColorMaster.getSelectedColor());
                    break;
                case "遅い":
                    slowLabel.setBackground(ColorMaster.getSelectedColor());
                    break;
                case "普通":
                    normalLabel.setBackground(ColorMaster.getSelectedColor());
                    break;
                case "速い":
                    fastLabel.setBackground(ColorMaster.getSelectedColor());
                    break;
                case "とても速い":
                    veryFastLabel.setBackground(ColorMaster.getSelectedColor());
                    break;
            }

            /** カスタム設定オフ */
            speedCustomLabel.setEnabled(false);
            speedCustomText.setEnabled(false);
            speedCustomLabel.setBackground(ColorMaster.getNotSelectedColor());
            speedCustomText.setBackground(ColorMaster.getNotSelectedColor());
        }

        speedMode = getFrame().getBasePanel().getMainExecutePanel().getSpeedMode();

        setBounds(e.getXOnScreen(),e.getYOnScreen(),300,200);
        setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (speedRadioButton1.isSelected()) {
            /** リスト設定がオンのとき速度選択パネルを変更する */
            verySlowLabel.setBackground(ColorMaster.getNotSelectedColor());
            slowLabel.setBackground(ColorMaster.getNotSelectedColor());
            normalLabel.setBackground(ColorMaster.getNotSelectedColor());
            fastLabel.setBackground(ColorMaster.getNotSelectedColor());
            veryFastLabel.setBackground(ColorMaster.getNotSelectedColor());
            JPanel panel;
            if (e.getSource() instanceof JPanel) {
                panel = (JPanel) e.getSource();
                panel.setBackground(ColorMaster.getSelectedColor());
                if (panel == verySlowLabel) {
                    speedMode = "とても遅い";
                } else if (panel == slowLabel) {
                    speedMode = "遅い";
                } else if (panel == normalLabel) {
                    speedMode = "普通";
                } else if (panel == fastLabel) {
                    speedMode = "速い";
                } else if (panel == veryFastLabel) {
                    speedMode = "とても速い";
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        /** 右側が有効な場合左側の色が変わらないようにする */
        if(speedRadioButton2.isSelected()){
            verySlowLabel.setBackground(ColorMaster.getNotSelectedColor());
            slowLabel.setBackground(ColorMaster.getNotSelectedColor());
            normalLabel.setBackground(ColorMaster.getNotSelectedColor());
            fastLabel.setBackground(ColorMaster.getNotSelectedColor());
            veryFastLabel.setBackground(ColorMaster.getNotSelectedColor());
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        /** 右側が有効な場合左側の色が変わらないようにする */
        if(speedRadioButton2.isSelected()){
            verySlowLabel.setBackground(ColorMaster.getNotSelectedColor());
            slowLabel.setBackground(ColorMaster.getNotSelectedColor());
            normalLabel.setBackground(ColorMaster.getNotSelectedColor());
            fastLabel.setBackground(ColorMaster.getNotSelectedColor());
            veryFastLabel.setBackground(ColorMaster.getNotSelectedColor());
        }
    }
    @Override
    public void mouseEntered(MouseEvent e) {

    }
    @Override
    public void mouseExited(MouseEvent e) {

    }

    private class DialogBasePanel extends NewJPanel implements ComponentListener{

        public DialogBasePanel(BaseFrame frame) {
            super(frame);
            addComponentListener(this);
        }

        @Override
        public void handResize(int width, int height) {
            speedRadioButton1.setBounds(0, 0,width/2,height/7);
            speedRadioButton2.setBounds(width/2,0,width - width/2,height/7);
            verySlowLabel.setBounds(0,height/7,width/2,height/7);
            slowLabel.setBounds(0,(height/7)*2,width/2,height/7);
            normalLabel.setBounds(0,(height/7)*3,width/2,height/7);
            fastLabel.setBounds(0,(height/7)*4,width/2,height/7);
            veryFastLabel.setBounds(0,(height/7)*5,width/2,height/7);
            speedCustomLabel.setBounds(width/2,height/7,width - width/2,(height/7)*2);
            speedCustomIndicateLabel.setBounds(width/2, (height/7)*3, width - width/2, height/7);
            speedCustomText.setBounds(width/2,(height/7)*4,width - width/2,(height/7)*2);
            speedConfirmLabel.setBounds(0,(height/7)*6,width,height - (height/7)*6);
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