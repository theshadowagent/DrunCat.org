package dgapmipt.druncatorg;

/**
 * Created by Shad on 28.10.2017.
 */

public class Scanner {
    private String scanResult;
    private OnDataReceivedListener mDataListener;

    public interface OnDataReceivedListener {
        void onDataReceived(String str);
    }

    public void setDataListener(OnDataReceivedListener listener) {
        this.mDataListener = listener;
    }

    public void saveScanResult(String tag) {
        scanResult = tag;
        if (mDataListener != null) mDataListener.onDataReceived(scanResult);
    }

    public String getScanResult() {
        return scanResult;
    }
}
