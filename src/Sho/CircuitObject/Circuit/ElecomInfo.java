package Sho.CircuitObject.Circuit;

import Master.ImageMaster.PartsDirections;
import Master.ImageMaster.PartsStandards;
import Master.ImageMaster.PartsStates;
import Master.ImageMaster.PartsVarieties;
import Sho.CopyTo.CannotCopyToException;
import Sho.CopyTo.CopyTo;
import Sho.IntegerDimension.IntegerDimension;

import java.util.ArrayList;

/**
 * 複数のCircuitBlockで共有する、電子部品の全体的な情報をまとめたクラス。
 */
public class ElecomInfo implements CopyTo {
    private PartsVarieties partsVarieties;
    private PartsStandards partsStandards;
    private PartsStates partsStates;
    private PartsDirections partsDirections;
    private int etcStatus;
    private IntegerDimension size;
    private int highLevelConnectSize;
    private ArrayList<CircuitLinkInfo> linkedTerminal;
    private boolean branch;

    public ElecomInfo() {
        size = new IntegerDimension();
        etcStatus = 0;
        linkedTerminal = new ArrayList<>();
        branch = false;
    }

    public ElecomInfo(PartsVarieties partsVarieties, PartsStandards partsStandards) {
        this.partsVarieties = partsVarieties;
        this.partsStandards = partsStandards;
        linkedTerminal = new ArrayList<>();
        branch = false;
    }

    public PartsVarieties getPartsVarieties() {
        return partsVarieties;
    }

    public void setPartsVarieties(PartsVarieties partsVarieties) {
        this.partsVarieties = partsVarieties;
    }

    public PartsStandards getPartsStandards() {
        return partsStandards;
    }

    public void setPartsStandards(PartsStandards partsStandards) {
        this.partsStandards = partsStandards;
    }

    public PartsStates getPartsStates() {
        return partsStates;
    }

    public void setPartsStates(PartsStates partsStates) {
        this.partsStates = partsStates;
    }

    public PartsDirections getPartsDirections() {
        return partsDirections;
    }

    public void setPartsDirections(PartsDirections partsDirections) {
        this.partsDirections = partsDirections;
    }

    public int getEtcStatus() {
        return etcStatus;
    }

    public void setEtcStatus(int etcStatus) {
        this.etcStatus = etcStatus;
    }

    public IntegerDimension getSize() {
        return size;
    }

    public void setSize(IntegerDimension size) {
        this.size = size;
    }

    public int getHighLevelConnectSize() {
        return highLevelConnectSize;
    }

    public void setHighLevelConnectSize(int highLevelConnectSize) {
        this.highLevelConnectSize = highLevelConnectSize;
    }

    public ArrayList<CircuitLinkInfo> getLinkedTerminal() {
        return linkedTerminal;
    }

    public void setLinkedTerminal(ArrayList<CircuitLinkInfo> linkedTerminal) {
        this.linkedTerminal = linkedTerminal;
    }

    public boolean isBranch() {
        return branch;
    }

    public void setBranch(boolean branch) {
        this.branch = branch;
    }

    @Override
    public String toString() {
        return "ElecomInfo :\n" +
                "   partsVarieties : " + partsVarieties + "\n" +
                "   partsStandards : " + partsStandards + "\n" +
                "   partsStates : " + partsStates + "\n" +
                "   partsDirections : " + partsDirections + "\n" +
                "   size : " + size.toString() + "\n" +
                "   linkedTerminal : " + linkedTerminal + "\n" +
                "   branch : " + branch
                ;
    }

    @Override
    public void copyTo(Object o) {
        if (o instanceof ElecomInfo) {
            ElecomInfo elecomInfo = (ElecomInfo)o;
            elecomInfo.partsVarieties = this.partsVarieties;
            elecomInfo.partsStandards = this.partsStandards;
            elecomInfo.partsStates = this.partsStates;
            elecomInfo.partsDirections = this.partsDirections;
            elecomInfo.etcStatus = this.etcStatus;
            this.size.copyTo(elecomInfo.getSize());
            elecomInfo.highLevelConnectSize = this.highLevelConnectSize;
            linkedTerminalCopyTo(elecomInfo.linkedTerminal);
            elecomInfo.branch = this.branch;
        }
        else {
            new CannotCopyToException("ElecomInfo");
        }
    }

    private void linkedTerminalCopyTo(ArrayList<CircuitLinkInfo> linkedTerminal) {
        linkedTerminal.clear();
        for (CircuitLinkInfo tmp : this.linkedTerminal) {
            linkedTerminal.add(tmp.clone());
        }
    }

    /** 種類と規格が一致するればtrueを返す */
    public boolean varietiesEquals(PartsVarieties varieties, PartsStandards standards) {
        return this.getPartsVarieties() == varieties && this.getPartsStandards() == standards;
    }
}
