package com.ghdev.followme.ui.mypage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.ghdev.followme.R
import kotlinx.android.synthetic.main.fragment_mypage.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MypageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MypageFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

       return inflater.inflate(R.layout.fragment_mypage, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_mypage_goto_mypick.setOnClickListener {
            activity?.let {
                val intent = Intent(context, MypageMypickActivity::class.java)
                startActivity(intent)
            }
        }

        btn_mypage_goto_myset.setOnClickListener {
            activity?.let {
                val intent = Intent(context, MypageMysettingActivity::class.java)
                startActivity(intent)
            }
        }
    }




}
