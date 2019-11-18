package com.andrey.fbstest.repository

import com.andrey.fbstest.model.RepoItem
import com.andrey.fbstest.model.RepoItems
import com.andrey.fbstest.network.Controller
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NetworkRepository:Repository {


    override fun getRepositoryList(query:String, page:String, dataReadyCallback: DataReadyCallback){
        Controller.getRepositoryList(query, page)
            .enqueue(object :Callback<RepoItems>{
                override fun onFailure(call: Call<RepoItems>?, t: Throwable?) {
                    failureHandler(dataReadyCallback)
                }

                override fun onResponse(call: Call<RepoItems>?, response: Response<RepoItems>?) {
                    if (response!=null){
                        responseHandler(response, dataReadyCallback)

                    }
                }
            })
    }

    private fun failureHandler( dataReadyCallback: DataReadyCallback){
        dataReadyCallback.dataReady(ArrayList())
    }

    private fun responseHandler(response: Response<RepoItems>?, dataReadyCallback: DataReadyCallback){
        if (response?.body() != null){
            dataReadyCallback.dataReady(response.body().items)
        }else{
            dataReadyCallback.dataReady(ArrayList())
        }
    }

    interface DataReadyCallback{
        fun dataReady(data: ArrayList<RepoItem>)
    }
}