package com.andrey.fbstest

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.style.BulletSpan
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrey.fbstest.model.RepoOwner
import com.andrey.fbstest.viewModel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity(), RecyclerAdapter.OnItemClickListener {


    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        mainActivityViewModel.setRecyclerAdapter(RecyclerAdapter(this))
        errorHandleRegistration()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        recyclerView.adapter = mainActivityViewModel.adapter
        getLoadingProgress()

        registerScrollListener()

    }

    private fun getLoadingProgress() {
        mainActivityViewModel.isDataLoading.observe(this, Observer<Boolean> {
            if (it) {
                showLoadingProgress()
                showMissedItemsMessage(false)
            } else {
                hideLoadingProgress()
                hideLoadingProgressInTheEnd()
                if(mainActivityViewModel.adapter?.repoList?.size==0){
                    showMissedItemsMessage(true)
                }
            }
        })
    }

    private fun showMissedItemsMessage(show:Boolean){
        if(show){
            missingItemsView.visibility=View.VISIBLE
        }else{
            missingItemsView.visibility=View.GONE

        }
    }

    private fun registerScrollListener() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    if (isLastItemDisplaying(recyclerView)) {
                        mainActivityViewModel.getRepositoryList()
                        showLoadingProgressInTheEnd()
                    }
                }
            }
        })
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

    fun searchRepoClick(view: View) {
        mainActivityViewModel.query = getQueryFromSearchField()
        if (mainActivityViewModel.query != "") {
            mainActivityViewModel.startLoadingNewQuery()
        }
    }


    private fun getQueryFromSearchField(): String {
        return query.text.trim().toString()
    }

    override fun onItemClick(position: Int) {
        startActivityProfile(mainActivityViewModel.adapter!!.repoList[position].owner)
    }

    private fun startActivityProfile(owner: RepoOwner) {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.putExtra("profile", owner)
        startActivity(intent)
    }

    private fun showLoadingProgress() {
        progressBar.visibility = View.VISIBLE

    }

    private fun hideLoadingProgress() {
        progressBar.visibility = View.GONE
    }

    private fun showLoadingProgressInTheEnd() {
        progressBarInTheEnd.visibility = View.VISIBLE

    }

    private fun hideLoadingProgressInTheEnd() {
        progressBarInTheEnd.visibility = View.GONE
    }

    private fun errorHandleRegistration() {
        mainActivityViewModel.errorHandle.observe(this, Observer<String> {
            showError(it)
        })
    }

    private fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        hideLoadingProgress()
        hideLoadingProgressInTheEnd()
    }
}
