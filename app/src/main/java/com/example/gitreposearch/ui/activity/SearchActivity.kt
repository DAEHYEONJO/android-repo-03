package com.example.gitreposearch.ui.activity

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.util.toRange
import com.example.gitreposearch.R
import com.example.gitreposearch.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    companion object{
        const val TAG = "SearchActivity"
    }
    private val binding: ActivitySearchBinding by lazy{ActivitySearchBinding.inflate(layoutInflater)}
    private lateinit var searchBtnDrawable: Drawable
    private lateinit var deleteBtnDrawable: Drawable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initResources()
        initAppBar()
        initEditText()
    }

    private fun initResources(){
        searchBtnDrawable = AppCompatResources.getDrawable(this@SearchActivity,R.drawable.ic_search_btn)!!
        deleteBtnDrawable = AppCompatResources.getDrawable(this@SearchActivity,R.drawable.ic_search_delete)!!
    }

    private fun initEditText() {
        with(binding.etSearchRepository){
            setOnTouchListener { v, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_UP){
                    compoundDrawables[2]?.let { drawable ->
                        val rangeX = (right - drawable.bounds.width() - paddingEnd .. right - paddingEnd).toRange()
                        if (motionEvent.rawX.toInt() in rangeX){
                            setText("")
                        }
                    }
                }
                v.performClick()
            }
            if (!hasFocus()) {
                Log.e(TAG, "initEditText: noFocus", )
                // api 21 이상인 경우 AppCompatResources.getDrawable
                // api 21 미만인 경우 resources.getDrawable
                setCompoundDrawables(left = searchBtnDrawable)
            }
            setOnFocusChangeListener { view, focus ->
                if (!focus){
                    Log.e(TAG, "initEditText: setOnFocusChangeListener noFocus", )
                    setCompoundDrawables(left = searchBtnDrawable)
                }
                if (focus){
                    Log.e(TAG, "initEditText: setOnFocusChangeListener focused", )
                    setCompoundDrawables(right = deleteBtnDrawable)
                }
            }
        }
    }

    private fun setCompoundDrawables(left: Drawable?=null, top: Drawable?=null, right: Drawable?=null, bottom: Drawable?=null){
        binding.etSearchRepository.setCompoundDrawablesWithIntrinsicBounds(
            left, top, right, bottom
        )
    }

    private fun initAppBar() {
        this.title = ""
        with(binding.appBarSearch) {
            setSupportActionBar(searchProfileToolBar)
            appBarTitleTv.text = resources.getString(R.string.app_bar_search)
            appBarBackBtn.setOnClickListener { finish() }
        }
    }
}