package com.example.gitreposearch.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitreposearch.GlobalApplication
import com.example.gitreposearch.R
import com.example.gitreposearch.databinding.FragmentIssueBinding
import com.example.gitreposearch.domain.model.Issue
import com.example.gitreposearch.presentation.adapter.IssueListRecyclerViewAdapter
import com.example.gitreposearch.presentation.adapter.SpinnerCustomAdapter
import com.example.gitreposearch.presentation.viewmodel.MainViewModel

class IssueFragment : Fragment(), AdapterView.OnItemSelectedListener {
    val TAG = "IssueFragment"
    private var binding: FragmentIssueBinding? = null
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var issueRecyclerViewAdapter: IssueListRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: ")
        binding = FragmentIssueBinding.inflate(inflater, container, false)

        return binding?.root
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")
        binding?.lifecycleOwner = viewLifecycleOwner

        showLoading()
        initFilterSpinner()
        initRefreshListener()
        initIssueRecyclerView()
        initObserve()
    }

    private fun initRefreshListener() {
        binding!!.layoutRefresh.setOnRefreshListener {
            getUserIssueList()
        }
    }

    private fun initFilterSpinner() {
        val spinnerAdapter = SpinnerCustomAdapter(requireContext(), mainViewModel)
        with(binding!!.spinnerIssueFilter) {
            val curItemPos = resources.getStringArray(R.array.filter)
                .indexOf(mainViewModel.issueState.value.toString())
            adapter = spinnerAdapter
            setSelection(curItemPos)
            onItemSelectedListener = this@IssueFragment
            dropDownVerticalOffset = 120
        }
    }


    private fun initIssueRecyclerView() {
        issueRecyclerViewAdapter = IssueListRecyclerViewAdapter()
        with(binding!!) {
            rcvIssueList.layoutManager = LinearLayoutManager(activity)
            rcvIssueList.adapter = issueRecyclerViewAdapter
        }

    }

    private fun initObserve() {
        with(mainViewModel) {
            userIssueList.observe(viewLifecycleOwner) { issueList ->
                hideLoading()
                Log.d(TAG, "user issue list changed")
                if (binding!!.layoutRefresh.isRefreshing) {
                    binding!!.layoutRefresh.isRefreshing = false
                }
                issueRecyclerViewAdapter.setData(issueList as MutableList<Issue>)
            }
        }
    }

    private fun getUserIssueList() {
        mainViewModel.getUserIssueList(GlobalApplication.getInstance().getTypedAccessToken()!!)
    }

    private fun showLoading() {
        with(binding!!) {
            progressBar.layoutProgressBarRoot.setBackgroundResource(0)
            progressBar.layoutProgressBarRoot.isGone = false
            progressBar.layoutProgressBarRoot.bringToFront()
        }
    }

    private fun hideLoading() {
        with(binding!!) {
            progressBar.layoutProgressBarRoot.isGone = true
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        Log.d(TAG, "onItemSelected: ")
        val state = parent?.getItemAtPosition(pos).toString()
        val prevState = mainViewModel.issueState.value
        if (prevState != state) { // Open 상태에서 또 Open 눌렀을 때, 호출하지않도록 if문 처리
            mainViewModel.setIssueState(state)
            showLoading()
            getUserIssueList()
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {


    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: ")
        binding = null
    }

}