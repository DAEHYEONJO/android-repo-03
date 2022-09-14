package com.example.gitreposearch.ui.activity

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.util.toRange
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gitreposearch.GlobalApplication
import com.example.gitreposearch.R
import com.example.gitreposearch.databinding.ActivitySearchBinding
import com.example.gitreposearch.ui.adapter.SearchRecyclerViewAdapter
import com.example.gitreposearch.ui.viewmodel.SearchViewModel
import com.example.gitreposearch.utils.CustomViewModelFactory
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class SearchActivity : AppCompatActivity() {

    companion object {
        const val TAG = "SearchActivity"
    }

    private val binding: ActivitySearchBinding by lazy {
        ActivitySearchBinding.inflate(
            layoutInflater
        )
    }
    private val searchBtnDrawable: Drawable by lazy {
        AppCompatResources.getDrawable(this@SearchActivity, R.drawable.ic_search_btn)!!
    }
    private val deleteBtnDrawable: Drawable by lazy {
        AppCompatResources.getDrawable(this@SearchActivity, R.drawable.ic_search_delete)!!
    }
    private val searchViewModel: SearchViewModel by viewModels {
        CustomViewModelFactory(
            GlobalApplication.repoFlowRepository
        )
    }

    private val imm: InputMethodManager by lazy { getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }

    private val searchRecyclerViewAdapter: SearchRecyclerViewAdapter by lazy {
        SearchRecyclerViewAdapter().apply {
            lifecycleScope.launch {
                loadStateFlow.collectLatest { loadState ->
                    setProgressBarVisible(loadState.source.refresh is LoadState.Loading)
                    binding.rvSearchRepository.isVisible =
                        loadState.source.refresh is LoadState.NotLoading

                    val errorState = loadState.source.refresh as? LoadState.Error
                        ?: loadState.source.append as? LoadState.Error
                        ?: loadState.source.prepend as? LoadState.Error
                    errorState?.let {
                        if (it.error.message == "End of List") {
                            searchViewModel.endOfListFlag.value = true
                        }
                        Toast.makeText(
                            this@SearchActivity,
                            "${it.error.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initAppBar()
        showKeyBoard()
        initObserver()
        initRecyclerView()
        initEditText()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    private fun initObserver() {
        searchViewModel.repoList.observe(this@SearchActivity) { data ->
            lifecycleScope.launch {
                setViewVisibility(View.GONE, View.GONE)
                closeKeyBoard()
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
                        if (searchViewModel.endOfListFlag.value == true) return
                        val lastVisibleItemPosition =
                            ((recyclerView.layoutManager) as LinearLayoutManager).findLastVisibleItemPosition()
                        val itemTotalCount = recyclerView.adapter!!.itemCount - 1
                        if (itemTotalCount != -1 && lastVisibleItemPosition != -1 && lastVisibleItemPosition == itemTotalCount) {
                            setProgressBarVisible(true)
                        }
                    }
                })
            }
        }
    }

    private fun initEditText() {
        with(binding.etSearchRepository) {
            setOnTouchListener { v, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_UP) {
                    compoundDrawables[2]?.let { drawable ->
                        val rangeX =
                            (right - drawable.bounds.width() - paddingEnd..right - paddingEnd).toRange()
                        if (motionEvent.rawX.toInt() in rangeX && text.isNotEmpty()) {
                            setViewVisibility(View.GONE, View.VISIBLE)
                            lifecycleScope.launch {
                                searchRecyclerViewAdapter.submitData(PagingData.empty())
                            }
                            setText("")
                        }
                    }
                }
                v.performClick()
            }

            val editTextDisposable = textChanges().debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                    onError = {
                        Toast.makeText(this@SearchActivity, "error caused by: ${it.message.toString()}", Toast.LENGTH_SHORT).show()
                    },
                    onNext = {
                        if (it.isNotEmpty()) {
                            lifecycleScope.launch {
                                searchViewModel.getRepoPaging(text.toString()).collectLatest {
                                    searchViewModel.repoList.value = it
                                }
                            }
                        }
                    },
                    onComplete = {
                        Log.e(TAG, "initEditText: Complete")
                    }
                )
            compositeDisposable.add(editTextDisposable)

            if (!hasFocus()) {
                setCompoundDrawables(left = searchBtnDrawable, right = null)
            } else {
                setCompoundDrawables(left = null, right = deleteBtnDrawable)
            }

            setOnFocusChangeListener { _, focus ->
                if (!focus) {
                    setCompoundDrawables(left = searchBtnDrawable)
                } else {
                    setCompoundDrawables(left = null, right = deleteBtnDrawable)
                }
            }
        }
    }

    private fun setViewVisibility(rvVisibility: Int, layoutVisibility: Int) {
        with(binding) {
            layoutSearchEmptyListDescription.visibility = layoutVisibility
            rvSearchRepository.visibility = rvVisibility
        }
    }

    private fun setProgressBarVisible(isVisible: Boolean) {
        with(binding.progressBar.layoutProgressBarRoot) {
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

    private fun showKeyBoard() {
        with(binding.etSearchRepository) {
            imm.showSoftInput(this, InputMethodManager.SHOW_FORCED)
            requestFocus()
        }
    }

    private fun closeKeyBoard() {
        this.currentFocus?.let {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
            binding.etSearchRepository.clearFocus()
        }
    }

    private fun initAppBar() {
        this.title = ""
        with(binding.appBarSearch) {
            appBarTitleTv.text = resources.getString(R.string.app_bar_search)
            appBarBackBtn.setOnClickListener {
                finish()
            }
        }
    }
}
