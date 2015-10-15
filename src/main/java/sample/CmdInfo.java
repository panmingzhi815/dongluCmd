package sample;

import java.io.Serializable;

/**
 * Created by xiaopan on 2015/10/14 0014.
 */
public class CmdInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private int index;
    private String name;
    private String cmd;

    public CmdInfo(int index, String name, String cmd) {
        this.index = index;
        this.name = name;
        this.cmd = cmd;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
}
