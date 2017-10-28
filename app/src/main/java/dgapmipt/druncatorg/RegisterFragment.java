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
public class RegisterFragment extends Fragment implements Scanner.OnDataReceivedListener {

    private TextView nfcTagView;

    private Scanner scanner;


    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance(Scanner scanner) {
        RegisterFragment fragment = new RegisterFragment();
        fragment.scanner = scanner;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        nfcTagView = (TextView) rootView.findViewById(R.id.scannedTagText);
        return rootView;
    }

    @Override
    public void onDataReceived(String str) {
        sendNFCtag(str);
    }

    private void sendNFCtag(String tag) {
        nfcTagView.setText(tag);
    }
}
