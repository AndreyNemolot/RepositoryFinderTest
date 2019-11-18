package com.andrey.fbstest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.andrey.fbstest.model.RepoOwner
import com.andrey.fbstest.viewModel.ProfileActivityViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import java.lang.Exception

class ProfileActivity : AppCompatActivity() {

    private lateinit var profileActivityViewModel: ProfileActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profileActivityViewModel =
            ViewModelProviders.of(this).get(ProfileActivityViewModel::class.java)

        try {
            intent.getParcelableExtra("profile") as RepoOwner
            profileActivityViewModel.repoOwner = intent.getParcelableExtra("profile") as RepoOwner
            setProfile()
        } catch (e: Exception) {
            setErrorProfile()
        }


    }

    private fun setProfile() {
        Picasso.get().load(profileActivityViewModel.repoOwner.avatar_url)
            .error(R.drawable.baseline_image_black_48)
            .placeholder(R.drawable.progress_animation).into(profileImage)
        ownerName.text = profileActivityViewModel.repoOwner.login
    }

    private fun setErrorProfile() {
        Picasso.get().load(R.drawable.baseline_image_black_48)
            .into(profileImage)
        ownerName.text = resources.getText(R.string.profile_download_error)
    }
}
