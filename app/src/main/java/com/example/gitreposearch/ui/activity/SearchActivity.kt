package com.example.gitreposearch.ui.activity

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.util.toRange
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bumptech.glide.load.engine.Engine
import com.example.gitreposearch.GlobalApplication
import com.example.gitreposearch.R
import com.example.gitreposearch.ui.adapter.SearchRecyclerViewAdapter
import com.example.gitreposearch.databinding.ActivitySearchBinding
import com.example.gitreposearch.utils.CustomViewModelFactory
import com.example.gitreposearch.ui.viewmodel.SearchViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {

    companion object {
        const val TAG = "SearchActivity"
    }

    private val binding: ActivitySearchBinding by lazy {
        ActivitySearchBinding.inflate(
            layoutInflater
        )
    }
    private lateinit var searchBtnDrawable: Drawable
    private lateinit var deleteBtnDrawable: Drawable
    private val searchViewModel: SearchViewModel by viewModels {
        CustomViewModelFactory(
            GlobalApplication.repoFlowRepository
        )
    }

    private val imm: InputMethodManager by lazy { getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }

    private val searchRecyclerViewAdapter: SearchRecyclerViewAdapter by lazy {
        SearchRecyclerViewAdapter().apply {
            addLoadStateListener { loadState ->
                setProgressBarVisible(loadState.source.refresh is LoadState.Loading)
                binding.rvSearchRepository.isVisible = loadState.source.refresh is LoadState.NotLoading
//                if (loadState.source.refresh is LoadState.NotLoading){
//                    Log.e(TAG, "리싸이클러뷰 보이기: ", )
//                    binding.rvSearchRepository.visibility = View.VISIBLE
//                }

                val errorState = loadState.source.refresh as? LoadState.Error
                    ?:loadState.source.append as? LoadState.Error
                    ?:loadState.source.prepend as? LoadState.Error
                errorState?.let {
                    if (it.error.message == "End of List"){
                        searchViewModel.endOfListFlag.value = true
                    }
                    Toast.makeText(this@SearchActivity, "${it.error.message}", Toast.LENGTH_LONG).show()
                }
                Log.e(TAG, "addLoadStateListener refresh: ${loadState.source.refresh}", )
                Log.e(TAG, "addLoadStateListener append: ${loadState.source.append}", )
                Log.e(TAG, "addLoadStateListener prepend: ${loadState.source.prepend}", )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initAppBar()
        showKeyBoard()
        initResources()
        initObserver()
        initRecyclerView()
        initEditText()
    }

    private fun initObserver() {
        searchViewModel.repoList.observe(this@SearchActivity) { data ->
            lifecycleScope.launch{
                searchRecyclerViewAdapter.submitData(data)
            }
        }
    }

    private fun initRecyclerView() {
        with(binding) {
            rvSearchRepository.run {
                adapter = searchRecyclerViewAdapter
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (searchViewModel.endOfListFlag.value==true) return
                        val lastVisibleItemPosition = ((recyclerView.layoutManager) as LinearLayoutManager).findLastVisibleItemPosition()
                        val itemTotalCount = recyclerView.adapter!!.itemCount - 1
                        if (lastVisibleItemPosition == itemTotalCount){
                            with(progressBar.layoutProgressBarRoot){
                                isVisible = true
                                bringToFront()
                            }
                        }

                    }
                })
            }
        }
    }

    private fun initResources() {
        searchBtnDrawable = AppCompatResources.getDrawable(this@SearchActivity, R.drawable.ic_search_btn)!!
        deleteBtnDrawable = AppCompatResources.getDrawable(this@SearchActivity, R.drawable.ic_search_delete)!!
    }

    private fun initEditText() {
        with(binding.etSearchRepository) {
            setOnTouchListener { v, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_UP) {
                    compoundDrawables[2]?.let { drawable ->
                        
                        val rangeX = (right - drawable.bounds.width() - paddingEnd..right - paddingEnd).toRange()
                        if (motionEvent.rawX.toInt() in rangeX && text.isNotEmpty()) {
                            Log.e(TAG, "setOnTouchListener: click X Btn", )
                            //imm.hideSoftInputFromWindow(this.windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY)

                            binding.rvSearchRepository.visibility = View.GONE
                            binding.layoutSearchEmptyListDescription.visibility = View.VISIBLE
                            setText("")
                        }
                    }
                }
                v.performClick()
            }

            setOnKeyListener { view, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER && text.isNotEmpty()) {
                    Log.e(TAG, "setOnKeyListener: 엔터 누르기", )
                    searchViewModel.endOfListFlag.value = false
                    binding.layoutSearchEmptyListDescription.visibility = View.GONE
                    binding.rvSearchRepository.visibility = View.GONE
                    //searchViewModel.hasFocusOnKeyboard.value = false
                    closeKeyBoard()
                    //imm.hideSoftInputFromWindow(this.windowToken, 0)
                    lifecycleScope.launch {
                        searchViewModel.getRepoPaging(text.toString()).collectLatest {
                            Log.e(TAG, "initEditText: Collect ${it}", )
                            searchViewModel.repoList.value = it
                        }
                    }
                    true
                }else{
                    false
                }
            }

            if (!hasFocus()) {
                Log.e(TAG, "!hasFocus(): 초기 포커스 없음", )
                // api 21 이상인 경우 AppCompatResources.getDrawable
                // api 21 미만인 경우 resources.getDrawable
                setCompoundDrawables(left = searchBtnDrawable, right = null)
                binding.layoutSearchEmptyListDescription.visibility = View.VISIBLE
                binding.rvSearchRepository.visibility = View.GONE
                Log.e(TAG, " if (!hasFocus()) {: ${binding.rvSearchRepository.visibility}", )
            }else{
                Log.e(TAG, "hasFocus(): 초기 포커스 있음", )
                //binding.layoutSearchEmptyListDescription.visibility = View.GONE
                //binding.rvSearchRepository.visibility = View.VISIBLE
                setCompoundDrawables(left = null, right = deleteBtnDrawable)
            }
            setOnFocusChangeListener { view, focus ->
                if (!focus) {
                    Log.e(TAG, "setOnFocusChangeListener: 포커스 없는걸로 바뀜", )
                    setCompoundDrawables(left = searchBtnDrawable)
                    if (text.isEmpty()) {
                        binding.layoutSearchEmptyListDescription.visibility = View.VISIBLE
                        binding.rvSearchRepository.visibility = View.GONE
                        Log.e(TAG, " setOnFocusChangeListener { view, focus ->\n" +
                                "                (text.isEmpty()) {: ${binding.rvSearchRepository.visibility}", )
                    }

                } else {
                    Log.e(TAG, "setOnFocusChangeListener: 포커스 있는걸로 바뀜", )
                    //binding.layoutSearchEmptyListDescription.visibility = View.VISIBLE
                    //binding.rvSearchRepository.visibility = View.GONE
                    setCompoundDrawables(left = null, right = deleteBtnDrawable)
                }
            }
        }
    }

    private fun setProgressBarVisible(isVisible: Boolean){
        with(binding.progressBar.layoutProgressBarRoot){
            this.isVisible = isVisible
            bringToFront()
        }
    }

    private fun setCompoundDrawables(
        left: Drawable? = null,
        top: Drawable? = null,
        right: Drawable? = null,
        bottom: Drawable? = null
    ) {
        binding.etSearchRepository.setCompoundDrawablesWithIntrinsicBounds(
            left, top, right, bottom
        )
    }

    private fun showKeyBoard(){
        with(binding.etSearchRepository){
            imm.showSoftInput(this, InputMethodManager.SHOW_FORCED)
            requestFocus()
        }
    }

    private fun closeKeyBoard(){
        Log.e(TAG, "closeKeyBoard: ${this.currentFocus?.hasFocus()}", )
        this.currentFocus?.let {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private fun initAppBar() {
        this.title = ""
        with(binding.appBarSearch) {
            setSupportActionBar(searchProfileToolBar)
            appBarTitleTv.text = resources.getString(R.string.app_bar_search)
            appBarBackBtn.setOnClickListener {
                finish()
            }
        }
    }
}
