package com.example.skyview.view.fragments

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skyview.R
import com.example.skyview.databinding.FragmentAlertBinding
import com.example.skyview.model.AlarmPojo
import com.example.skyview.model.Utility
import com.example.skyview.repository.AlarmRepository
import com.example.skyview.view.activities.AddAlarmActivity
import com.example.skyview.view.adapters.AlarmAdapter
import com.example.skyview.viewmodel.viewmodel.AlarmViewModel
import com.example.skyview.viewmodel.viewmodelfactory.AlarmViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "AlertFragment"
class AlertFragment : Fragment() {

    private lateinit var alarmFragmentBinding: FragmentAlertBinding
    private lateinit var alarmAdapter: AlarmAdapter
    lateinit var alarmViewModel : AlarmViewModel
    lateinit var alarmViewModelFactory : AlarmViewModelFactory
    lateinit var alarmList: List<AlarmPojo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        alarmViewModelFactory = AlarmViewModelFactory(AlarmRepository(requireContext()))
        alarmViewModel = ViewModelProvider(this, alarmViewModelFactory)
            .get(AlarmViewModel::class.java)
        alarmList = listOf()
        Log.i(TAG, "onCreate: ")

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        alarmFragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_alert,
            container,
            false
        )
        alarmFragmentBinding.lifecycleOwner = this
        alarmAdapter = AlarmAdapter(listOf()) {
            alarmViewModel.deleteAlarm(it)
        }
        alarmAdapter.notifyDataSetChanged()
        alarmFragmentBinding.alarmAdapter = alarmAdapter
        return alarmFragmentBinding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i(TAG, "onViewCreated: ")
        super.onViewCreated(view, savedInstanceState)
        alarmFragmentBinding.addAlertBtn.setOnClickListener {
            onClickOpenAddAlarm()
        }
        lifecycleScope.launch(Dispatchers.Unconfined){
            alarmViewModel.alarmData.observe(viewLifecycleOwner){
                alarmList = it
                Log.i(TAG, "onViewCreated:----- ${it.size} ")
            }
            Thread.sleep(500)
            withContext(Dispatchers.Main){
                Log.i(TAG, "onViewCreated:********* ${alarmList.size}")
                alarmAdapter.alarmsList = alarmList
                alarmAdapter.notifyDataSetChanged()
            }
        }
        alarmAdapter.notifyDataSetChanged()


        lifecycleScope.launch(Dispatchers.Main){
//            Log.i(TAG, "onViewCreated:********* ${alarmList.size}")
//            alarmAdapter.alarmsList = alarmList
//            alarmAdapter.notifyDataSetChanged()
        }


    }





    private fun checkPermission() : Boolean{
        var overlayShared : SharedPreferences = requireContext()
            .getSharedPreferences("overlay", AppCompatActivity.MODE_PRIVATE)
        return overlayShared.getBoolean("overlay", true)
    }

    private fun setPermissionGranted(){
        Utility.saveOverlayPermission(requireContext(), "overlay", false)
    }

    private fun onClickOpenAddAlarm(){
        //checkDrawOverlayPermission()
        if(checkPermission()){
            checkDrawOverlayPermission()
            setPermissionGranted()
        }else {
            val intent = Intent(requireContext(), AddAlarmActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkDrawOverlayPermission() {
        if (!Settings.canDrawOverlays(requireContext())) {
            val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
            alertDialogBuilder.setTitle(getString(R.string.permission_req_txt))
                .setMessage(
                    getString(R.string.permission_access_msg))
                .setPositiveButton(getString(R.string.ok_perm_txt)) { dialog: DialogInterface, _: Int ->
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + requireContext().packageName)
                    )
                    dialog.dismiss()
                    startActivityForResult(intent, 1)
                }.setNegativeButton(
                    getString(R.string.cancel_perm_txt)
                ) { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                }.show()
        }
    }

}