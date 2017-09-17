package KUU.FrameWorkComponent.OrderComponent;

import KUU.BaseComponent.BaseFrame;
import KUU.FrameWorkComponent.OrderComponent.EditComponent.SelectOrderPanel;
import KUU.GeneralComponent.GeneralItemPanel;
import KUU.Mode.MainOrderVariableMode;
import KUU.NewComponent.NewJPanel;
import Master.ColorMaster.ColorMaster;
import Master.ImageMaster.ImageMaster;
import ProcessTerminal.SyntaxSettings.Command;
import ProcessTerminal.SyntaxSettings.ListElement;
import ProcessTerminal.SyntaxSettings.Syntax;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * 命令モードでのエディタ領域の画面。
 */
public class EditOrderPanel extends NewJPanel implements MouseListener {
    private GeneralItemPanel         nowFunctionLabel;
    private DefaultListModel<String> programModel;
    private JList<String>            programList;
    private JScrollPane              programScroll;
    private GeneralItemPanel editLabel;
    private GeneralItemPanel deleteLabel;
    private GeneralItemPanel allDeleteLabel;
    private SelectOrderPanel selectOrderPanel;

    private boolean clickEditDeleteFlg;
    private boolean clickAddFlg;
    private int lineNumber;

    public EditOrderPanel(BaseFrame frame) {
        super(frame);
        setLayout(null);

        makeList();

        add(nowFunctionLabel = new GeneralItemPanel(null, null, ""));
        add(programScroll    = new JScrollPane(programList));
        add(editLabel        = new GeneralItemPanel(false, ImageMaster.getImageMaster().getEditIcon(), "編集"));
        add(deleteLabel      = new GeneralItemPanel(false, ImageMaster.getImageMaster().getDeleteIcon(), "削除"));
        add(allDeleteLabel   = new GeneralItemPanel(true,  ImageMaster.getImageMaster().getDeleteIcon(), "全削除"));
        add(selectOrderPanel = new SelectOrderPanel(frame));

        programList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        programList.addMouseListener(this);
        editLabel.addMouseListener(this);
        deleteLabel.addMouseListener(this);
        allDeleteLabel.addMouseListener(this);

        clickEditDeleteFlg = false;
        lineNumber = 0;
    }

    @Override
    public void handResize(int width, int height) {
        int partsWidth  = width/7;
        int partsHeight = height/12;

        nowFunctionLabel.setBounds(0, 0, width, 30);
        programScroll.setBounds(0, 30, width, partsHeight*9 - 30);
        editLabel.setBounds(0, partsHeight*9, partsWidth, (height - partsHeight*9)/3);
        deleteLabel.setBounds(0, partsHeight*9 + (height - partsHeight*9)/3, partsWidth, (height - partsHeight*9)/3);
        allDeleteLabel.setBounds(0, partsHeight*9 + ((height - partsHeight*9)/3)*2, partsWidth, height - (partsHeight*9 + ((height - partsHeight*9)/3)*2));

        selectOrderPanel.setBounds(partsWidth, partsHeight*9, width - partsWidth, height - partsHeight*9);
        selectOrderPanel.handResize(width - partsWidth,  height - partsHeight*9);
    }

    /** エディタ部の更新を行うメソッド */
    public void updateProgramList(){
        programModel.removeAllElements();
        String functionName = getFrame().getBasePanel().getSubOrderPanel().getFunctionName();
        for (String str : getFrame().getMasterTerminal().getFunctionGroup().searchFunction(functionName).getListString()){
            programModel.addElement(str);
        }
    }

    /** 現在の関数名をラベルに表示する */
    public void setNowFunctionName(){
        nowFunctionLabel.setText(getFrame().getBasePanel().getSubOrderPanel().getFunctionName());
        nowFunctionLabel.repaint();
    }

    /** 編集/削除パネルがクリック可能か設定する */
    public void setProgramEditDeleteCanClick(boolean flg){
        if (flg) {
            clickEditDeleteFlg = true;
            editLabel.setBackground(ColorMaster.getSelectableColor());
            deleteLabel.setBackground(ColorMaster.getSelectableColor());
        }else {
            clickEditDeleteFlg = false;
            editLabel.setBackground(ColorMaster.getBackColor());
            deleteLabel.setBackground(ColorMaster.getBackColor());
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        /** 命令選択パネル */
        if (e.getSource()instanceof JPanel) {
            JPanel panel = (JPanel) e.getSource();
            if (panel == editLabel && clickEditDeleteFlg) {
                String functionName = getFrame().getBasePanel().getSubOrderPanel().getFunctionName();
                ListElement element = getFrame().getMasterTerminal().searchOrder(functionName, lineNumber);
                if (element instanceof Command) {
                    selectOrderPanel.openEditDialog(((Command) element).getType(), e);
                } else if (element instanceof Syntax) {
                    selectOrderPanel.openEditDialog(((Syntax) element).getsType(), e);
                } else {
                    JOptionPane.showMessageDialog(editLabel, "編集したい行を選択してください。");
                }
            } else if (panel == deleteLabel && clickEditDeleteFlg) {
                String functionName = getFrame().getBasePanel().getSubOrderPanel().getFunctionName();
                getFrame().getMasterTerminal().removeOrder(functionName, lineNumber);
                getFrame().updateOrderPanel(false);
            } else if (panel == allDeleteLabel) {
                /**
                 * 警告ダイアログを生成
                 * YESなら変数、プログラムを全削除する
                 * main操作パネルを関数にセット
                 * エディタパネルをMAINにセット
                 */
                if (JOptionPane.showConfirmDialog(getFrame(), "プログラムおよび、変数を全て削除します。\n本当に全て削除してもよろしいですか？", "警告", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                    getFrame().getMasterTerminal().initAllProgram();
                    getFrame().getBasePanel().getMainOrderPanel().getFunctionLabel().setBackground(ColorMaster.getSelectedColor());
                    getFrame().getBasePanel().getMainOrderPanel().getVariableLabel().setBackground(ColorMaster.getNotSelectedColor());
                    getFrame().getBasePanel().getMainOrderPanel().getOneDimensionArrayLabel().setBackground(ColorMaster.getNotSelectedColor());
                    getFrame().getBasePanel().getMainOrderPanel().getTwoDimensionArrayLabel().setBackground(ColorMaster.getNotSelectedColor());
                    getFrame().getBasePanel().getMainOrderPanel().setVariableMode(MainOrderVariableMode.FUNCTION);
                    getFrame().getBasePanel().getSubOrderPanel().setFunctionName("MAIN");
                    getFrame().getBasePanel().getEditOrderPanel().getSelectOrderPanel().setPanelVisible("MAIN");
                    getFrame().updateOrderPanel(true);
                }
            }
            getFrame().setOrderPanelCanClick(false, false, (clickEditDeleteFlg || selectOrderPanel.getClickAddFlg()));
        }

        /** プログラムリスト */
        if (e.getSource()instanceof JList) {
            lineNumber = programList.getSelectedIndex();
            String functionName = getFrame().getBasePanel().getSubOrderPanel().getFunctionName();
            /** 編集/削除パネルがクリックできるか・命令挿入パネルがクリックできるかを判定 */
            clickEditDeleteFlg = getFrame().getMasterTerminal().searchFunction(functionName).isCanControlOrder(lineNumber);
            String str = programList.getSelectedValue();
            char c = str.charAt(str.length()-1);
            clickAddFlg = !(c == ':' || c == '}');
            getFrame().setOrderPanelCanClick(false, clickEditDeleteFlg, clickAddFlg);
        }
    }
    /** クリック時パネルが有効なら色変更 */
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource()instanceof JPanel && clickEditDeleteFlg){
            JPanel panel = (JPanel)e.getSource();
            if (panel == editLabel || panel == deleteLabel){
                panel.setBackground(ColorMaster.getClickedColor());
            }
        }
    }
    /** クリック終了時パネルの有効無効にあわせ色変更 */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getSource()instanceof JPanel){
            JPanel panel = (JPanel)e.getSource();
            if (panel == editLabel || panel == deleteLabel){
                if (clickEditDeleteFlg){
                    panel.setBackground(ColorMaster.getSelectableColor());
                }else {
                    panel.setBackground(ColorMaster.getBackColor());
                }
            }
        }
    }
    @Override
    public void mouseEntered(MouseEvent e) {
    }
    @Override
    public void mouseExited(MouseEvent e) {
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public DefaultListModel<String> getProgramModel() {
        return programModel;
    }

    public SelectOrderPanel getSelectOrderPanel() {
        return selectOrderPanel;
    }

    /** JListの表示設定 */
    private JList<String> makeList() {
        programModel = new DefaultListModel<>();
        programList = new DnDList<>();
        programList.setModel(programModel);
        programList.setCellRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                return this;
            }
        });
        return programList;
    }

    private class DnDList<E> extends JList<E> implements DragGestureListener, Transferable {
        private final Color LINE_COLOR = new Color(100, 100, 255);
        private static final String NAME = "test";
        private final DataFlavor FLAVOR = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType, NAME);
        private final Rectangle targetLine = new Rectangle();
        private int draggedIndex = -1;
        private int targetIndex = -1;

        DnDList() {
            super();
            new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, new CDropTargetListener(), true);
            DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (targetIndex >= 0) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(LINE_COLOR);
                g2.fill(targetLine);
                g2.dispose();
            }
        }

        private void initTargetLine(Point p) {
            Rectangle rect = getCellBounds(0, 0);
            int cellHeight = rect.height;
            int lineHeight = 2;
            int modelSize = getModel().getSize();
            targetIndex = -1;
            targetLine.setSize(rect.width, lineHeight);
            for (int i = 0; i < modelSize; i++) {
                rect.setLocation(0, cellHeight * i - cellHeight / 2);
                if (rect.contains(p)) {
                    targetIndex = i;
                    targetLine.setLocation(0, i * cellHeight);
                    break;
                }
            }
            if (targetIndex < 0) {
                targetIndex = modelSize;
                targetLine.setLocation(0, targetIndex * cellHeight - lineHeight);
            }
        }

        @Override
        public void dragGestureRecognized(DragGestureEvent e) {
            if (getSelectedIndices().length > 1) {
                return;
            }
            draggedIndex = locationToIndex(e.getDragOrigin());
            if (draggedIndex < 0) {
                return;
            }
            try {
                e.startDrag(DragSource.DefaultMoveDrop, this, new ListDragSourceListener());
            } catch (InvalidDnDOperationException ex) {
                ex.printStackTrace();
            }
        }


        @Override
        public Object getTransferData(DataFlavor flavor) {
            return this;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{FLAVOR};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor.getHumanPresentableName().equals(NAME);
        }

        private class CDropTargetListener implements DropTargetListener {
            @Override
            public void dragExit(DropTargetEvent e) {
                targetIndex = -1;
                repaint();
            }

            @Override
            public void dragEnter(DropTargetDragEvent e) {
                if (isDragAcceptable(e)) {
                    e.acceptDrag(e.getDropAction());
                } else {
                    e.rejectDrag();
                }
            }

            @Override
            public void dragOver(DropTargetDragEvent e) {
                if (isDragAcceptable(e)) {
                    e.acceptDrag(e.getDropAction());
                } else {
                    e.rejectDrag();
                    return;
                }
                initTargetLine(e.getLocation());
                repaint();
            }

            @Override
            public void dropActionChanged(DropTargetDragEvent e) {
            }

            /** 移動実行 */
            @SuppressWarnings("unchecked")
            @Override
            public void drop(DropTargetDropEvent e) {
                String functionName = getFrame().getBasePanel().getSubOrderPanel().getFunctionName();
                System.out.println("drag"+draggedIndex);
                System.out.println("target"+targetIndex);
                getFrame().getMasterTerminal().moveOrder(functionName, draggedIndex, targetIndex);
                e.dropComplete(false);
                targetIndex = -1;
                repaint();
                getFrame().updateOrderPanel(false);
                getFrame().setOrderPanelCanClick(false, false, false);
            }

            private boolean isDragAcceptable(DropTargetDragEvent e) {
                return isDataFlavorSupported(e.getCurrentDataFlavors()[0]);
            }

            private boolean isDropAcceptable(DropTargetDropEvent e) {
                return isDataFlavorSupported(e.getTransferable().getTransferDataFlavors()[0]);
            }
        }
    }


    private class ListDragSourceListener implements DragSourceListener {
        @Override
        public void dragEnter(DragSourceDragEvent e) {
            e.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
        }

        @Override
        public void dragExit(DragSourceEvent e) {
            e.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
        }

        @Override
        public void dragOver(DragSourceDragEvent e) { /* not needed */ }

        @Override
        public void dropActionChanged(DragSourceDragEvent e) { /* not needed */ }

        @Override
        public void dragDropEnd(DragSourceDropEvent e) { /* not needed */ }
    }
}

