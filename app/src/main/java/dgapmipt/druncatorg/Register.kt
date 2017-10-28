//package dgapmipt.druncatorg
//
//import android.content.Context
//import android.content.DialogInterface
//import android.content.Intent
//import android.nfc.NfcAdapter
//import android.nfc.Tag
//import android.os.Bundle
//import android.os.Parcelable
//import android.os.Vibrator
//import android.support.v4.app.Fragment
//import android.support.v4.app.FragmentManager
//import android.support.v4.app.FragmentPagerAdapter
//import android.support.v7.app.AppCompatActivity
//import kotlinx.android.synthetic.main.activity_register.*
//
//
//class Register : AppCompatActivity() {
//
//    /**
//     * The [android.support.v4.view.PagerAdapter] that will provide
//     * fragments for each of the sections. We use a
//     * {@link FragmentPagerAdapter} derivative, which will keep every
//     * loaded fragment in memory. If this becomes too memory intensive, it
//     * may be best to switch to a
//     * [android.support.v4.app.FragmentStatePagerAdapter].
//     */
//    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
//
//    lateinit var mDataListener: OnDataReceivedListener
//
//    lateinit var admin: User
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_register)
//
//        //checkNFC()
//
//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        // Create the adapter that will return a fragment for each of the three
//        // primary sections of the activity.
//        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
//
//        // Set up the ViewPager with the sections adapter.
//        container.adapter = mSectionsPagerAdapter
//
//        admin = intent.getParcelableExtra("admin")
//
//        mDataListener.onDataReceived("huy")
//    }
//
//    protected override fun onNewIntent(intent: Intent) {
//        super.onNewIntent(intent)
//
//        handleIntent(intent)
//    }
//
//    private fun bytesToHexString(data: ByteArray): String {
//        val buf = StringBuffer()
//        for (i in 0 until data.size) {
//            var halfbyte = data[i].toInt() ushr 4 and 0x0F
//            var two_halfs = 0
//            do {
//                if (0 <= halfbyte && halfbyte <= 9)
//                    buf.append(('0' + halfbyte).toChar())
//                else
//                    buf.append(('A' + (halfbyte - 10)).toChar())
//                halfbyte = data[i].toInt() and 0x0F
//            } while (two_halfs++ < 1)
//        }
//        return buf.toString()
//    }
//
//    private fun handleIntent(intent: Intent) {
//        val scanResult = intent.getStringExtra("scanResult")
//        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent.action) {
//            val tag = intent.getParcelableExtra<Parcelable>(NfcAdapter.EXTRA_TAG) as Tag
//            val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
//            v!!.vibrate(500)
//            val id = bytesToHexString(tag.id)
//            mDataListener.onDataReceived(id)
//        } else if (scanResult != null) {
//            mDataListener.onDataReceived(scanResult)
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//
//        //checkNFC()
//
//        handleIntent(intent)
//    }
//
//    private fun checkNFC() {
//        var nfcAdapter = NfcAdapter.getDefaultAdapter(this)
//        if (nfcAdapter == null) {
//            val dialog = SimpleDialogBuilder(this, R.string.dialogNFCTitle, R.string.dialogNoNFC,
//                    DialogInterface.OnClickListener { dialog, which -> finish() }).create()
//            dialog.show()
//        } else if (!nfcAdapter.isEnabled()) {
//            val dialog = SimpleDialogBuilder(this, R.string.dialogNFCTitle, R.string.dialogNFCNotEnabled,
//                    DialogInterface.OnClickListener { dialog, which -> finish() }).create()
//            dialog.show()
//        }
//    }
//
//    interface OnDataReceivedListener {
//        fun onDataReceived(str: String)
//    }
//
//    /**
//     * A [FragmentPagerAdapter] that returns a fragment corresponding to
//     * one of the sections/tabs/pages.
//     */
//    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
//
//        override fun getItem(position: Int): Fragment {
//            if (position == 1) return RegisterFragment.newInstance()
//            else return ScanFragment.newInstance()
//        }
//
//        override fun getCount(): Int {
//            return 2
//        }
//    }
//}
