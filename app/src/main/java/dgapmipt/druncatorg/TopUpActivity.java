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
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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

public class TopUpActivity extends AppCompatActivity {
    Button add10;
    Button add20;
    Button add50;
    Button add100;
    private User admin;
    private Button addButton;
    private ProgressBar loginProgress;
    private ScrollView loginForm;
    private RequestQueue mRequestQueue;
    private NfcAdapter nfcAdapter;
    private String scannedStr;
    private TextView errorView;
    private EditText customSumView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);

        Intent intent = getIntent();
        admin = intent.getParcelableExtra("admin");

        loginProgress = findViewById(R.id.login_progress);
        loginForm = findViewById(R.id.login_form);
        errorView = findViewById(R.id.error_prompt);
        customSumView = findViewById(R.id.customSum);
        add10 = findViewById(R.id.add10);
        add20 = findViewById(R.id.add20);
        add50 = findViewById(R.id.add50);
        add100 = findViewById(R.id.add100);
        add10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);

                if (scannedStr != null) {
                    try {
                        if (admin == null) {
                            showProgress(false);
                            errorView.setText("Exception: Admin is null");
                        }
                        User user = new User(admin.getToken(), scannedStr, 0,
                                "", "", "");
                        topUp(user, "10");
                    } catch (Exception e) {
                        errorView.setText(String.format("%s%s", errorView.getText(), e.getMessage()));
                    }
                } else {
                    errorView.setText("Exception: scannedTag is null");
                }
            }
        });

        add20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);

                if (scannedStr != null) {
                    try {
                        if (admin == null) {
                            showProgress(false);
                            errorView.setText("Exception: Admin is null");
                        }
                        User user = new User(admin.getToken(), scannedStr, 0,
                                "", "", "");
                        topUp(user, "20");
                    } catch (Exception e) {
                        errorView.setText(String.format("%s%s", errorView.getText(), e.getMessage()));
                    }
                } else {
                    errorView.setText("Exception: scannedTag is null");
                }
            }
        });


        add50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);

                if (scannedStr != null) {
                    try {
                        if (admin == null) {
                            showProgress(false);
                            errorView.setText("Exception: Admin is null");
                        }
                        User user = new User(admin.getToken(), scannedStr, 0,
                                "", "", "");
                        topUp(user, "50");
                    } catch (Exception e) {
                        errorView.setText(String.format("%s%s", errorView.getText(), e.getMessage()));
                    }
                } else {
                    errorView.setText("Exception: scannedTag is null");
                }
            }
        });


        add100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);

                if (scannedStr != null) {
                    try {
                        if (admin == null) {
                            showProgress(false);
                            errorView.setText("Exception: Admin is null");
                        }
                        User user = new User(admin.getToken(), scannedStr, 0,
                                "", "", "");
                        topUp(user, "100");
                    } catch (Exception e) {
                        errorView.setText(String.format("%s%s", errorView.getText(), e.getMessage()));
                    }
                } else {
                    errorView.setText("Exception: scannedTag is null");
                }
            }
        });

        addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                showProgress(true);

                if (scannedStr != null) {
                    try {
                        if (admin == null) {
                            showProgress(false);
                            errorView.setText("Exception: Admin is null");
                        }
                        User user = new User(admin.getToken(), scannedStr, 0,
                                "", "", "");
                        topUp(user, customSumView.getText().toString());
                    } catch (Exception e) {
                        errorView.setText(String.format("%s%s", errorView.getText(), e.getMessage()));
                    }
                } else {
                    errorView.setText("Exception: scannedTag is null");
                }
            }
        });
        if (scannedStr != null)
            addButton.setEnabled(true);

        customSumView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

    private void topUp(final User user, final String sum) {
        mRequestQueue = Volley.newRequestQueue(this);

        String url = "http://9bm.ru/money/add/";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String raw_response) {
                        try {
                            JSONObject response = new JSONObject(raw_response);
                            if ((response.get("success").toString()).equals("1")) {
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
                params.put("summ", String.valueOf(sum));
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
            addButton.setEnabled(true);

        } else if (scanResult != null) {
            scannedStr = scanResult;
            addButton.setEnabled(true);
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
}
