package Sho.CircuitObject.Operate;

import KUU.BaseComponent.BaseFrame;
import Master.ImageMaster.PartsDirections;
import Master.ImageMaster.PartsStandards;
import Master.ImageMaster.PartsStates;
import Master.ImageMaster.PartsVarieties;
import Sho.CircuitObject.Circuit.*;
import Sho.CircuitObject.Circuit.CircuitBorder.Borders;
import Sho.CircuitObject.Circuit.CircuitOperateCommand.Command;
import Sho.CircuitObject.Execute.Execute;
import Sho.CircuitObject.Graphed.BranchNodeConnect;
import Sho.CircuitObject.HighLevelConnect.HighLevelConnectInfo;
import Sho.CircuitObject.UnitPanel.CircuitUnitPanel;
import Sho.CircuitObject.UnitPanel.ExecuteUnitPanel;
import Sho.CircuitObject.UnitPanel.UnitPanel;
import Sho.IntegerDimension.IntegerDimension;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;

import static Master.ImageMaster.PartsDirections.*;
import static Sho.CircuitObject.SubCircuitPanelComponent.PartsAdd.PartsPanelKeyWords.getIntegerElecomInfoHashMap;
import static Sho.CircuitObject.UnitPanel.UnitPanel.UNIT_PIXEL;
import static java.lang.Thread.sleep;

/**
 * UnitPanelに対するボタン操作や分類困難な処理に特化した処理系クラス。
 */
public class OperateOperate_ {
    /**
     * 大元のJFrameのアドレスを保持する
     */
    private BaseFrame frame;
    /**
     * 導線のサンプルは全ウィンドウ共通なのでstatic
     */
    private static ArrayList<CircuitBlock> wireSample;

    public OperateOperate_(BaseFrame frame) {
        this.frame = frame;
    }

    /**
     * 全ウィンドウ共通のためstaticメソッド。
     */
    private static ArrayList<CircuitBlock> getWireSample() {
        if (wireSample == null) {
            wireSample = new ArrayList<>();
            for (int i = 0; i < getIntegerElecomInfoHashMap().size(); i++) {
                if (getIntegerElecomInfoHashMap().get(i).getPartsVarieties() == PartsVarieties.WIRE) {
                    CircuitBlock wire = new CircuitBlock(0, 0);
                    wire.setElecomInfo(getIntegerElecomInfoHashMap().get(i));
                    wire.getElecomInfo().setSize(new IntegerDimension());
                    wireSample.add(wire);
                }
            }
        }
        return wireSample;
    }

    /**
     * マウスカーソルの座標から、そのマウスカーソルが回路上のどの座標に重なっているかを求める。
     * マウスカーソルが回路上の座標からはみ出している場合は、nullを返す。
     */
    public void getMouseInIndex(UnitPanel panel, int my, int mx) {
        CircuitUnit u = panel.getCircuitUnit();
        my -= panel.getPaintBaseCo().getHeight();
        mx -= panel.getPaintBaseCo().getWidth();

        int inY = -1, inX = -1;
        for (int i = 0; i < panel.getCircuitSize().getHeight(); i++) {
            if (i * UNIT_PIXEL * panel.getPaintRatio() <= my && (i + 1) * UNIT_PIXEL * panel.getPaintRatio() > my) {
                inY = i;
                break;
            }
        }
        for (int j = 0; j < panel.getCircuitSize().getWidth(); j++) {
            if (j * UNIT_PIXEL * panel.getPaintRatio() <= mx && (j + 1) * UNIT_PIXEL * panel.getPaintRatio() > mx) {
                inX = j;
                break;
            }
        }
        if (inY > -1 && inX > -1) {
            /* inYとinXのどちらかが-1でなければcursorCoに格納する。 */
            if (panel.getCursorCo() == null) {
                panel.setCursorCo(new IntegerDimension());
            }
            panel.getCursorCo().setHeight(inY);
            panel.getCursorCo().setWidth(inX);
        } else {
            /* cursorCoインスタンスを破棄する */
            panel.setCursorCo(null);
        }
    }

    /**
     * ある座標二点を頂点とする矩形がある時、その右下の座標を返す。
     */
    public IntegerDimension getEndCoordinate(IntegerDimension a, IntegerDimension b) {
        return getStartCoordinate(a.getHeight(), a.getWidth(), b.getHeight(), b.getWidth());
    }

    /**
     * ある座標二点を頂点とする矩形がある時、その右下の座標を返す。
     */
    public IntegerDimension getEndCoordinate(int aY, int aX, int bY, int bX) {
        return new IntegerDimension(Math.max(aY, bY), Math.max(aX, bX));
    }

    /**
     * ある座標二点を頂点とする矩形がある時、その左上の座標を返す。
     */
    public IntegerDimension getStartCoordinate(IntegerDimension a, IntegerDimension b) {
        return getStartCoordinate(a.getHeight(), a.getWidth(), b.getHeight(), b.getWidth());
    }

    /**
     * ある座標二点を頂点とする矩形がある時、その左上の座標を返す。
     */
    public IntegerDimension getStartCoordinate(int aY, int aX, int bY, int bX) {
        return new IntegerDimension(Math.min(aY, bY), Math.min(aX, bX));
    }

    /**
     * 指定した始点と終点の領域に部品が存在するか、領域が基板の領域からあふれる場合はtrueを返す。
     * startまたはendがnullならばtrueを返す。
     */
    public boolean isError(UnitPanel panel, IntegerDimension start, IntegerDimension end) {
        /* nullならばtrueを返す */
        if (start == null || end == null) {
            return true;
        }
        return isError(panel, start.getHeight(), start.getWidth(), end.getHeight(), end.getWidth());
    }

    /**
     * 指定した始点と終点の領域に部品が存在するか、領域が基板の領域からあふれる場合はtrueを返す。
     * startまたはendがnullならばtrueを返す。
     */
    public boolean isError(UnitPanel panel, int startY, int startX, int endY, int endX) {
        /* 念のため、始点と終点を明示的に取得する */
        IntegerDimension start = panel.getOperateOperate().getStartCoordinate(startY, startX, endY, endX);
        IntegerDimension end = panel.getOperateOperate().getEndCoordinate(startY, startX, endY, endX);
        /* 領域外ならばtrueを返す */
        if (!(OperateDetection_.judgeOutOfBound(panel, start) && OperateDetection_.judgeOutOfBound(panel, end))) {
            return true;
        }
        /* １か所でも部品が存在すればtrueを返す */
        for (int i = start.getHeight(); i <= end.getHeight(); i++) {
            for (int j = start.getWidth(); j <= end.getWidth(); j++) {
                if (panel.getCircuitUnit().getCircuitBlock().getMatrix().get(i).get(j).isExist()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 指定された二点の座標を矩形とする領域内に存在する全てのブロック情報を一時情報として保持する。
     * 注意：前提として、保存対象のマスに適切なボーダが設定されている必要がある。
     */
    public void addPartsCommonCandidate(UnitPanel panel) {
        CircuitBlock b;
        for (int i = 0; i < panel.getCircuitSize().getHeight(); i++) {
            for (int j = 0; j < panel.getCircuitSize().getWidth(); j++) {
                b = panel.getCircuitUnit().getCircuitBlock().getMatrix().get(i).get(j);
                if (b.getBorder() == Borders.SELECTED) {
                    panel.getTmps().add(b);
                }
            }
        }
    }

    /**
     * PARTS_MOVEモードのPARTS_MOVEコマンド専用の処理。
     * 一時情報に追加されたブロックの元あった場所に空白の情報を「挿入」する。
     * clearだと一時情報にまで反映されてしまう。
     */
    public void partsMoveBeforeInfoDelete(UnitPanel panel) {
        int y, x;
        for (CircuitBlock b : panel.getTmps()) {
            y = b.getCircuitInfo().getAbco().getHeight();
            x = b.getCircuitInfo().getAbco().getWidth();
            panel.getCircuitUnit().getCircuitBlock().getMatrix().get(y).set(x, new CircuitBlock(y, x));
        }
        update(panel);
    }

    /**
     * PARTS_MOVEモードのPARTS_MOVEコマンド専用の処理。
     * 展開に失敗した場合に呼び出し、元あった位置に一時情報を戻す。
     */
    public void partsMoveAfterInfoReturn(UnitPanel panel) {
        for (CircuitBlock b : panel.getTmps()) {
            b.copyTo(panel.getCircuitUnit().getCircuitBlock().getMatrix().get(b.getCircuitInfo().getAbco().getHeight()).get(b.getCircuitInfo().getAbco().getWidth()));
        }
        update(panel);
    }

    /**
     * 一時保存している情報を保持している押下地点の相対座標に展開する。
     * しかし、展開の前に検査を行い、すべてのマスにおいて展開可能な場合のみ展開する。
     * 展開が成功したらtrueを、展開不可能な場合はfalseを返す。
     * なお、このメソッドの内部で一時情報に変更が加えられることはない。
     * 注意：このメソッドはパネルに保持されている一時情報を元に処理を行う。
     * 使用する一時情報：tmps cursorCo moveCursorCo
     */
    public boolean expand(UnitPanel panel) {
        if (panel instanceof CircuitUnitPanel) {
            CircuitUnitPanel cPanel = (CircuitUnitPanel) panel;
            if (cPanel.getCursorCo() != null && cPanel.getMoveCursorCo() != null && cPanel.getTmps() != null) {
                CircuitUnit u = cPanel.getCircuitUnit();
                int recoY, recoX, abcoY, abcoX;
                boolean flg = false;
                recoY = cPanel.getCursorCo().getHeight() - cPanel.getMoveCursorCo().getHeight();
                recoX = cPanel.getCursorCo().getWidth() - cPanel.getMoveCursorCo().getWidth();
                /* 展開検査：展開先のボーダが赤ならば展開不可能 */
                flg = u.getCircuitBlock().getMatrix().get(panel.getCursorCo().getHeight()).get(panel.getCursorCo().getWidth()).getBorder() == Borders.ERROR;
                /* 展開検査：展開した場合に規定数越えの可能性があれば展開不可能 */
                flg = !flg && judgeOverPartsCheck(cPanel);
                if (flg) {
                    /* 展開処理 */
                    for (CircuitBlock b : cPanel.getTmps()) {
                        abcoY = b.getCircuitInfo().getAbco().getHeight() + recoY;
                        abcoX = b.getCircuitInfo().getAbco().getWidth() + recoX;
                        b.copyTo(u.getCircuitBlock().getMatrix().get(abcoY).get(abcoX));
                    }
                    update(cPanel);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 一時保存したアドレス元のCircuitBlockを指定した絶対座標の位置に追加させる。
     * 配列の範囲をはみ出したり、他の部品と重なったり、指定個数以上の追加の場合、追加させることができない。
     */
    public void add(UnitPanel panel, IntegerDimension abco) {
        CircuitUnit u = panel.getCircuitUnit();
        CircuitBlock tmp = panel.getTmp();
        CircuitBlock base;
        IntegerDimension size = tmp.getElecomInfo().getSize();
        IntegerDimension start = new IntegerDimension(panel.getCursorCo().getHeight() - size.getHeight() / 2, panel.getCursorCo().getWidth() - size.getWidth() / 2);
        IntegerDimension end = new IntegerDimension(panel.getCursorCo().getHeight() + size.getHeight() / 2, panel.getCursorCo().getWidth() + size.getWidth() / 2);

        /* 範囲外や部品に重なることがなく、かつ配置最大数を超すことがなければ追加する */
        if (!isError(panel, start, end) && judgeOverPartsCheck(panel, tmp.getElecomInfo().getPartsVarieties(), tmp.getElecomInfo().getPartsStandards())) {
            for (int i = 0; i < tmp.getElecomInfo().getSize().getHeight(); i++) {
                for (int j = 0; j < tmp.getElecomInfo().getSize().getWidth(); j++) {
                    base = u.getCircuitBlock().getMatrix().get(i + start.getHeight()).get(j + start.getWidth());
                    tmp.copyTo(base);
                    base.getCircuitInfo().setReco(new IntegerDimension(i, j));
                    base.setExist(true);
                }
            }
            update(panel);
        }
    }

    /**
     * 指定した座標に存在する交点のパターンを変更する。
     * 指定した座標にある情報が導線かつ交点パターンでない場合は何もしない。
     */
    public void crossChange(UnitPanel panel, IntegerDimension abco) {
        CircuitBlock b, b1;
        ElecomInfo e;
        if (OperateDetection_.judgeOutOfBound(panel, abco)) {
            b = panel.getCircuitUnit().getCircuitBlock().getMatrix().get(abco.getHeight()).get(abco.getWidth());
            e = b.getElecomInfo();
            if (e.getPartsVarieties() == PartsVarieties.WIRE) {
                if (e.getPartsStandards() == PartsStandards._4) {
                    if (e.getPartsDirections() == UP) {
                        b1 = new CircuitBlock(0, 0);
                        getWireSample().get(4).copyTo(b1);
                        panel.getElecomInfoSelector().elecomInfoSelector(b1.getElecomInfo());
                        rotateInfo(b1, RIGHT);
                        b1.copyTo(b);
                        b.setExist(true);
                        update(panel);
                    } else if (e.getPartsDirections() == RIGHT) {
                        b1 = new CircuitBlock(0, 0);
                        getWireSample().get(5).copyTo(b1);
                        panel.getElecomInfoSelector().elecomInfoSelector(b1.getElecomInfo());
                        b1.copyTo(b);
                        b.setExist(true);
                        update(panel);
                    }
                } else if (e.getPartsStandards() == PartsStandards._5) {
                    b1 = new CircuitBlock(0, 0);
                    getWireSample().get(6).copyTo(b1);
                    panel.getElecomInfoSelector().elecomInfoSelector(b1.getElecomInfo());
                    b1.copyTo(b);
                    b.setExist(true);
                    update(panel);
                } else if (e.getPartsStandards() == PartsStandards._6) {
                    b1 = new CircuitBlock(0, 0);
                    getWireSample().get(4).copyTo(b1);
                    panel.getElecomInfoSelector().elecomInfoSelector(b1.getElecomInfo());
                    b1.copyTo(b);
                    b.setExist(true);
                    update(panel);
                }
            }
        }
    }

    /**
     * パネルの指定した座標に指定したブロックをコピーして配置する。
     * 導線結合などで使用している。
     */
    public void specifiedAdd(UnitPanel panel, IntegerDimension abco, CircuitBlock block) {
        block.copyTo(panel.getCircuitUnit().getCircuitBlock().getMatrix().get(abco.getHeight()).get(abco.getWidth()));
    }

    /**
     * ファイル読み込み専用メソッド。
     */
    public void specifiedAdd(UnitPanel panel, CircuitBlock block, int abcoY, int abcoX, PartsDirections dir) {
        /* 情報を具体化 */
        panel.getElecomInfoSelector().elecomInfoSelector(block.getElecomInfo());
        rotateInfo(block, dir);
        if (judgeOverPartsCheck(panel, block.getElecomInfo().getPartsVarieties(), block.getElecomInfo().getPartsStandards())) {
            CircuitUnit u = panel.getCircuitUnit();
            int height = block.getElecomInfo().getSize().getHeight();
            int width = block.getElecomInfo().getSize().getWidth();
            /* 部品枠が基板から出ていたら配置しない */
            if (OperateDetection_.judgeOutOfBound(panel, abcoY, abcoX) && OperateDetection_.judgeOutOfBound(panel, abcoY + height, abcoX + width)) {
                for (int i = abcoY; i < abcoY + height; i++) {
                    for (int j = abcoX; j < abcoX + width; j++) {
                        CircuitBlock tmp = u.getCircuitBlock().getMatrix().get(i).get(j);
                        block.copyTo(tmp);
                        tmp.getCircuitInfo().setReco(new IntegerDimension(i - abcoY, j - abcoX));
                        tmp.setExist(true);
                    }
                }
            }
        }
    }

    /**
     * 一時情報を展開する時に、展開時に規定数以上存在してはいけない部品が規定数を超えていなければtrueを、規定数以上ならfalseを返す。
     * こちらは回路上に部品を移動・複製する時に使用する。
     * 注意：前提として、expandメソッドから呼び出されることを想定しているため、冗長な条件判定を省いている。他のメソッドから呼び出す場合には、条件漏れに注意すること。
     */
    private boolean judgeOverPartsCheck(CircuitUnitPanel panel) {
        int powerNum, miconNum;
        CircuitInfo c;
        ElecomInfo e;
        powerNum = getOverLapParts(panel, PartsVarieties.POWER, PartsStandards.DC);
        miconNum = getOverLapParts(panel, PartsVarieties.PIC, PartsStandards._18PINS);
        for (CircuitBlock b : panel.getTmps()) {
            c = b.getCircuitInfo();
            e = b.getElecomInfo();
            if (c.getReco().equals(0, 0)) {
                if (e.getPartsVarieties() == PartsVarieties.POWER && e.getPartsStandards() == PartsStandards.DC) {
                    powerNum++;
                } else if (e.getPartsVarieties() == PartsVarieties.PIC && e.getPartsStandards() == PartsStandards._18PINS) {
                    miconNum++;
                }
            }
        }
        /* 配置したら？という数を保持しているため、配置後の数として判定する */
        if (powerNum > 10) {
            frame.getBasePanel().getSubCircuitPanel().getLowerPanel().getLog().addMessage(new String[]{
                "電源部品が規定数を超える可能性があります。",
                "電源部品の規定数は、１０個です。"
            });
        }
        if (miconNum > 1) {
            frame.getBasePanel().getSubCircuitPanel().getLowerPanel().getLog().addMessage(new String[]{
                "マイコンが規定数を超える可能性があります。",
                "マイコンの規定数は、１個です。"
            });
        }
        return powerNum <= 10 && miconNum <= 1;
    }

    /**
     * 回路上に規定数以上存在してはいけない部品があり、その部品の個数が規定数未満ならばtrueを、規定数以上ならばfalseを返す。
     * こちらは、回路上に部品を追加する時に使用する。
     */
    public boolean judgeOverPartsCheck(UnitPanel panel, PartsVarieties varieties, PartsStandards standards) {
        if (varieties == PartsVarieties.POWER && standards == PartsStandards.DC) {
            if (getOverLapParts(panel, varieties, standards) >= 10) {
                frame.getBasePanel().getSubCircuitPanel().getLowerPanel().getLog().addMessage(new String[]{
                    "電源部品が規定数を超える可能性があります。",
                    "電源部品の規定数は、１０個です。"
                });
                return false;
            }
        } else if (varieties == PartsVarieties.PIC && standards == PartsStandards._18PINS) {
            if (getOverLapParts(panel, varieties, standards) >= 1) {
                frame.getBasePanel().getSubCircuitPanel().getLowerPanel().getLog().addMessage(new String[]{
                    "マイコンが規定数を超える可能性があります。",
                    "マイコンの規定数は、１個です。"
                });
                return false;
            }
        }
        return true;
    }

    /**
     * 回路上に指定した種類の部品がいくつ存在するかを返す。
     * 部品のため、導線の個数は数えられない。
     */
    public int getOverLapParts(UnitPanel panel, PartsVarieties varieties, PartsStandards standards) {
        CircuitUnit u = panel.getCircuitUnit();
        int cnt = 0;
        CircuitBlock b;
        for (int y = 0; y < u.getCircuitBlock().getMatrix().size(); y++) {
            for (int x = 0; x < u.getCircuitBlock().getMatrix().get(0).size(); x++) {
                b = u.getCircuitBlock().getMatrix().get(y).get(x);
                if (b.getCircuitInfo().getReco().equals(0, 0)) {
                    if (b.getElecomInfo().getPartsVarieties() == varieties && b.getElecomInfo().getPartsStandards() == standards) {
                        cnt++;
                    }
                }
            }
        }
        return cnt;
    }

    /**
     * 指定したブロックの情報を、指定した向きにする。
     */
    public void rotateInfo(CircuitBlock b, PartsDirections direction) {
        int befNum, aftNum;
        befNum = getIntForDirection(b.getElecomInfo().getPartsDirections());
        aftNum = getIntForDirection(direction);
        rotateInfo(b, (aftNum - befNum) % 4);
    }

    /**
     * 指定したブロックの情報を指定した回数だけ回転させる。
     */
    public void rotateInfo(CircuitBlock b, int num) {
        int sizeTmp;
        for (int i = 0; i < num; i++) {
            rotateDirectionTo(b.getElecomInfo(), true);
            rotateLinkInfoTo(b.getElecomInfo(), true);
            sizeTmp = b.getElecomInfo().getSize().getHeight();
            b.getElecomInfo().getSize().setHeight(b.getElecomInfo().getSize().getWidth());
            b.getElecomInfo().getSize().setWidth(sizeTmp);
        }
    }

    public int getIntForDirection(PartsDirections d) {
        switch (d) {
            case UP:
                return 0;
            case RIGHT:
                return 1;
            case DOWN:
                return 2;
            case LEFT:
                return 3;
            default:
                return -1;
        }
    }

    /**
     * 一時情報を元に回転を行う。
     * 回転の基準座標は範囲指定された領域の、辺になる領域を矩形とした中央座標である。
     * ただし、回転後に一箇所でも範囲外に出たり他の部品や導線に重複するような場合は回転させることはできない。
     * 処理が成功した場合、rangeStartとrangeEndの値が回転後の座標に変わる。
     * 処理が成功した場合はtrueを、失敗した場合はfalseを返す。
     * 注意：このメソッドはパネルに保持されている一時情報を元に処理を行う。
     * 使用する一時情報：tmps rangeStart rangeEnd
     */
    public boolean rotate(UnitPanel panel, boolean direction) {
        if (panel instanceof CircuitUnitPanel) {
            CircuitUnitPanel cPanel = (CircuitUnitPanel) panel;
            if (cPanel.getCursorCo() != null && cPanel.getMoveCursorCo() != null && cPanel.getTmps() != null) {
                CircuitBlock b1;
                CircuitInfo c;
                ElecomInfo e;
                int minY, minX, maxY, maxX, baseY, baseX, rotaY, rotaX, tmp;
                boolean isMid, flg = false;
                /* 回転の妨げにならないよう、回転前の部品や導線を一時的に削除する */
                partsMoveBeforeInfoDelete(panel);
                /* 回転の基準座標を割り出す */
                minY = cPanel.getCircuitSize().getHeight() - 1;
                minX = cPanel.getCircuitSize().getWidth() - 1;
                maxY = 0;
                maxX = 0;
                for (CircuitBlock b : cPanel.getTmps()) {
                    if (b.getCircuitInfo().getAbco().getHeight() < minY) {
                        minY = b.getCircuitInfo().getAbco().getHeight();
                    }
                    if (b.getCircuitInfo().getAbco().getWidth() < minX) {
                        minX = b.getCircuitInfo().getAbco().getWidth();
                    }
                    if (b.getCircuitInfo().getAbco().getHeight() > maxY) {
                        maxY = b.getCircuitInfo().getAbco().getHeight();
                    }
                    if (b.getCircuitInfo().getAbco().getWidth() > maxX) {
                        maxX = b.getCircuitInfo().getAbco().getWidth();
                    }
                }
                isMid = (maxY - minY) % 2 != 0 && (maxX - minX) % 2 == 0;
                baseY = (maxY - minY) / 2 + minY;
                baseX = (maxX - minX) / 2 + minX;
                /* 割り出した基準座標を基準に回転させた時、回転後の位置が範囲外または何かと重複する場合は回転できない */
                for (CircuitBlock b : cPanel.getTmps()) {
                    c = b.getCircuitInfo();
                    rotaY = getRotateCoordinateY(c.getAbco().getHeight(), c.getAbco().getWidth(), baseY, baseX, direction, isMid);
                    rotaX = getRotateCoordinateX(c.getAbco().getHeight(), c.getAbco().getWidth(), baseY, baseX, direction, isMid);
                    if (OperateDetection_.judgeOutOfBound(cPanel, rotaY, rotaX)) {
                        if (cPanel.getCircuitUnit().getCircuitBlock().getMatrix().get(rotaY).get(rotaX).isExist()) {
                            flg = true;
                        }
                    } else {
                        flg = true;
                    }
                    if (flg) {
                        break;
                    }
                }
                /* フラグが立っていなければ回転処理する */
                if (!flg) {
                    for (CircuitBlock b : cPanel.getTmps()) {
                        c = b.getCircuitInfo();
                        rotaY = getRotateCoordinateY(c.getAbco().getHeight(), c.getAbco().getWidth(), baseY, baseX, direction, isMid);
                        rotaX = getRotateCoordinateX(c.getAbco().getHeight(), c.getAbco().getWidth(), baseY, baseX, direction, isMid);
                        b1 = new CircuitBlock(rotaY, rotaX);
                        b.copyTo(b1);
                        e = b1.getElecomInfo();
                        rotateDirectionTo(e, direction);
                        rotateLinkInfoTo(e, direction);
                        rotateCoordinateTo(b1.getCircuitInfo().getReco(), e.getSize().getHeight(), e.getSize().getWidth(), direction);
                        tmp = e.getSize().getWidth();
                        e.getSize().setWidth(e.getSize().getHeight());
                        e.getSize().setHeight(tmp);
                        cPanel.getCircuitUnit().getCircuitBlock().getMatrix().get(rotaY).set(rotaX, b1);
                    }
                    update(cPanel);
                    /* 指定範囲も回転させる */
                    minY = getRotateCoordinateY(cPanel.getRangeStart().getHeight(), cPanel.getRangeStart().getWidth(), baseY, baseX, direction, isMid);
                    minX = getRotateCoordinateX(cPanel.getRangeStart().getHeight(), cPanel.getRangeStart().getWidth(), baseY, baseX, direction, isMid);
                    maxY = getRotateCoordinateY(cPanel.getRangeEnd().getHeight(), cPanel.getRangeEnd().getWidth(), baseY, baseX, direction, isMid);
                    maxX = getRotateCoordinateX(cPanel.getRangeEnd().getHeight(), cPanel.getRangeEnd().getWidth(), baseY, baseX, direction, isMid);
                    cPanel.getRangeStart().setHeight(minY);
                    cPanel.getRangeStart().setWidth(minX);
                    cPanel.getRangeEnd().setHeight(maxY);
                    cPanel.getRangeEnd().setWidth(maxX);
                    return true;
                }
                /* 回転処理不可能だった場合、消した位置に一時情報を戻す */
                partsMoveAfterInfoReturn(cPanel);
            }
        }
        frame.getBasePanel().getSubCircuitPanel().getLowerPanel().getLog().addMessage(new String[]{
            "回転操作に失敗しました。",
            "回転後に基板からはみ出してしまうか、",
            "部品、導線に重なってしまう可能性があります。"
        });
        return false;
    }

    /**
     * 指定した座標を基準座標を基準にして回転させた時のY座標を返す。
     *
     * @param direction trueの時は右回転。falseの時は左回転。
     * @param isMid     trueの時は基準が完全に座標の境目にある時として補正をかける。
     */
    public static int getRotateCoordinateY(int y, int x, int baseY, int baseX, boolean direction, boolean isMid) {
        if (direction) {
            return x + baseY - baseX;
        } else {
            return -x + baseX + baseY + (isMid ? 1 : 0);
        }
    }

    /**
     * 指定した座標を基準座標を基準にして回転させた時のX座標を返す。
     *
     * @param direction trueの時は右回転。falseの時は左回転。
     * @param isMid     trueの時は基準が完全に座標の境目にある時として補正をかける。
     */
    public static int getRotateCoordinateX(int y, int x, int baseY, int baseX, boolean direction, boolean isMid) {
        if (direction) {
            return -y + baseY + baseX + (isMid ? 1 : 0);
        } else {
            return y + baseX - baseY;
        }
    }

    private void rotateCoordinateTo(IntegerDimension x, int h, int w, boolean direction) {
        int tmp;
        if (direction) {
            /* 右回転 */
            tmp = x.getHeight();
            x.setHeight(x.getWidth());
            x.setWidth(h - tmp - 1);
        } else {
            /* 左回転 */
            tmp = x.getWidth();
            x.setWidth(x.getHeight());
            x.setHeight(w - tmp - 1);
        }
    }

    private void rotateDirectionTo(ElecomInfo elecomInfo, boolean direction) {
        switch (elecomInfo.getPartsDirections()) {
            case UP:
                elecomInfo.setPartsDirections(direction ? RIGHT : LEFT);
                break;
            case RIGHT:
                elecomInfo.setPartsDirections(direction ? DOWN : UP);
                break;
            case DOWN:
                elecomInfo.setPartsDirections(direction ? LEFT : RIGHT);
                break;
            case LEFT:
                elecomInfo.setPartsDirections(direction ? UP : DOWN);
                break;
        }
    }

    private void rotateLinkInfoTo(ElecomInfo elecomInfo, boolean direction) {
        boolean linkTmp;
        int correspondTmp;
        TerminalDirection directionTmp;
        Iterator<CircuitLinkInfo> it = elecomInfo.getLinkedTerminal().iterator();
        while (it.hasNext()) {
            CircuitLinkInfo linkInfo = it.next();
            rotateCoordinateTo(linkInfo.getReco(), elecomInfo.getSize().getHeight(), elecomInfo.getSize().getWidth(), direction);
            if (direction) {
                /* linkの中身を右シフト */
                linkTmp = linkInfo.getLink()[linkInfo.getLink().length - 1];
                for (int i = linkInfo.getLink().length - 1; i > 0; i--) {
                    linkInfo.getLink()[i] = linkInfo.getLink()[i - 1];
                }
                linkInfo.getLink()[0] = linkTmp;

                /* correspondの中身を右シフト */
                correspondTmp = linkInfo.getTerminalCorrespond()[linkInfo.getTerminalCorrespond().length - 1];
                for (int i = linkInfo.getTerminalCorrespond().length - 1; i > 0; i--) {
                    linkInfo.getTerminalCorrespond()[i] = linkInfo.getTerminalCorrespond()[i - 1];
                }
                linkInfo.getTerminalCorrespond()[0] = correspondTmp;
                for (int i = 0; i < linkInfo.getTerminalCorrespond().length; i++) {
                    if (linkInfo.getTerminalCorrespond()[i] != -1) {
                        linkInfo.getTerminalCorrespond()[i] = (linkInfo.getTerminalCorrespond()[i] + 1) % 4;
                    }
                }

                /* terminalDirectionの中身を右シフト */
                directionTmp = linkInfo.getTerminalDirection()[linkInfo.getLink().length - 1];
                for (int i = linkInfo.getTerminalDirection().length - 1; i > 0; i--) {
                    linkInfo.getTerminalDirection()[i] = linkInfo.getTerminalDirection()[i - 1];
                }
                linkInfo.getTerminalDirection()[0] = directionTmp;
            } else {
                /* linkの中身を左シフト */
                linkTmp = linkInfo.getLink()[0];
                for (int i = 0; i < linkInfo.getLink().length - 1; i++) {
                    linkInfo.getLink()[i] = linkInfo.getLink()[i + 1];
                }
                linkInfo.getLink()[linkInfo.getLink().length - 1] = linkTmp;

                /* correspondの中身を右シフト */
                correspondTmp = linkInfo.getTerminalCorrespond()[0];
                for (int i = 0; i < linkInfo.getTerminalCorrespond().length - 1; i++) {
                    linkInfo.getTerminalCorrespond()[i] = linkInfo.getTerminalCorrespond()[i + 1];
                }
                linkInfo.getTerminalCorrespond()[linkInfo.getTerminalCorrespond().length - 1] = correspondTmp;
                for (int i = 0; i < linkInfo.getTerminalCorrespond().length; i++) {
                    if (linkInfo.getTerminalCorrespond()[i] != -1) {
                        linkInfo.getTerminalCorrespond()[i] = (linkInfo.getTerminalCorrespond()[i] + 3) % 4;
                    }
                }

                /* terminalDirectionの中身を左シフト */
                directionTmp = linkInfo.getTerminalDirection()[0];
                for (int i = 0; i < linkInfo.getTerminalDirection().length - 1; i++) {
                    linkInfo.getTerminalDirection()[i] = linkInfo.getTerminalDirection()[i + 1];
                }
                linkInfo.getTerminalDirection()[linkInfo.getTerminalDirection().length - 1] = directionTmp;
            }
        }
    }

    /**
     * 一時保存された座標リストをたどり、その座標に該当する導線を配置していく。
     */
    public void bond(UnitPanel panel) {
        ArrayList<IntegerDimension> list = panel.getIdTmps();
        int inDirection, outDirection;
        for (int i = 0; i < list.size(); i++) {
            inDirection = -1;
            outDirection = -1;
            if (i != 0) {
                inDirection = getNextDirection(list.get(i), list.get(i - 1));
            }
            if (i != list.size() - 1) {
                outDirection = getNextDirection(list.get(i), list.get(i + 1));
            }
            addMatchWire(panel, list.get(i), inDirection, outDirection);
        }
        update(panel);
    }

    /**
     * 部品端子と導線を自動結合する。
     * 指定された絶対座標に未接続な部品端子があり、その方向に導線がある場合、自動的にその導線と端子を接続する。
     * また、絶対座標に一方向でも未接続な部品端子があり、隣接マスに未接続な部品端子がある場合、その全ての端子を接続する。
     * 注意：未接続の部品端子および導線の判定には、そのマスに設定されたボーダ情報を用いている。
     */
    public void partsBond(UnitPanel panel, IntegerDimension abco) {
        CircuitUnit u = panel.getCircuitUnit();
        CircuitBlock b, b1;
        IntegerDimension next = new IntegerDimension();
        int inDirection, outDirection;

        b = u.getCircuitBlock().getMatrix().get(abco.getHeight()).get(abco.getWidth());
        /* ボーダ情報のないブロックが指定されていたら何もしない */
        if (b.getBorder() == null) {
            return;
        }
        if (b.getElecomInfo().getPartsVarieties() != PartsVarieties.WIRE) {
            /* 未接続な部品端子の場合：端子と導線を結合する */
            for (int i = 0; i < 4; i++) {
                next.setHeight(BranchNodeConnect.nextCoordinateY(abco.getHeight(), i));
                next.setWidth(BranchNodeConnect.nextCoordinateX(abco.getWidth(), i));
                if (OperateDetection_.judgeOutOfBound(panel, next)) {
                    b1 = u.getCircuitBlock().getMatrix().get(next.getHeight()).get(next.getWidth());
                    if (b1.getBorder() == Borders.BONDABLE) {
                        partsBondPoints(panel, abco, next);
                    }
                }
            }
        } else {
            /* 未接続な導線の場合：隣接するすべての部品端子と結合する */
            for (int i = 0; i < 4; i++) {
                next.setHeight(BranchNodeConnect.nextCoordinateY(abco.getHeight(), i));
                next.setWidth(BranchNodeConnect.nextCoordinateX(abco.getWidth(), i));
                if (OperateDetection_.judgeOutOfBound(panel, next)) {
                    b1 = u.getCircuitBlock().getMatrix().get(next.getHeight()).get(next.getWidth());
                    if (b1.getBorder() == Borders.SELECTABLE) {
                        partsBondPoints(panel, abco, next);
                    }
                }
            }
        }
    }

    /**
     * 隣接する二つの座標間に導線を追加する。
     * 注意：前提として引数の座標は隣接している必要がある。
     */
    private void partsBondPoints(UnitPanel panel, IntegerDimension a, IntegerDimension b) {
        int inDirection, outDirection;

        panel.getIdTmps().clear();
        panel.getIdTmps().add(a);
        panel.getIdTmps().add(b);
        for (int j = 0; j < panel.getIdTmps().size(); j++) {
            inDirection = -1;
            outDirection = -1;
            if (j != 0) {
                inDirection = getNextDirection(panel.getIdTmps().get(j), panel.getIdTmps().get(j - 1));
            }
            if (j != panel.getIdTmps().size() - 1) {
                outDirection = getNextDirection(panel.getIdTmps().get(j), panel.getIdTmps().get(j + 1));
            }
            addMatchWire(panel, panel.getIdTmps().get(j), inDirection, outDirection);
        }
        update(panel);
    }

    /**
     * 基板上のすべての未接続な部品端子を結合する。
     * 注意：前提として未接続な導線にBONDABLEボーダが設定されている必要がある。
     */
    public void allPartsBond(UnitPanel panel) {
        CircuitBlock b;
        for (int i = 0; i < panel.getCircuitSize().getHeight(); i++) {
            for (int j = 0; j < panel.getCircuitSize().getWidth(); j++) {
                b = panel.getCircuitUnit().getCircuitBlock().getMatrix().get(i).get(j);
                if (b.getBorder() == Borders.BONDABLE) {
                    partsBond(panel, b.getCircuitInfo().getAbco());
                }
            }
        }
    }

    /**
     * 隣接する二点の座標が、基準の座標から見てどの方向に隣接しているのかを求める。
     * ただし、完全に同じ座標を渡すと-1を返す。
     * 例：基準座標から見て隣接座標が上⇒0を返す。
     */
    private int getNextDirection(IntegerDimension base, IntegerDimension next) {
        if (base.equals(next)) {
            return -1;
        }
        if (OperateDetection_.getNearY(base.getHeight(), next.getHeight()) != -1) {
            return OperateDetection_.getNearY(base.getHeight(), next.getHeight());
        } else {
            return OperateDetection_.getNearX(base.getWidth(), next.getWidth());
        }
    }

    /**
     * 元々あった接続構成と進入方向、進出方向を元に、その座標に適切な新しい導線を追加する。
     */
    private void addMatchWire(UnitPanel panel, IntegerDimension base, int inDirection, int outDirection) {
        /* 基準座標に部品があった場合は何もしないで終わる */
        if (panel.getCircuitUnit().getCircuitBlock().getMatrix().get(base.getHeight()).get(base.getWidth()).isExist()) {
            if (panel.getCircuitUnit().getCircuitBlock().getMatrix().get(base.getHeight()).get(base.getWidth()).getElecomInfo().getPartsVarieties() != PartsVarieties.WIRE) {
                return;
            }
        }

        CircuitUnit u = panel.getCircuitUnit();
        CircuitInfo c = u.getCircuitBlock().getMatrix().get(base.getHeight()).get(base.getWidth()).getCircuitInfo();
        ElecomInfo e = u.getCircuitBlock().getMatrix().get(base.getHeight()).get(base.getWidth()).getElecomInfo();

        /* 始点か終点でなければ空中放置導線の場合は、端子情報を排除する */
        if (inDirection != -1 && outDirection != -1) {
            if (e.getPartsVarieties() == PartsVarieties.WIRE && e.getPartsStandards() == PartsStandards._0) {
                for (int i = 0; i < c.getTerminal().length; i++) {
                    c.getTerminal()[i] = false;
                }
            }
        }
        /* 進入と進出の端子、対応の情報を追加する */
        if (inDirection != -1) {
            c.getTerminal()[inDirection] = true;
        }
        if (outDirection != -1) {
            c.getTerminal()[outDirection] = true;
        }

        getMatchWire(panel, base);
    }

    /**
     * 渡された情報を元に、その場所に導線を配置する。
     */
    private void getMatchWire(UnitPanel panel, IntegerDimension base) {
        CircuitUnit u = panel.getCircuitUnit();
        CircuitInfo c = u.getCircuitBlock().getMatrix().get(base.getHeight()).get(base.getWidth()).getCircuitInfo();
        ElecomInfo e = u.getCircuitBlock().getMatrix().get(base.getHeight()).get(base.getWidth()).getElecomInfo();

        CircuitBlock copy = new CircuitBlock(0, 0);
        copy.getCircuitInfo().setReco(new IntegerDimension());
        CircuitInfo dummy = new CircuitInfo(0, 0);
        CircuitLinkInfo l;
        int rotateNum;
        int baseNum, sampNum;
        boolean baseFlg, sampFlg;

        /* 適切な導線を探る */
        for (CircuitBlock sample : getWireSample()) {
            c.copyTo(dummy);
            sample.copyTo(copy);
            copy.setExist(true);
            panel.getElecomInfoSelector().elecomInfoSelector(copy.getElecomInfo());
            l = copy.getElecomInfo().getLinkedTerminal().get(0);
            /* 最初は、端子数から比較し、同じ数でなければパスする */
            sampNum = 0;
            baseNum = 0;
            for (int i = 0; i < dummy.getTerminal().length; i++) {
                if (dummy.getTerminal()[i]) {
                    baseNum++;
                }
            }
            for (int i = 0; i < l.getLink().length; i++) {
                if (l.getLink()[i]) {
                    sampNum++;
                }
            }

            /* 完全に空の場合は、そのまま配置してスルー */
            if (baseNum == 0) {
                clear(copy);
                specifiedAdd(panel, c.getAbco(), copy);
                continue;
            }
            if (baseNum != sampNum) {
                continue;
            }
            switch (baseNum) {
                case 1:
                    /* 端子数が１なのは１種類しかないので、そのまま配置処理 */
                    rotateNum = 4 - getRotateNum(copy.getElecomInfo().getLinkedTerminal().get(0).getLink(), dummy.getTerminal());
                    rotateInfo(copy, rotateNum);
                    specifiedAdd(panel, c.getAbco(), copy);
                    return;
                case 2:
                    /* 端子が反対方向にあるかどうかで比較する */
                    baseFlg = false;
                    sampFlg = false;
                    for (int i = 0; i < dummy.getTerminal().length; i++) {
                        if (dummy.getTerminal()[i]) {
                            if (dummy.getTerminal()[(i + 2) % 4]) {
                                baseFlg = true;
                            }
                        }
                        if (l.getLink()[i]) {
                            if (l.getLink()[(i + 2) % 4]) {
                                sampFlg = true;
                            }
                        }
                    }
                    /* 一致した場合は配置処理 */
                    if (baseFlg == sampFlg) {
                        rotateNum = 4 - getRotateNum(copy.getElecomInfo().getLinkedTerminal().get(0).getLink(), dummy.getTerminal());
                        rotateInfo(copy, rotateNum);
                        specifiedAdd(panel, c.getAbco(), copy);
                        return;
                    }
                    break;
                case 3:
                    /* 端子数が３なのは１種類しかないので、そのまま配置処理 */
                    rotateNum = 4 - getRotateNum(copy.getElecomInfo().getLinkedTerminal().get(0).getLink(), dummy.getTerminal());
                    rotateInfo(copy, rotateNum);
                    specifiedAdd(panel, c.getAbco(), copy);
                    return;
                case 4:
                    /* 互いに節なら、そのまま配置 */
                    if (!e.isBranch()) {
                        if (!copy.getElecomInfo().isBranch()) {
                            specifiedAdd(panel, c.getAbco(), copy);
                            return;
                        }
                    }
                    /* 端子の対応方向で比較する */
                    else {
                        baseFlg = false;
                        sampFlg = false;
                        for (int i = 0; i < dummy.getTerminal().length; i++) {
                            if (dummy.getTerminalCorrespond()[i] != -1) {
                                if (dummy.getTerminalCorrespond()[(i + 2) % 4] == i) {
                                    baseFlg = true;
                                }
                            }
                            if (l.getTerminalCorrespond()[i] != -1) {
                                if (l.getTerminalCorrespond()[(i + 2) % 4] == i) {
                                    sampFlg = true;
                                }
                            }
                        }
                        /* 一致した場合は配置処理 */
                        if (baseFlg == sampFlg) {
                            rotateNum = getRotateNum(copy.getElecomInfo().getLinkedTerminal().get(0).getTerminalCorrespond(), dummy.getTerminalCorrespond(), copy);
                            rotateInfo(copy, rotateNum);
                            specifiedAdd(panel, c.getAbco(), copy);
                            return;
                        }
                    }
                    break;
            }
        }
    }

    /**
     * 導線結合の最短経路に配置されるであろう導線パターンを構築する。
     */
    public ArrayList<CircuitBlock> predictionMatchWire(UnitPanel panel) {
        /* 始点にマウスカーソルが重なっている場合はnullを返す */
        if (panel.getTmp().getCircuitInfo().getAbco().equals(panel.getCursorCo())) {
            return null;
        }
        ArrayList<IntegerDimension> list = panel.getIdTmps();
        ArrayList<CircuitBlock> blocks = new ArrayList<>();
        CircuitBlock b;
        int inDirection, outDirection;
        int num;
        boolean bList[];
        for (int i = 0; i < list.size(); i++) {
            b = new CircuitBlock(list.get(i).getHeight(), list.get(i).getWidth());
            inDirection = -1;
            outDirection = -1;
            num = 0;
            if (i != 0) {
                inDirection = getNextDirection(list.get(i), list.get(i - 1));
            }
            if (i != list.size() - 1) {
                outDirection = getNextDirection(list.get(i), list.get(i + 1));
            }
            /* 進入と進出の端子、対応の情報を追加する */
            if (inDirection != -1) {
                b.getCircuitInfo().getTerminal()[inDirection] = true;
            }
            if (outDirection != -1) {
                b.getCircuitInfo().getTerminal()[outDirection] = true;
            }
            bList = b.getCircuitInfo().getTerminal();
            /* 導線のパターンを構築 */
            for (int j = 0; j < bList.length; j++) {
                if (bList[j]) {
                    num++;
                }
            }
            b.getElecomInfo().setPartsVarieties(PartsVarieties.WIRE);
            if (num == 1) {
                if (bList[3]) {
                    b.getElecomInfo().setPartsStandards(PartsStandards._0);
                    b.getElecomInfo().setPartsDirections(UP);
                } else if (bList[0]) {
                    b.getElecomInfo().setPartsStandards(PartsStandards._0);
                    b.getElecomInfo().setPartsDirections(RIGHT);
                } else if (bList[1]) {
                    b.getElecomInfo().setPartsStandards(PartsStandards._0);
                    b.getElecomInfo().setPartsDirections(DOWN);
                } else if (bList[2]) {
                    b.getElecomInfo().setPartsStandards(PartsStandards._0);
                    b.getElecomInfo().setPartsDirections(LEFT);
                }
            } else {
                if (bList[1] && bList[3]) {
                    b.getElecomInfo().setPartsStandards(PartsStandards._1);
                    b.getElecomInfo().setPartsDirections(UP);
                } else if (bList[0] && bList[2]) {
                    b.getElecomInfo().setPartsStandards(PartsStandards._1);
                    b.getElecomInfo().setPartsDirections(RIGHT);
                } else if (bList[2] && bList[3]) {
                    b.getElecomInfo().setPartsStandards(PartsStandards._2);
                    b.getElecomInfo().setPartsDirections(UP);
                } else if (bList[0] && bList[3]) {
                    b.getElecomInfo().setPartsStandards(PartsStandards._2);
                    b.getElecomInfo().setPartsDirections(RIGHT);
                } else if (bList[0] && bList[1]) {
                    b.getElecomInfo().setPartsStandards(PartsStandards._2);
                    b.getElecomInfo().setPartsDirections(DOWN);
                } else if (bList[1] && bList[2]) {
                    b.getElecomInfo().setPartsStandards(PartsStandards._2);
                    b.getElecomInfo().setPartsDirections(LEFT);
                }
            }
            blocks.add(b);
        }
        return blocks;
    }

    /**
     * rotateを何回右シフトしたらbaseと一致するかを求める。
     * シフト回数が戻ってくる。
     * ただし、二つとも同じサイズでないと-1を返す。
     * また、何回やっても一致しなければ-1を返す。
     */
    private int getRotateNum(boolean[] base, boolean[] rotate) {
        if (base.length != rotate.length) {
            return -1;
        }
        boolean tmp;
        boolean flg;
        for (int i = 0; i < base.length; i++) {
            flg = false;
            for (int j = 0; j < base.length; j++) {
                if (base[j] != rotate[j]) {
                    flg = true;
                    break;
                }
            }
            if (!flg) {
                return i;
            }
            tmp = rotate[rotate.length - 1];
            for (int j = rotate.length - 1; j > 0; j--) {
                rotate[j] = rotate[j - 1];
            }
            rotate[0] = tmp;
        }
        return -1;
    }

    /**
     * rotateを何回右シフトしたらbaseと一致するかを求める。
     * こちらは、端子の対応関係で比較する。
     * シフト回数が戻ってくる。
     * ただし、二つとも同じサイズでないと-1を返す。
     * また、何回やっても一致しなければ-1を返す。
     */
    private int getRotateNum(int[] base, int[] rotate, CircuitBlock block) {
        if (base.length != rotate.length) {
            return -1;
        }
        int tmp;
        boolean flg;
        int c1 = -1, c2 = -1;
        /* 比較できる２点を抽出する */
        for (int i = 0; i < rotate.length; i++) {
            if (rotate[i] != -1) {
                if (c1 == -1) {
                    c1 = i;
                } else {
                    c2 = i;
                }
            }
        }

        for (int i = 0; i < base.length; i++) {
            flg = false;
            for (int j = 0; j < base.length; j++) {
                if (j == c1 || j == c2) {
                    if (base[j] != rotate[j]) {
                        flg = true;
                        break;
                    }
                }
            }
            if (!flg) {
                return i / 2;
            }
            c1 = (c1 + 1) % 4;
            c2 = (c2 + 1) % 4;
            tmp = rotate[rotate.length - 1];
            for (int j = rotate.length - 1; j > 0; j--) {
                rotate[j] = rotate[j - 1];
            }
            rotate[0] = tmp;
        }
        return -1;
    }

    /**
     * パネル上の全ての部品のステータスを０にする。
     */
    public void partsEditInit(UnitPanel panel) {
        for (int i = 0; i < panel.getCircuitSize().getHeight(); i++) {
            for (int j = 0; j < panel.getCircuitSize().getWidth(); j++) {
                panel.getCircuitUnit().getCircuitBlock().getMatrix().get(i).get(j).getElecomInfo().setEtcStatus(0);
            }
        }
    }

    /**
     * 指定された回路ブロックの中身をクリアする。
     */
    public void clear(CircuitBlock circuitBlock) {
        /* ブロック内データ削除(完全ではない) */
        circuitBlock.getElecomInfo().setPartsVarieties(null);
        circuitBlock.getElecomInfo().setPartsStandards(null);
        circuitBlock.getElecomInfo().setPartsStates(null);
        circuitBlock.getElecomInfo().setPartsDirections(null);
        circuitBlock.getElecomInfo().setEtcStatus(0);
        circuitBlock.getElecomInfo().getLinkedTerminal().clear();
        circuitBlock.getElecomInfo().getSize().setHeight(0);
        circuitBlock.getElecomInfo().getSize().setWidth(0);
        circuitBlock.getCircuitInfo().getReco().setHeight(0);
        circuitBlock.getCircuitInfo().getReco().setWidth(0);
        circuitBlock.getElecomInfo().setBranch(false);
        circuitBlock.setExist(false);
        circuitBlock.setBorder(null);
    }

    /**
     * 指定された二点の座標を矩形とする領域内に存在する全ての「選択されていない状態」の部品や導線を「選択された状態」にする。
     * もしくは、「選択された状態」の部品や導線を「選択されていない状態」にする。
     * isCollectがtrueの場合、グループ単位で追加するためhcTmpsに、falseの場合、座標単位で追加するためにtmpsに追加される。
     * 注意：前提として、矩形領域には適切なボーダ設定が施されているものとして処理を行う。
     */
    public void addDeleteCandidate(UnitPanel panel, IntegerDimension idA, IntegerDimension idB, boolean isCollect) {
        CircuitBlock b;
        CircuitInfo c, c1;
        ElecomInfo e;
        for (int i = Math.min(idA.getHeight(), idB.getHeight()); i <= Math.max(idA.getHeight(), idB.getHeight()); i++) {
            for (int j = Math.min(idA.getWidth(), idB.getWidth()); j <= Math.max(idA.getWidth(), idB.getWidth()); j++) {
                b = panel.getCircuitUnit().getCircuitBlock().getMatrix().get(i).get(j);
                c = b.getCircuitInfo();
                e = b.getElecomInfo();
                if (b.getBorder() == Borders.OVERLAP) {
                    /* 一時情報に追加する */
                    if (isCollect) {
                        /* 指定座標を含むすべてのグループを列挙し、それがリストに含まれていなければ追加する */
                        for (HighLevelConnectInfo hc : panel.getHcTmp(c.getAbco())) {
                            if (!panel.getHcTmps().contains(hc)) {
                                panel.addHcTmp(c.getAbco());
                            }
                        }
                    } else {
                        /* その相対座標を割り出して、全てを追加する */
                        for (int k = 0; k < e.getSize().getHeight(); k++) {
                            for (int l = 0; l < e.getSize().getWidth(); l++) {
                                c1 = panel.getCircuitUnit().getCircuitBlock().getMatrix().get(c.getAbco().getHeight() - c.getReco().getHeight() + k).get(c.getAbco().getWidth() - c.getReco().getWidth() + l).getCircuitInfo();
                                if (!panel.getIdTmps().contains(c1.getAbco())) {
                                    panel.getIdTmps().add(c1.getAbco());
                                }
                            }
                        }
                    }
                } else if (b.getBorder() == Borders.SELECTED) {
                    /* 一時情報に追加する */
                    if (isCollect) {
                        /* 指定座標を含むすべてのグループを列挙し、それがリストに含まれていれば削除する */
                        for (HighLevelConnectInfo hc : panel.getHcTmp(c.getAbco())) {
                            if (panel.getHcTmps().contains(hc)) {
                                panel.removeHcTmp(c.getAbco());
                            }
                        }
                    } else {
                        /* その相対座標を割り出して、全てを追加する */
                        for (int k = 0; k < e.getSize().getHeight(); k++) {
                            for (int l = 0; l < e.getSize().getWidth(); l++) {
                                c1 = panel.getCircuitUnit().getCircuitBlock().getMatrix().get(c.getAbco().getHeight() - c.getReco().getHeight() + k).get(c.getAbco().getWidth() - c.getReco().getWidth() + l).getCircuitInfo();
                                panel.getIdTmps().remove(c1.getAbco());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 一時情報を元に部品や導線を削除する。
     * isCollectがtrueの場合、グループ単位で削除し、falseの場合、座標単位で削除する。
     */
    public void delete(UnitPanel panel, boolean isCollect) {
        CircuitUnit u = panel.getCircuitUnit();
        CircuitInfo c;
        ElecomInfo e;
        ArrayList<IntegerDimension> list = new ArrayList<>();
        int inDirection, outDirection;
        if (isCollect) {
            /** グループ単位での削除 */
            for (HighLevelConnectInfo hc : panel.getHcTmps()) {
                /* グループの属性が節ならば、絶対座標の繰り返し削除で済ます */
                if (!hc.isBranch()) {
                    for (IntegerDimension abco : hc.getAbcos()) {
                        /* 辺の時は、端子がないか検証する */
                        c = u.getCircuitBlock().getMatrix().get(abco.getHeight()).get(abco.getWidth()).getCircuitInfo();
                        e = u.getCircuitBlock().getMatrix().get(abco.getHeight()).get(abco.getWidth()).getElecomInfo();
                        if (c.getReco().getHeight() == 0 || c.getReco().getHeight() == e.getSize().getHeight() - 1 || c.getReco().getWidth() == 0 || c.getReco().getWidth() == e.getSize().getWidth() - 1) {
                            removeNextBondWire(panel, c.getAbco(), -1, -1);
                        }
                        clear(u.getCircuitBlock().getMatrix().get(abco.getHeight()).get(abco.getWidth()));
                    }
                }
                /* 枝ならば、リストの端を一度求め、その端から他方の端へと辿る新しいリストを形成する */
                else {
                    list.clear();
                    getAlignmentList(panel, hc, getRemoveTerminal(panel, hc, hc.getAbcos().get(0)), list);
                    for (int i = 0; i < list.size(); i++) {
                        inDirection = -1;
                        outDirection = -1;
                        if (i != 0) {
                            inDirection = getNextDirection(list.get(i), list.get(i - 1));
                        }
                        if (i != list.size() - 1) {
                            outDirection = getNextDirection(list.get(i), list.get(i + 1));
                        }
                        /* 始点終点に隣接する結合点にも干渉する */
                        if (inDirection == -1 || outDirection == -1) {
                            removeNextBondWire(panel, list.get(i), inDirection, outDirection);
                        }
                        /* 単体ブロックの削除は、ただのクリアを使う */
                        if (inDirection == -1 && outDirection == -1) {
                            clear(u.getCircuitBlock().getMatrix().get(list.get(i).getHeight()).get(list.get(i).getWidth()));
                        } else {
                            removeMatchWire(panel, list.get(i), inDirection, outDirection);
                        }
                    }
                }
            }
        } else {
            /** マス単位での削除 */
            for (IntegerDimension id : panel.getIdTmps()) {
                removeNextBondWire(panel, id, -1, -1);
                clear(u.getCircuitBlock().getMatrix().get(id.getHeight()).get(id.getWidth()));
            }
        }
        update(panel);
        panel.resetIdTmps();
        panel.resetHcTmps();
    }

    /* グループ情報より、適当に端の座標を返す */
    public IntegerDimension getRemoveTerminal(UnitPanel panel, HighLevelConnectInfo info, IntegerDimension base) {
        ArrayList<IntegerDimension> list = new ArrayList<>();
        getAlignmentList(panel, info, base, list);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 与えられたグループ情報の構成座標を、端から線形配置されるように整列して返す。
     */
    public void getAlignmentList(UnitPanel panel, HighLevelConnectInfo info, IntegerDimension base, ArrayList<IntegerDimension> list) {
        CircuitUnit u = panel.getCircuitUnit();
        IntegerDimension next = new IntegerDimension();
        CircuitInfo baseInfo = new CircuitInfo(base.getHeight(), base.getWidth());
        int direction = -1;

        /* 基準座標がグループ構成座標でなければ何もしない */
        if (!info.getAbcos().contains(base)) {
            return;
        }
        /* 最初は向きを決める */
        for (int i = 0; i < 4; i++) {
            if (u.getCircuitBlock().getMatrix().get(base.getHeight()).get(base.getWidth()).getCircuitInfo().getTerminal()[i]) {
                next.setHeight(BranchNodeConnect.nextCoordinateY(base.getHeight(), i));
                next.setWidth(BranchNodeConnect.nextCoordinateX(base.getWidth(), i));
                if (OperateDetection_.judgeOutOfBound(panel, next.getHeight(), next.getWidth())) {
                    if (u.getCircuitBlock().getMatrix().get(next.getHeight()).get(next.getWidth()).isExist()) {
                        if (u.getCircuitBlock().getMatrix().get(next.getHeight()).get(next.getWidth()).getCircuitInfo().getTerminal()[(i + 2) % 4]) {
                            if (info.getAbcos().contains(next)) {
                                direction = i;
                                break;
                            }
                        }
                    }
                }
            }
        }
        ArrayList<CircuitInfo> terminalList = new ArrayList<>();
        terminalList.add(baseInfo);
        getAlignmentRecursive(panel, info, baseInfo, list, terminalList, direction);
    }

    /**
     * 指定された向きの反対にある端子の対応する端子がない、もしくは構成座標からはみ出したらリストに追加して返す。
     */
    private void getAlignmentRecursive(UnitPanel panel, HighLevelConnectInfo info, CircuitInfo baseInfo, ArrayList<IntegerDimension> list, ArrayList<CircuitInfo> terminalList, int direction) {
        CircuitUnit u = panel.getCircuitUnit();

        /* 構成座標からはみ出したら終わる */
        try {
            if (!info.getAbcos().contains(baseInfo.getAbco())) {
                return;
            }
        } catch (StackOverflowError e) {
        }

        /* 向きが存在しない場合は打ち止め */
        if (direction == -1) {
            list.add(baseInfo.getAbco());
            return;
        }
        CircuitInfo nextInfo = new CircuitInfo(BranchNodeConnect.nextCoordinateY(baseInfo.getAbco().getHeight(), direction), BranchNodeConnect.nextCoordinateX(baseInfo.getAbco().getWidth(), direction));
        /* 範囲外なら打ち止め */
        if (OperateDetection_.judgeOutOfBound(panel, nextInfo.getAbco().getHeight(), nextInfo.getAbco().getWidth())) {
            /* グループに含まれていないなら打ち止め */
            if (info.getAbcos().contains(nextInfo.getAbco())) {
                /* 端子リストに隣接座標が含まれていて、かつ探索対象の向きもtrueなら打ち止め */
                boolean flg = false;
                CircuitInfo exist = null;
                for (CircuitInfo c : terminalList) {
                    if (c.getAbco().equals(nextInfo.getAbco())) {
                        if (c.getTerminal()[(direction + 2) % 4]) {
                            exist = c;
                            flg = true;
                            break;
                        }
                    }
                }
                if (!flg) {
                    /* 隣接座標を端子リストに登録し、探索向きを自身に、探索向きの逆を隣接に登録する */
                    if (exist != null) {
                        nextInfo = exist;
                    } else {
                        terminalList.add(nextInfo);
                    }
                    baseInfo.getTerminal()[direction] = true;
                    nextInfo.getTerminal()[(direction + 2) % 4] = true;
                    getAlignmentRecursive(panel, info, nextInfo, list, terminalList, u.getCircuitBlock().getMatrix().get(nextInfo.getAbco().getHeight()).get(nextInfo.getAbco().getWidth()).getCircuitInfo().getTerminalCorrespond()[(direction + 2) % 4]);
                }
                list.add(baseInfo.getAbco());
            } else {
                list.add(baseInfo.getAbco());
            }
        } else {
            list.add(baseInfo.getAbco());
        }
    }

    /**
     * 元々あった接続構成と進入方向、進出方向を元に、その座標の削除後の適切な導線を配置する。
     */
    private void removeMatchWire(UnitPanel panel, IntegerDimension base, int inDirection, int outDirection) {
        CircuitUnit u = panel.getCircuitUnit();
        CircuitInfo c = u.getCircuitBlock().getMatrix().get(base.getHeight()).get(base.getWidth()).getCircuitInfo();

        /* 中間端子用：入出方向の端子を消す */
        if (inDirection != -1) {
            c.getTerminal()[inDirection] = false;
        }
        if (outDirection != -1) {
            c.getTerminal()[outDirection] = false;
        }
        /* 始点終点用：片方消した時に、残ったもう片方も消す */
        if (inDirection == -1) {
            if (c.getTerminalCorrespond()[outDirection] != -1) {
                c.getTerminal()[c.getTerminalCorrespond()[outDirection]] = false;
            }
        }
        if (outDirection == -1) {
            if (c.getTerminalCorrespond()[inDirection] != -1) {
                c.getTerminal()[c.getTerminalCorrespond()[inDirection]] = false;
            }
        }

        getMatchWire(panel, base);
    }

    /**
     * 始点終点に隣接する結合点の端を削除する。
     */
    private void removeNextBondWire(UnitPanel panel, IntegerDimension base, int inDirection, int outDirection) {
        IntegerDimension nextBase = new IntegerDimension();
        CircuitUnit u = panel.getCircuitUnit();
        CircuitInfo c = u.getCircuitBlock().getMatrix().get(base.getHeight()).get(base.getWidth()).getCircuitInfo();
        int count;
        boolean flg = false;

        /* 単体ブロックの場合、始点終点を兼ねるので二回 */
        if (inDirection == -1 && outDirection == -1) {
            for (int i = 0; i < c.getTerminal().length; i++) {
                if (c.getTerminal()[i]) {
                    nextBase.setHeight(BranchNodeConnect.nextCoordinateY(base.getHeight(), i));
                    nextBase.setWidth(BranchNodeConnect.nextCoordinateX(base.getWidth(), i));
                    if (OperateDetection_.judgeOutOfBound(panel, nextBase.getHeight(), nextBase.getWidth())) {
                        /* 隣接ブロックが導線でなければパス */
                        if (u.getCircuitBlock().getMatrix().get(nextBase.getHeight()).get(nextBase.getWidth()).getElecomInfo().getPartsVarieties() == PartsVarieties.WIRE) {
                            /* 節でなければ、対応する端子も削除する処理に入る */
                            if (u.getCircuitBlock().getMatrix().get(nextBase.getHeight()).get(nextBase.getWidth()).getElecomInfo().isBranch()) {
                                count = 0;
                                for (int j = 0; j < 4; j++) {
                                    if (u.getCircuitBlock().getMatrix().get(nextBase.getHeight()).get(nextBase.getWidth()).getCircuitInfo().getTerminal()[j]) {
                                        count++;
                                    }
                                }
                                /* 端子数が３以上残っているなら、対応する端子も消す */
                                if (count >= 3) {
                                    u.getCircuitBlock().getMatrix().get(nextBase.getHeight()).get(nextBase.getWidth()).getCircuitInfo().
                                        getTerminal()[u.getCircuitBlock().getMatrix().get(nextBase.getHeight()).get(nextBase.getWidth()).getCircuitInfo().getTerminalCorrespond()[(i + 2) % 4]] = false;
                                }
                            }
                            /* 端につながっている隣接ブロックの端子を削除する */
                            u.getCircuitBlock().getMatrix().get(nextBase.getHeight()).get(nextBase.getWidth()).getCircuitInfo().getTerminal()[(i + 2) % 4] = false;
                            getMatchWire(panel, nextBase);
                            flg = true;
                        }
                    }
                }
            }
        } else {
            /* 始点終点の対応端子が存在する場合は基準座標を隣接ブロックに合わせる */
            if (inDirection == -1) {
                if (c.getTerminalCorrespond()[outDirection] != -1) {
                    if (c.getTerminal()[c.getTerminalCorrespond()[outDirection]]) {
                        nextBase.setHeight(BranchNodeConnect.nextCoordinateY(base.getHeight(), c.getTerminalCorrespond()[outDirection]));
                        nextBase.setWidth(BranchNodeConnect.nextCoordinateX(base.getWidth(), c.getTerminalCorrespond()[outDirection]));
                        if (OperateDetection_.judgeOutOfBound(panel, nextBase.getHeight(), nextBase.getWidth())) {
                            /* 隣接ブロックが導線でなければパス */
                            if (u.getCircuitBlock().getMatrix().get(nextBase.getHeight()).get(nextBase.getWidth()).getElecomInfo().getPartsVarieties() == PartsVarieties.WIRE) {
                                /* 基準を変える */
                                c = u.getCircuitBlock().getMatrix().get(nextBase.getHeight()).get(nextBase.getWidth()).getCircuitInfo();
                                /* 端につながっている隣接ブロックの端子を削除する */
                                c.getTerminal()[(u.getCircuitBlock().getMatrix().get(base.getHeight()).get(base.getWidth()).getCircuitInfo().getTerminalCorrespond()[outDirection] + 2) % 4] = false;
                                getMatchWire(panel, nextBase);
                                flg = true;
                            }
                        }
                    }
                }
            } else {
                if (c.getTerminalCorrespond()[inDirection] != -1) {
                    if (c.getTerminal()[c.getTerminalCorrespond()[inDirection]]) {
                        nextBase.setHeight(BranchNodeConnect.nextCoordinateY(base.getHeight(), c.getTerminalCorrespond()[inDirection]));
                        nextBase.setWidth(BranchNodeConnect.nextCoordinateX(base.getWidth(), c.getTerminalCorrespond()[inDirection]));
                        /* 隣接ブロックが導線でなければパス */
                        if (u.getCircuitBlock().getMatrix().get(nextBase.getHeight()).get(nextBase.getWidth()).getElecomInfo().getPartsVarieties() == PartsVarieties.WIRE) {
                            /* 基準を変える */
                            c = u.getCircuitBlock().getMatrix().get(nextBase.getHeight()).get(nextBase.getWidth()).getCircuitInfo();
                            /* 端につながっている隣接ブロックの端子を削除する */
                            c.getTerminal()[(u.getCircuitBlock().getMatrix().get(base.getHeight()).get(base.getWidth()).getCircuitInfo().getTerminalCorrespond()[inDirection] + 2) % 4] = false;
                            getMatchWire(panel, nextBase);
                            flg = true;
                        }
                    }
                }
            }
        }
        /* 接続解析を行う */
        if (flg) {
            update(panel);
        }
    }

    /**
     * CircuitUnit内の全てのCircuitBlockを削除する。
     */
    public void allDelete(UnitPanel panel) {
        CircuitUnit u = panel.getCircuitUnit();
        if (u.getCommand().getCommand() != Command.EXECUTE) {
            for (int i = 0; i < panel.getCircuitSize().getHeight(); i++) {
                for (int j = 0; j < panel.getCircuitSize().getWidth(); j++) {
                    clear(u.getCircuitBlock().getMatrix().get(i).get(j));
                }
            }
            update(panel);
        }
    }

    /**
     * 接続解析を行う。
     * 再描画はしない。
     */
    public void update(UnitPanel panel) {
        /* 接続解析を行う */
        clearConnectInfo(panel);
        if (frame.getBasePanel().getEditCircuitPanel().getBranchNodeConnect().branchNodeConnect(panel)) {
            frame.getBasePanel().getEditCircuitPanel().setNodeExist(true);
        } else {
            frame.getBasePanel().getEditCircuitPanel().setNodeExist(false);
        }
    }

    /* 接続解析データをクリアする */
    private void clearConnectInfo(UnitPanel panel) {
        CircuitUnit u = panel.getCircuitUnit();

        for (int i = 0; i < panel.getCircuitSize().getHeight(); i++) {
            for (int j = 0; j < panel.getCircuitSize().getWidth(); j++) {
                for (int k = 0; k < u.getCircuitBlock().getMatrix().get(i).get(j).getCircuitInfo().getTerminal().length; k++) {
                    u.getCircuitBlock().getMatrix().get(i).get(j).getCircuitInfo().getTerminal()[k] = false;
                    u.getCircuitBlock().getMatrix().get(i).get(j).getCircuitInfo().getConnection()[k] = false;
                    u.getCircuitBlock().getMatrix().get(i).get(j).getCircuitInfo().getGroupedTerminal()[k] = false;
                }
                u.getCircuitBlock().getMatrix().get(i).get(j).getCircuitInfo().setHighLevelConnectIndex(0);
            }
        }
    }

    /**
     * シミュレーションを開始する。
     * 開始後は終了しない限り二度は開始できない。
     * シミュレーションが正常に開始されたらtrueを返す。そうでなければfalseを返す。
     */
    public boolean startSimulation(ExecuteUnitPanel panel) {
        CircuitUnit cirUnit = frame.getBasePanel().getEditCircuitPanel().getCircuitUnit();
        CircuitUnit exeUnit = panel.getCircuitUnit();
        try {
            if (panel.getExecutor() == null) {
                /* 部品が存在すれば実行可能ではある */
                if (frame.getBasePanel().getEditCircuitPanel().isNodeExist()) {
                    cirUnit.copyTo(exeUnit);
                    /* 第０解析フェーズ１：接続性解析 */
                    clearConnectInfo(panel);
                    frame.getBasePanel().getEditCircuitPanel().getBranchNodeConnect().branchNodeConnect(panel);
                    /* 第０解析フェーズ２：グラフによる回路解析 */
                    frame.getBasePanel().getEditCircuitPanel().getCircuitMatrixing().circuitMatrixing(panel);
                    /* 第０解析フェーズ３：余分なグループ情報の削除および再解析 */
                    if (frame.getBasePanel().getEditCircuitPanel().getBranchNodeConnect().branchNodeReconnect(panel)) {
                        /* 無駄を省略し、それでも部品が存在していればシミュレート開始 */
                        panel.setExecutor(new Execute(panel));
                        panel.getExecutor().start();
                        return true;
                    } else {
                        JOptionPane.showMessageDialog(frame, "回路に接続されている部品がないため実行できません。\n部品を回路に接続して下さい。", "エラーメッセージ", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "部品がないため実行できません。\n部品を配置し、回路に接続して下さい。", "エラーメッセージ", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "すでに実行しています。\n実行の重複はできません。", "エラーメッセージ", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * シミュレーションを終了する。
     * 終了後は何度呼び出しても何もしない。
     */
    public void stopSimulation(ExecuteUnitPanel panel) {
        if (panel.getExecutor() != null) {
            panel.getExecutor().setRunStop(true);
        }
    }

    /**
     * 指定した座標を含む部品構成ブロックの状態を、指定したステートにする。
     */
    public void setPartsStates(UnitPanel panel, IntegerDimension abco, PartsStates states) {
        ArrayList<HighLevelConnectInfo> infos = panel.getHcTmp(abco);
        for (HighLevelConnectInfo info : infos) {
            for (IntegerDimension co : info.getAbcos()) {
                panel.getCircuitUnit().getCircuitBlock().getMatrix().get(co.getHeight()).get(co.getWidth()).getElecomInfo().setPartsStates(states);
            }
        }
    }
}
