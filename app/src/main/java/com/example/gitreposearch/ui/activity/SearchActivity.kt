package com.example.gitreposearch.ui.activity

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.gitreposearch.R
import com.example.gitreposearch.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    companion object{
        const val TAG = "SearchActivity"
    }
    private val binding: ActivitySearchBinding by lazy{ActivitySearchBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initAppBar()
        initEditText()
    }

    private fun initEditText() {
        with(binding){
            if (!searchEditText.hasFocus()) {
                Log.e(TAG, "initEditText: noFocus", )
                setCompoundDrawables(left = resources.getDrawable(R.drawable.ic_search_btn, null))
            }
            searchEditText.setOnFocusChangeListener { view, b ->
                if (!b){
                    Log.e(TAG, "initEditText: setOnFocusChangeListener noFocus", )
                    setCompoundDrawables(left = resources.getDrawable(R.drawable.ic_search_btn, null))
                }
                if (b){
                    Log.e(TAG, "initEditText: setOnFocusChangeListener focused", )
                    setCompoundDrawables(right = resources.getDrawable(R.drawable.ic_search_delete, null))
                }
            }
        }
    }

    private fun setCompoundDrawables(left: Drawable?=null, top: Drawable?=null, right: Drawable?=null, bottom: Drawable?=null){
        binding.searchEditText.setCompoundDrawablesWithIntrinsicBounds(
            left, top, right, bottom
        )
    }

    private fun initAppBar() {
        this.title = ""
        with(binding.searchAppBar) {
            setSupportActionBar(searchProfileToolBar)
            appBarTitleTv.text = "Search"
            appBarBackBtn.setOnClickListener { finish() }
        }
    }
}