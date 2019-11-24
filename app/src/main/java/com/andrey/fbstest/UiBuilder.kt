package com.andrey.fbstest

import com.andrey.fbstest.model.repository.RepoItem
import com.andrey.fbstest.model.ui.UiModel

class UiBuilder {
    private var uiModel:UiModel= UiModel()

    fun setRepositoryList(repositoryList: ArrayList<RepoItem>):UiBuilder{
        uiModel.repositoryList=repositoryList
        return this
    }

    fun setErrorText(errorText: String):UiBuilder{
        uiModel.errorText = errorText
        return this
    }

    fun setLoadingListState(isLoadingList: Boolean):UiBuilder{
        uiModel.isLoadingList = isLoadingList
        return this
    }

    fun setLoadingExtraListState(isLoadingExtraList: Boolean):UiBuilder{
        uiModel.isLoadingExtraList = isLoadingExtraList
        return this
    }

    fun setCurrentFragment(currentFragment: UiModel.CurrentFragment):UiBuilder{
        uiModel.currentFragment = currentFragment
        return this
    }

    fun getResult(): UiModel{
        return uiModel
    }
}