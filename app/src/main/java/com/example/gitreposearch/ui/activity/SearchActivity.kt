package com.example.gitreposearch.ui.activity

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.util.toRange
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bumptech.glide.load.engine.Engine
import com.example.gitreposearch.GlobalApplication
import com.example.gitreposearch.R
import com.example.gitreposearch.ui.adapter.SearchRecyclerViewAdapter
import com.example.gitreposearch.databinding.ActivitySearchBinding
import com.example.gitreposearch.utils.CustomViewModelFactory
import com.example.gitreposearch.ui.viewmodel.SearchViewModel
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
    private val searchRecyclerViewAdapter: SearchRecyclerViewAdapter by lazy {
        SearchRecyclerViewAdapter().apply {
            addLoadStateListener { loadState ->
                binding.progressBar.layoutProgressBarRoot.isVisible = loadState.source.refresh is LoadState.Loading
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.etSearchRepository.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.etSearchRepository, 0)

        initRecyclerView()
        initResources()
        initAppBar()
        initEditText()

    }

    private fun initObserver() {
        searchViewModel.repoList.observe(this@SearchActivity) {
            lifecycleScope.launch {
                Log.e(TAG, "searchViewModel.repoList.observe() called")
                binding.rvSearchRepository.visibility = View.VISIBLE
                searchRecyclerViewAdapter.submitData(it)
            }
        }
    }

    private fun initRecyclerView() {
        with(binding) {
            rvSearchRepository.run {
                layoutManager =
                    LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
                adapter = searchRecyclerViewAdapter
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

            doAfterTextChanged {
                Log.e(TAG, "initEditText: ${it.toString()}")
                binding.rvSearchRepository.visibility = View.GONE
                Log.e(TAG, "initEditText: ${it.toString()} ${searchViewModel.preQuery.value}")
                if (it.toString() != searchViewModel.preQuery.value) {
                    lifecycleScope.launch {
                        searchViewModel.getRepoPaging(it.toString())
                    }
                }

                initObserver()

                if (it!!.isEmpty()) {
                    setCompoundDrawables(left = searchBtnDrawable)
                    binding.layoutSearchEmptyListDescription.visibility = View.VISIBLE
                    binding.rvSearchRepository.visibility = View.GONE
                } else {
                    setCompoundDrawables(right = deleteBtnDrawable)
                    binding.layoutSearchEmptyListDescription.visibility = View.GONE
                    //binding.rvSearchRepository.visibility = View.VISIBLE
                }

            }

            if (!hasFocus()) {
                Log.e(TAG, "initEditText: noFocus")
                // api 21 이상인 경우 AppCompatResources.getDrawable
                // api 21 미만인 경우 resources.getDrawable
                setCompoundDrawables(left = searchBtnDrawable, right = null)
                binding.layoutSearchEmptyListDescription.visibility = View.VISIBLE
                binding.rvSearchRepository.visibility = View.GONE
            }
            setOnFocusChangeListener { view, focus ->
                if (!focus) {
                    Log.e(TAG, "initEditText: setOnFocusChangeListener noFocus")
                    setCompoundDrawables(left = searchBtnDrawable)
                    if (text.isEmpty()) {
                        binding.layoutSearchEmptyListDescription.visibility = View.VISIBLE
                        binding.rvSearchRepository.visibility = View.GONE
                    }

                } else {
                    Log.e(TAG, "initEditText: setOnFocusChangeListener focused")
                    binding.layoutSearchEmptyListDescription.visibility = View.GONE
                    //binding.rvSearchRepository.visibility = View.VISIBLE
                    setCompoundDrawables(right = deleteBtnDrawable)
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