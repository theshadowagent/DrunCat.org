package dgapmipt.druncatorg;


import android.app.Activity;
import android.app.VoiceInteractor;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment implements Scanner.OnDataReceivedListener {

    private TextView nfcTagView;
    private String token;
    private Scanner scanner;

    private RequestQueue mRequestQueue;
    private Button regButton;
    private ProgressBar loginProgress;
    private ScrollView loginForm;
    private EditText fullNameView;
    private EditText groupView;
    private TextView errorView;
    private SeekBar coeffProgressBar;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance(Scanner scanner, String token) {
        RegisterFragment fragment = new RegisterFragment();
        fragment.scanner = scanner;
        fragment.token = token;
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
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (OnArticleSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }

    public void onResume() {

        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        loginProgress = rootView.findViewById(R.id.login_progress);
        loginForm = rootView.findViewById(R.id.login_form);
        regButton = rootView.findViewById(R.id.register_button);
        fullNameView = rootView.findViewById(R.id.fullName);
        groupView = rootView.findViewById(R.id.group);
        errorView = rootView.findViewById(R.id.error_prompt);
        coeffProgressBar = rootView.findViewById(R.id.coeffBar);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);
                String fullName = fullNameView.getText().toString();
                String[] split_name = fullName.split(" ");
                String name = split_name[0];
                String surname;
                if (split_name.length > 1) {
                    surname = split_name[1];
                } else {
                    surname = "N/A";
                }
                Log.println(Log.INFO, "govno", token);
                User user = new User(token, "dummy", (coeffProgressBar.getProgress() / (float)10),
                        name, surname, groupView.getText().toString());
                register(user);
            }
        });
        if (scanner.getScanResult() != null)
            errorView.setText(scanner.getScanResult());
        return rootView;
    }

    @Override
    public void onDataReceived(String str) {
        errorView.setText(str);
    }

    private void showProgress(Boolean show) {
        if (show) {
            loginProgress.setVisibility(View.VISIBLE);
            loginForm.setVisibility(View.GONE);
        } else {
            loginProgress.setVisibility(View.GONE);
            loginForm.setVisibility(View.VISIBLE);
        }
    }


    private void register(final User user) {
        mRequestQueue = Volley.newRequestQueue(getContext());

        String url = "http://9bm.ru/user/add/";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

        @Override
            public void onResponse(String raw_response) {
                try {
                    JSONObject response = new JSONObject(raw_response);
                    if ((response.get("success").toString()).equals("1")) {
                        int userID = Integer.parseInt(response.get("user_id").toString());
                        showProgress(false);
                        getActivity().finish();
                    } else {
                        showProgress(false);
                        String error = response.getString("error");
                        errorView.setText(error);
                    }
                } catch (JSONException e) {
                    showProgress(false);
                    String error = e.getMessage();
                    errorView.setText(error);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showProgress(false);
                errorView.setText(error.toString());
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                params.put("token", user.getToken());
                params.put("rfid", user.getCardId());
                params.put("group", user.getGroup());
                params.put("name", user.getName());
                params.put("surname", user.getSurname());
                params.put("coefficient", String.valueOf(user.getCoefficent()));

                return params;
            }
        };

        Log.println(Log.INFO, "post", postRequest.getUrl());
        mRequestQueue.add(postRequest);
    }
}
