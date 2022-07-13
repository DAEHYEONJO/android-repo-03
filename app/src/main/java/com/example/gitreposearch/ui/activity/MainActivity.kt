package com.example.gitreposearch.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.gitreposearch.R
import com.example.gitreposearch.data.Token
import com.example.gitreposearch.databinding.ActivityMainBinding
import com.example.gitreposearch.ui.fragments.IssueFragment
import com.example.gitreposearch.ui.fragments.NotificationFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var token: Token

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getToken()
        initStartDisplay()
        initToggleButton()

    }

    private fun getToken(){
        token = intent.getSerializableExtra("token") as Token
    }

    private fun initStartDisplay() {
        supportFragmentManager.commit{
            replace<IssueFragment>(R.id.main_hostFrag)
        }
    }

    private fun initToggleButton() {
        with(binding){
            mainIssueTabBtn.setOnClickListener {
                mainNotificationTabBtn.setBackgroundResource(R.drawable.frame_notabbutton)
                mainIssueTabBtn.setBackgroundResource(R.drawable.frame_tabbutton)

                supportFragmentManager.commit {
                    replace<IssueFragment>(R.id.main_hostFrag)
                }
            }
            mainNotificationTabBtn.setOnClickListener {
                mainIssueTabBtn.setBackgroundResource(R.drawable.frame_notabbutton)
                mainNotificationTabBtn.setBackgroundResource(R.drawable.frame_tabbutton)

                supportFragmentManager.commit {
                    replace<NotificationFragment>(R.id.main_hostFrag)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.mainAppbar_search -> {
            // Todo : SearchActivity 로 화면전환
            true
        }

        R.id.mainAppbar_profile -> {
            // Todo : ProfileActivity 로 화면전환
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun initFilterSpinner() {
        /*
          ArrayAdapter.createFromResource(
            this,
              R.array.filter_option_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.menu_filter_option)
            // Apply the adapter to the spinner
            binding.mainFilterSpinner.adapter = adapter
            binding.mainFilterSpinner.setPopupBackgroundResource(R.drawable.frame_filter_popup)
            binding.mainFilterSpinner.dropDownVerticalOffset = 160

        }

        binding.mainFilterSpinner.setPopupBackgroundResource(R.drawable.frame_filter_spinner)
        binding.mainFilterSpinner.dropDownVerticalOffset = 160
        binding.mainFilterSpinner.adapter = FilterSpinnerAdapter(this)
         */
    }
}