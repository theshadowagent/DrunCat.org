package dgapmipt.druncatorg

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import dgapmipt.druncatorg.R.id.heading
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var admin: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setAdmin()

        register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra("admin", admin)
            startActivity(intent)
        }
    }

    private fun setAdmin() {
        val token = intent.getStringExtra("token")
        val fullName = intent.getStringExtra("name")
        var splitName = fullName.split(" ")
        var name = splitName[0]
        var surname = ""
        if (splitName.size > 1) {
            var surname = splitName[1]
        } else {
            var surname = ""
        }

        heading.text = String.format(getString(R.string.hello_name), name)

        admin = User(token = token, cardId = "", name = name, surname = surname, group = "")
    }
}