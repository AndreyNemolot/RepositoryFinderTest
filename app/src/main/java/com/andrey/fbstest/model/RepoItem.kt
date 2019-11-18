package com.andrey.fbstest.model

data class RepoItem(
    var name: String = "",
    var owner: RepoOwner = RepoOwner(),
    var language: String = ""
)