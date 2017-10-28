//package dgapmipt.druncatorg
//
//import android.content.Context
//import android.content.Intent
//import android.net.Uri
//import android.nfc.NfcAdapter
//import android.nfc.Tag
//import android.os.Bundle
//import android.os.Parcelable
//import android.os.Vibrator
//import android.support.v4.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import kotlinx.android.synthetic.main.fragment_scan.*
//
//
///**
// * A simple [Fragment] subclass.
// * Activities that contain this fragment must implement the
// * [ScanFragment.OnFragmentInteractionListener] interface
// * to handle interaction events.
// * Use the [ScanFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class ScanFragment : Fragment(), Register.OnDataReceivedListener {
//
//    // TODO: Rename and change types of parameters
//    private var mParam1: String? = null
//    private var mParam2: String? = null
////
////    private var mListener: OnFragmentInteractionListener? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        (activity as Register).mDataListener = this
//
////        if (arguments != null) {
////            mParam1 = arguments.getString(ARG_PARAM1)
////            mParam2 = arguments.getString(ARG_PARAM2)
////        }
//
//    }
//
//    override fun onDataReceived(str: String) {
//        sendNfcTag(str)
//    }
//
//    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
//                              savedInstanceState: Bundle?): View? {
//        // Inflate the layout for this fragment
//        return inflater!!.inflate(R.layout.fragment_scan, container, false)
//    }
//
//    private fun sendNfcTag(tag: String) {
//        scannedTagText.text = tag
//    }
//
////    // TODO: Rename method, update argument and hook method into UI event
////    fun onButtonPressed(uri: Uri) {
////        if (mListener != null) {
////            mListener!!.onFragmentInteraction(uri)
////        }
////    }
////
////    override fun onAttach(context: Context?) {
////        super.onAttach(context)
////        if (context is OnFragmentInteractionListener) {
////            mListener = context
////        } else {
////            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
////        }
////    }
////
////    override fun onDetach() {
////        super.onDetach()
////        mListener = null
////    }
////
////    /**
////     * This interface must be implemented by activities that contain this
////     * fragment to allow an interaction in this fragment to be communicated
////     * to the activity and potentially other fragments contained in that
////     * activity.
////     *
////     *
////     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
////     */
////    interface OnFragmentInteractionListener {
////        // TODO: Update argument type and name
////        fun onFragmentInteraction(uri: Uri)
////    }
//
//    companion object {
//        // TODO: Rename parameter arguments, choose names that match
//        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//        private val ARG_PARAM1 = "param1"
//        private val ARG_PARAM2 = "param2"
//
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment ScanFragment.
//         */
//        // TODO: Rename and change types and number of parameters
////        fun newInstance(param1: String, param2: String): ScanFragment {
////            val fragment = ScanFragment()
////            val args = Bundle()
////            args.putString(ARG_PARAM1, param1)
////            args.putString(ARG_PARAM2, param2)
////            fragment.arguments = args
////            return fragment
////        }
//
//        fun newInstance(): ScanFragment {
//            val fragment = ScanFragment()
//            return fragment
//        }
//    }
//}
