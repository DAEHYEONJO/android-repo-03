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
                binding.progressBar.layoutProgressBarRoot.isVisible =
                    loadState.source.refresh is LoadState.Loading
                binding.progressBar.layoutProgressBarRoot.bringToFront()

                val errorState = loadState.source.refresh as? LoadState.Error
                    ?:loadState.source.append as? LoadState.Error
                    ?:loadState.source.prepend as? LoadState.Error
                errorState?.let {
                    Toast.makeText(this@SearchActivity, "${it.error.message}", Toast.LENGTH_LONG).show()
                }
                
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.etSearchRepository.requestFocus()

        imm.showSoftInput(binding.etSearchRepository, 0)

        initRecyclerView()
        initResources()
        initAppBar()
        initEditText()
        initObserver()
    }

    private fun initObserver() {
        searchViewModel.repoList.observe(this@SearchActivity) { data ->
            lifecycleScope.launch{
                Log.e(TAG, " searchViewModel.repoList.observe(this@SearchActivity): ${data}", )
                binding.rvSearchRepository.visibility = View.VISIBLE
                searchRecyclerViewAdapter.submitData(data)
                Log.e(TAG, " searchViewModel.repoList.observe(this@SearchActivity): ${binding.rvSearchRepository.visibility}", )
            }
        }
    }

    private fun initRecyclerView() {
        with(binding) {
            rvSearchRepository.run {
                layoutManager =
                    LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
                adapter = searchRecyclerViewAdapter
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val lastVisibleItemPosition = ((recyclerView.layoutManager) as LinearLayoutManager).findLastVisibleItemPosition()
                        val itemTotalCount = recyclerView.adapter!!.itemCount - 1
                        if (lastVisibleItemPosition == itemTotalCount){
                            Log.e(TAG, "onScrolled: Last!!!!", )
                            binding.progressBar.layoutProgressBarRoot.isVisible = true
                            binding.progressBar.layoutProgressBarRoot.bringToFront()
                        }

                    }
                })
            }
        }
    }

    private fun initResources() {
        searchBtnDrawable =
            AppCompatResources.getDrawable(this@SearchActivity, R.drawable.ic_search_btn)!!
        deleteBtnDrawable =
            AppCompatResources.getDrawable(this@SearchActivity, R.drawable.ic_search_delete)!!
    }

    private fun initEditText() {
        with(binding.etSearchRepository) {
            setOnTouchListener { v, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_UP) {
                    compoundDrawables[2]?.let { drawable ->
                        val rangeX =
                            (right - drawable.bounds.width() - paddingEnd..right - paddingEnd).toRange()
                        if (motionEvent.rawX.toInt() in rangeX) {
                            setText("")
                        }
                    }
                }
                v.performClick()
            }

            setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER && text.isNotEmpty()) {
                    binding.layoutSearchEmptyListDescription.visibility = View.GONE
                    imm.hideSoftInputFromWindow(this.windowToken, 0)
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
                Log.e(TAG, "initEditText: noFocus")
                // api 21 이상인 경우 AppCompatResources.getDrawable
                // api 21 미만인 경우 resources.getDrawable
                setCompoundDrawables(left = searchBtnDrawable, right = null)
                binding.layoutSearchEmptyListDescription.visibility = View.VISIBLE
                binding.rvSearchRepository.visibility = View.GONE
                Log.e(TAG, " if (!hasFocus()) {: ${binding.rvSearchRepository.visibility}", )
            }else{
                Log.e(TAG, "initEditText: setOnFocusChangeListener focused")
                binding.layoutSearchEmptyListDescription.visibility = View.GONE
                //binding.rvSearchRepository.visibility = View.VISIBLE
                setCompoundDrawables(left = null, right = deleteBtnDrawable)
            }
            setOnFocusChangeListener { view, focus ->
                if (!focus) {
                    Log.e(TAG, "initEditText: setOnFocusChangeListener noFocus")
                    setCompoundDrawables(left = searchBtnDrawable)
                    if (text.isEmpty()) {
                        binding.layoutSearchEmptyListDescription.visibility = View.VISIBLE
                        binding.rvSearchRepository.visibility = View.GONE
                        Log.e(TAG, " setOnFocusChangeListener { view, focus ->\n" +
                                "                if (!focus) {: ${binding.rvSearchRepository.visibility}", )
                    }

                } else {
                    Log.e(TAG, "initEditText: setOnFocusChangeListener focused")
                    binding.layoutSearchEmptyListDescription.visibility = View.GONE
                    //binding.rvSearchRepository.visibility = View.VISIBLE
                    setCompoundDrawables(left = null, right = deleteBtnDrawable)
                    Log.e(TAG, " setOnFocusChangeListener { view, focus ->\n" +
                            "                else {: ${binding.rvSearchRepository.visibility}", )
                }
            }
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

    private fun initAppBar() {
        this.title = ""
        with(binding.appBarSearch) {
            setSupportActionBar(searchProfileToolBar)
            appBarTitleTv.text = resources.getString(R.string.app_bar_search)
            appBarBackBtn.setOnClickListener { finish() }
        }
    }
}