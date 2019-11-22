package com.andrey.fbstest.model.repository

data class RepoItem(
    var name: String = "",
    var owner: RepoOwner = RepoOwner(),
    var language: String = ""
)