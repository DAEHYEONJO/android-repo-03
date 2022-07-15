package com.example.gitreposearch.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitreposearch.R
import com.example.gitreposearch.adapter.IssueListRecyclerViewAdapter
import com.example.gitreposearch.databinding.FragmentIssueBinding
import com.example.gitreposearch.viewmodel.MainViewModel

class IssueFragment : Fragment(), AdapterView.OnItemSelectedListener {
    val TAG = "jiwoo"
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
        initFilterSpinner()
        initIssueRecyclerView()
        initObserve()
    }

    private fun initFilterSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.filter,
            android.R.layout.simple_spinner_item
        )
            .also { madapter ->
                val curItemPos = resources.getStringArray(R.array.filter)
                    .indexOf(mainViewModel.issueState.value.toString())
                with(binding!!.mainFilterSpinner) {
                    adapter = madapter
                    onItemSelectedListener = this@IssueFragment
                    Log.d(TAG, "initFilterSpinner: itemSelected ")
                    setSelection(curItemPos)
                }
            }
    }

    private fun initIssueRecyclerView() {
        issueRecyclerViewAdapter = IssueListRecyclerViewAdapter()
        with(binding!!) {
            issueRecyclerView.layoutManager = LinearLayoutManager(activity)
            issueRecyclerView.adapter = issueRecyclerViewAdapter
        }

    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
    }

    private fun initObserve() {
        with(mainViewModel) {
            userIssueList.observe(viewLifecycleOwner) { issueList ->
                Log.d(TAG, "userIssueList Observe: ")
                issueRecyclerViewAdapter.setData(issueList)
            }

            issueState.observe(viewLifecycleOwner) {
                Log.d(TAG, "issueState Observe: ")
                token.value?.let { token -> getUserIssueList(token) }
            }


        }
    }





    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        val state = parent?.getItemAtPosition(pos).toString()
        with(mainViewModel){
            if(issueState.value != state){
                setIssueState(state)
                token.value?.let { token -> mainViewModel.getUserIssueList(token)}
            }
        }
        Log.d(TAG, "itemSelected ")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: ")
        binding = null
    }

}