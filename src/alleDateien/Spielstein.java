package alleDateien;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Spielstein {
    public boolean istBewegungsfaehig() {
        return false;
    }

    public List<int[]> getBewegungsziele() {
        return new ArrayList<>();
    }

    public int getFarbe() {
        return 0;
    }

    public Boolean getZugzwang() {
        return false;
    }

    public HashMap<List<Integer>, List<int[]>> getBewegungszieleMitPfad() {
        return null;
    }

    public List<List<int[]>> getBewegungspfadeSchlagen() {
        return null;
    }

    public Boolean getBewegungsfaehig() {
        return false;
    }
}
