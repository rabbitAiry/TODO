package com.example.todo.home_fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todo.ActivityAllItem
import com.example.todo.MainActivity
import com.example.todo.R
import com.example.todo.databinding.FragmentMeBinding
import java.util.concurrent.CountDownLatch

class FragmentMe(val listener: StartActivityListener) : FragmentBase(listener) {
    private val TAG = "ActivityLoading"
    override val title = R.string.main_toolbar_me

    private var _binding: FragmentMeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMeBinding.inflate(inflater, container, false)
        val view: View = binding.root
        binding.fragmentMeAllItemButton.setOnClickListener {
            Log.d(TAG, "onCreateView: clicked")
            listener.startActivityForResult(
                Intent(context, ActivityAllItem::class.java),
                MainActivity.refresh_todo_list_request
            )
        }
        binding.fragmentMeBackgroundSettingButton.setOnClickListener { }
        return view
    }

    override fun initFragmentData(context: Context) {
        Log.d(TAG, "initFragmentData: Me")
    }

    override fun refreshData(context: Context) {
        TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}