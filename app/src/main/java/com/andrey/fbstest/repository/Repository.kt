package com.andrey.fbstest.repository

interface Repository {

    fun getRepositoryList(query:String, page: String, dataReadyCallback: NetworkRepository.DataReadyCallback)
}