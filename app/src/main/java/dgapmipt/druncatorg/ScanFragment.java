package dgapmipt.druncatorg;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScanFragment extends Fragment implements Scanner.OnDataReceivedListener {

    private TextView nfcTagView;
    private RegisterActivity mActivity;

    private Scanner scanner;

    public ScanFragment() {
        // Required empty public constructor
    }

    public static ScanFragment newInstance(Scanner scanner) {
        ScanFragment fragment = new ScanFragment();
        fragment.scanner = scanner;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (scanner == null) {
            scanner = new Scanner();
        }
        scanner.setDataListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_scan, container, false);
        nfcTagView = rootView.findViewById(R.id.scannedTagText);
        if (scanner.getScanResult() != null)
                seeNFCtag(scanner.getScanResult());
        return rootView;
    }

    @Override
    public void onDataReceived(String str) {
        seeNFCtag(str);
    }

    private void seeNFCtag(String tag) {
        nfcTagView.setText(tag);
    }
}
