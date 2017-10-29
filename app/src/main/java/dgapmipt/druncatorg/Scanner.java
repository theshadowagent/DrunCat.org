package dgapmipt.druncatorg;

/**
 * Created by Shad on 28.10.2017.
 */

public class Scanner {
    private String scanResult;

    public void saveScanResult(String tag) {
        scanResult = tag;
    }

    public String getScanResult() {
        return scanResult;
    }
}
