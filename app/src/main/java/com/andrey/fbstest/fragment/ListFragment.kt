package com.andrey.fbstest.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andrey.fbstest.MainActivity
import com.andrey.fbstest.R
import com.andrey.fbstest.RecyclerAdapter
import com.andrey.fbstest.model.repository.RepoItem
import com.andrey.fbstest.viewModel.MainActivityViewModel
import kotlinx.android.synthetic.main.list_fragment.*


class ListFragment : Fragment(), RecyclerAdapter.OnItemClickListener {

    private lateinit var viewModel: MainActivityViewModel
    var adapter: RecyclerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(
            MainActivityViewModel::class.java)
        adapter = RecyclerAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)

        recyclerView.adapter = adapter
        setList(viewModel.repositoryList)

        registerScrollListener()


    }

    private fun registerScrollListener() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    if (isLastItemDisplaying(recyclerView)) {
                        viewModel.startLoadNextPage()
                    }
                }
            }
        })
    }


    fun setList(list: ArrayList<RepoItem>) {
        adapter?.setRepositories(list)
    }


    private fun isLastItemDisplaying(recyclerView: RecyclerView): Boolean {
        if (recyclerView.adapter!!.itemCount != 0) {
            val lastVisibleItemPosition =
                (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.adapter!!.itemCount - 1)
                return true
        }
        return false
    }

    override fun onItemClick(position: Int) {
        (activity as MainActivity).showProfile(adapter!!.repoList[position].owner)
    }


    fun showLoadingProgressInTheEnd() {
        progressBarInTheEnd.visibility = View.VISIBLE
    }

    fun hideLoadingProgressInTheEnd() {
        try {
            progressBarInTheEnd.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
