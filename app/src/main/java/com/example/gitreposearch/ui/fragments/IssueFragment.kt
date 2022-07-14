package com.example.gitreposearch.ui.fragments

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
    
    companion object{
        const val TAG = "IssueFragment"
    }

    private var binding : FragmentIssueBinding? = null
    private val mainViewModel : MainViewModel by activityViewModels()
    private lateinit var adapter: IssueListRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = FragmentIssueBinding.inflate(inflater, container, false)
        Log.e(TAG, "onCreateView: ", )
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e(TAG, "onViewCreated: ", )
        initFilterSpinner()
        initIssueRecyclerView()
        initObserve()

    }

    private fun initFilterSpinner() {
        ArrayAdapter.createFromResource(requireContext(), R.array.filter, android.R.layout.simple_spinner_item)
            .also { madapter ->
                val curItemPos = resources.getStringArray(R.array.filter).indexOf(mainViewModel.issueState.value.toString())
                with(binding!!.mainFilterSpinner){
                    adapter = madapter
                    onItemSelectedListener = this@IssueFragment
                    setSelection(curItemPos)
                }
            }
    }

    private fun initIssueRecyclerView(){
        adapter = IssueListRecyclerViewAdapter()
        with(binding!!){
            issueRecyclerView.layoutManager = LinearLayoutManager(activity)
            issueRecyclerView.adapter = adapter
        }

    }



    private fun initObserve(){
        mainViewModel.userIssueList.observe(viewLifecycleOwner){ issueList ->
            Log.e(TAG, "initObserve: userIssueList")
            adapter.setData(issueList)
        }

        mainViewModel.issueState.observe(viewLifecycleOwner){state ->
            Log.e(TAG, "initObserve: issueState")
            mainViewModel.token.value?.let { mainViewModel.getUserIssueList(it, state) }

        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        val state = parent?.getItemAtPosition(pos).toString()
        Log.e(TAG, "onItemSelected: $state", )
        //mainViewModel.setIssueState(state)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e(TAG, "onDestroyView: ", )
        binding = null
    }

}