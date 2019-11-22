package com.andrey.fbstest.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.andrey.fbstest.R
import com.andrey.fbstest.viewModel.MainActivityViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.profile_fragment.*

class ProfileFragment : Fragment() {

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(
            MainActivityViewModel::class.java)

        setProfile()
    }

    fun setProfile() {
        Picasso.get().load(viewModel.chosenOwner.avatar_url)
            .error(R.drawable.baseline_image_black_48)
            .placeholder(R.drawable.progress_animation).into(profileImage)
        ownerName.text = viewModel.chosenOwner.login
    }

    private fun setErrorProfile() {
        Picasso.get().load(R.drawable.baseline_image_black_48)
            .into(profileImage)
        ownerName.text = resources.getText(R.string.profile_download_error)
    }

}
