package com.andrey.fbstest.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.andrey.fbstest.model.RepoOwner

class ProfileActivityViewModel(application: Application) : AndroidViewModel(application) {

    var repoOwner=RepoOwner()
}