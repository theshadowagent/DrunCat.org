package dgapmipt.druncatorg;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
//    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private User admin;
    private ViewPager mViewPager;
    private NfcAdapter nfcAdapter;

    private String scannedStr;
    private RequestQueue mRequestQueue;
    private ProgressBar loginProgress;
    private ScrollView loginForm;
    private Button regButton;
    private EditText fullNameView;
    private EditText groupView;
    private TextView errorView;
    private SeekBar coeffProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        admin = intent.getParcelableExtra("admin");

        loginProgress = findViewById(R.id.login_progress);
        loginForm = findViewById(R.id.login_form);
        regButton = findViewById(R.id.register_button);
        fullNameView = findViewById(R.id.fullName);
        groupView = findViewById(R.id.group);
        errorView = findViewById(R.id.error_prompt);
        coeffProgressBar = findViewById(R.id.coeffBar);
        regButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
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
                if (scannedStr != null) {
                    try {
                        if (admin == null) {
                            showProgress(false);
                            errorView.setText("Exception: Admin is null");
                        }
                        User user = new User(admin.getToken(), scannedStr, (coeffProgressBar.getProgress() / (float) 10),
                                name, surname, groupView.getText().toString());
                        register(user);
                    } catch (Exception e) {
                        errorView.setText(String.format("%s%s", errorView.getText(), e.getMessage()));
                    }
                } else {
                    errorView.setText("Exception: scannedTag is null");
                }
            }
        });
        if (scannedStr != null)
            regButton.setEnabled(true);

        fullNameView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        groupView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", admin.getToken());
        editor.putString("name", admin.getName());
        editor.putString("name", admin.getSurname());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        admin = intent.getParcelableExtra("admin");

        handleIntent(getIntent());

        initialize();
    }

    private void initialize() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String token = sharedPreferences.getString("token", "0");
        String name = sharedPreferences.getString("name", "");
        String surname = sharedPreferences.getString("surname", "");
        if (admin == null) {
            admin = new User(token, "", 1.0F, name, surname, "");
        }
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
        mRequestQueue = Volley.newRequestQueue(this);

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
                                finish();
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

        mRequestQueue.add(postRequest);
    }
    private void checkNFC() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Dialog dialog = new SimpleDialogBuilder(this, R.string.dialogNFCTitle, R.string.dialogNoNFC,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).create();
            dialog.show();
        } else if (!nfcAdapter.isEnabled()) {
            Dialog dialog = new SimpleDialogBuilder(this, R.string.dialogNFCTitle, R.string.dialogNFCNotEnabled,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).create();
            dialog.show();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        final String scanResult = intent.getStringExtra("scanResult");
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);
            final String id = bytesToHexString(tag.getId());
            scannedStr = id;
            regButton.setEnabled(true);

        } else if (scanResult != null) {
            scannedStr = scanResult;
            regButton.setEnabled(true);
        }
    }

    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("0x");
        if (src == null || src.length <= 0) {
            return null;
        }

        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            System.out.println(buffer);
            stringBuilder.append(buffer);
        }

        return stringBuilder.toString();
    }

//    public class SectionsPagerAdapter extends FragmentPagerAdapter {
//
//        public SectionsPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            if (position == 1) {
//                return RegisterFragment.newInstance(scanner, admin.getToken());
//            } else {
//                return ScanFragment.newInstance(scanner);
//            }
//        }
//
//        @Override
//        public int getCount() {
//            return 2;
//        }
//    }
}
