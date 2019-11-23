package com.andrey.fbstest.model.ui

import com.andrey.fbstest.model.repository.RepoItem

data class UiModel(
    var repositoryList: ArrayList<RepoItem> = ArrayList<RepoItem>(),
    var errorText: String = "",
    var isLoadingList: Boolean = false,
    var isLoadingExtraList: Boolean = false,
    var currentFragment: CurrentFragment = CurrentFragment.NONE

) {

    enum class CurrentFragment {
        LIST_FRAGMENT, PROFILE_FRAGMENT, NONE
    }

}