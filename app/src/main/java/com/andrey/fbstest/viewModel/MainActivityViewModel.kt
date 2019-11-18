package com.andrey.fbstest.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.andrey.fbstest.RecyclerAdapter
import com.andrey.fbstest.model.RepoItem
import com.andrey.fbstest.network.NetworkConnectivityManager
import com.andrey.fbstest.repository.NetworkRepository

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private var repository = NetworkRepository()
    private var page = 1
    var errorHandle = MutableLiveData<String>()
    var adapter: RecyclerAdapter? = null
    var isDataLoading = MutableLiveData<Boolean>()
    var query = ""


    fun setRecyclerAdapter(recyclerAdapter: RecyclerAdapter) {
        if (adapter == null) {
            adapter = recyclerAdapter
        }
    }

    fun startLoadingNewQuery() {
        if (isInternetAvailable()) {
            page = 1
            startUiLoading()
            getRepositoryList()
        }
    }

    fun getRepositoryList() {
        if (!isInternetAvailable()) {
            return
        }
        repository.getRepositoryList(
            query,
            page++.toString(),
            object : NetworkRepository.DataReadyCallback {
                override fun dataReady(data: ArrayList<RepoItem>) {
                    saveRepos(data)
                }

            })
    }

    private fun isInternetAvailable(): Boolean =
        if (NetworkConnectivityManager.getConnectivityStatus(getApplication()) != 0) {
            true
        } else {
            errorHandle.postValue("Отсутствует интернет соединение")
            false
        }

    private fun startUiLoading() {
        adapter?.clearData()
        isDataLoading.postValue(true)
    }

    private fun saveRepos(currentList: ArrayList<RepoItem>) {
        adapter?.add(currentList)
        stopUiLoading()
    }

    private fun stopUiLoading() {
        isDataLoading.postValue(false)
    }

}
