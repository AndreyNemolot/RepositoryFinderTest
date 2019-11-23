package com.andrey.fbstest.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.andrey.fbstest.R
import com.andrey.fbstest.UiBuilder
import com.andrey.fbstest.model.repository.RepoItem
import com.andrey.fbstest.model.repository.RepoOwner
import com.andrey.fbstest.model.ui.UiModel
import com.andrey.fbstest.network.NetworkConnectivityManager
import com.andrey.fbstest.repository.NetworkRepository


class MainActivityViewModel(application: Application) : AndroidViewModel(application) {


    private var dataRepository = NetworkRepository()
    private var page = 1

    var repositoryList = ArrayList<RepoItem>()
    var uiLiveData = MutableLiveData<UiModel>()
    var query = ""
    var chosenOwner=RepoOwner()
    set(value){
        field=value
        val uiModel = UiBuilder()
            .setCurrentFragment(UiModel.CurrentFragment.PROFILE_FRAGMENT).getResult()
        render(uiModel)
    }

    fun startLoadingNewQuery() {
        if (isInternetAvailable()) {
            resetData()
            val uiModel = UiBuilder()
                .setCurrentFragment(UiModel.CurrentFragment.LIST_FRAGMENT)
                .setLoadingListState(true).getResult()
            render(uiModel)
            getRepositoryList()
        }else{
            val uiModel = UiBuilder()
                .setCurrentFragment(UiModel.CurrentFragment.NONE)
                .setErrorText(getApplication<Application>().resources.getString(R.string.internet_unavailable)).getResult()
            render(uiModel)
        }
    }

    private fun resetData(){
        page = 1
        repositoryList = ArrayList<RepoItem>()
        chosenOwner=RepoOwner()
    }

    fun startLoadNextPage(){
        if (isInternetAvailable()) {
            val uiModel = UiBuilder()
                .setCurrentFragment(UiModel.CurrentFragment.LIST_FRAGMENT)
                .setLoadingExtraListState(true).getResult()
            render(uiModel)
            getRepositoryList()
        }
    }

    private fun getRepositoryList() {
        dataRepository.getRepositoryList(
            query,
            page.toString(),
            object : NetworkRepository.DataReadyCallback {
                override fun dataReady(data: ArrayList<RepoItem>) {
                    saveRepos(data)
                }

            })
    }

    private fun isInternetAvailable(): Boolean =
        NetworkConnectivityManager.getConnectivityStatus(getApplication()) != 0

    private fun saveRepos(currentList: ArrayList<RepoItem>) {
        repositoryList.addAll(currentList)
        if(repositoryList.size==0){
            val uiModel = UiBuilder()
                .setCurrentFragment(UiModel.CurrentFragment.NONE)
                .setErrorText(getApplication<Application>().resources.getString(R.string.items_are_missing)).getResult()
            render(uiModel)
        }else{
            page++
            val uiModel = UiBuilder()
                .setRepositoryList(repositoryList).setCurrentFragment(UiModel.CurrentFragment.LIST_FRAGMENT).getResult()
            render(uiModel)
        }

    }

    private fun render(uiModel: UiModel) {
        uiLiveData.postValue(uiModel)
    }


}
