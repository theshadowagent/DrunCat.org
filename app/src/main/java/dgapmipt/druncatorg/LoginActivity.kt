package dgapmipt.druncatorg

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.nfc.NfcAdapter
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private lateinit var queue: RequestQueue

    private var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Disable navigation bar.
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_login)
//        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
//        val token = sharedPreferences!!.getString("token", "0")
//        val nameStr = sharedPreferences!!.getString("name", "")
//        val surname = sharedPreferences!!.getString("surname", "")
//
//        var admin = User(token = token, cardId = "", name = nameStr, surname = surname, group = "")
//        if (token != "0") {
//            intent = Intent(this, MainActivity::class.java);
//            intent.putExtra("admin", admin)
//            startActivity(intent);
//            finish();
//        } else {

            //TODO:        checkNFC()

            name.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    hideKeyboard(v)
                }
            }
            password.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    hideKeyboard(v)
                }
            }
            val typeFace = ResourcesCompat.getFont(this, R.font.poppins)
            password.typeface = typeFace
            password.transformationMethod = PasswordTransformationMethod()
            // Set up the login form.
            password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin()
                    return@OnEditorActionListener true
                }
                false
            })

            sign_in_button.setOnClickListener { attemptLogin() }
        }
//    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun attemptLogin() {
        queue = Volley.newRequestQueue(this)

        // Reset errors.
        nameWrapper.error = null
        passwordWrapper.error = null

        // Store values at the time of the login attempt.
        val nameStr = name.text.toString()
        val passwordStr = password.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the User entered one.
        if (!TextUtils.isEmpty(passwordStr) && !isPasswordValid(passwordStr)) {
            passwordWrapper.error = getString(R.string.error_invalid_password)
            focusView = password
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(nameStr)) {
            nameWrapper.error = getString(R.string.error_empty_name)
            focusView = name
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the User login attempt.
            if (currentFocus != null)
                hideKeyboard(currentFocus)
            else {
                hideKeyboard(name)
            }
            showProgress(true)
            auth(nameStr, passwordStr)
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 3
    }

    private fun checkNFC() {
        var nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            val dialog = SimpleDialogBuilder(this, R.string.dialogNFCTitle, R.string.dialogNoNFC,
                    DialogInterface.OnClickListener { dialog, which -> finish() }).create()
            dialog.show()
        } else if (!nfcAdapter.isEnabled()) {
            val dialog = SimpleDialogBuilder(this, R.string.dialogNFCTitle, R.string.dialogNFCNotEnabled,
                    DialogInterface.OnClickListener { dialog, which -> finish() }).create()
            dialog.show()
        }
    }

    private fun showProgress(show: Boolean) {
        login_progress.visibility = if (show) View.VISIBLE else View.GONE
        login_form.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun auth(name: String, passw: String) {
        try {
            val authUrl = "http://9bm.ru/session/auth/$passw/"

            val jsObjRequest = JsonObjectRequest(Request.Method.GET, authUrl, null,
                    Response.Listener<JSONObject> { response ->
                        if (response.get("success").toString() == "1") {
                            val token = response.get("token").toString()
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("token", token)
                            intent.putExtra("name", name)
                            startActivity(intent)
                            finish()
                        } else {
                            var error = response.get("error").toString()
                            if (error == "Bad pin") error = getString(R.string.error_incorrect_password)
                            showProgress(false)
                            passwordWrapper.error = error
                            password.requestFocus()
                        }
            }, Response.ErrorListener { error: VolleyError ->
                    passwordWrapper.error = error.toString()
                    password.requestFocus()
                    showProgress(false)
            })

            queue.add(jsObjRequest)

        } catch (e: InterruptedException) {
            Log.println(Log.INFO, "response", "Connection interrupted. Try again later.")
            passwordWrapper.error = "Connection interrupted. Try again later."
            password.requestFocus()
            showProgress(false)
        }
    }
}