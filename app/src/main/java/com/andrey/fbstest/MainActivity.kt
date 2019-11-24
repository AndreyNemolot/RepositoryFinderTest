package com.andrey.fbstest

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.andrey.fbstest.model.repository.RepoOwner
import com.andrey.fbstest.viewModel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import com.andrey.fbstest.fragment.ListFragment
import com.andrey.fbstest.fragment.ProfileFragment
import com.andrey.fbstest.model.repository.RepoItem
import com.andrey.fbstest.model.ui.UiModel


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var listFragment: ListFragment
    private lateinit var profileFragment: ProfileFragment
    private lateinit var fragmentTransaction: FragmentTransaction
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_search, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu!!.findItem(R.id.searchView).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(false)
        }.setOnQueryTextListener(this)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                Toast.makeText(this, query, Toast.LENGTH_LONG).show()

            }
        }
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        listFragment = ListFragment()
        profileFragment = ProfileFragment()
        setListFragment()
        viewModel.uiLiveData.observe(this, Observer<UiModel> {
            setUi(it)
        })
    }

    private fun setUi(uiModel: UiModel) {
        resolveOrientationVisible(uiModel)
        if (uiModel.errorText != "") {
            showErrorState(uiModel.errorText)
            return
        }
        if (uiModel.isLoadingList) {
            showLoadingState()
        }
        if (uiModel.isLoadingExtraList) {
            listFragment.showLoadingProgressInTheEnd()
        }
        if (uiModel.repositoryList.size != 0) {
            showDataState(uiModel.repositoryList)
        }
    }

    private fun resolveOrientationVisible(uiModel: UiModel) {
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setPortraitFragmentVisible(uiModel)
        } else {
            setLandscapeFragmentVisible(uiModel)
        }
    }

    private fun setPortraitFragmentVisible(uiModel: UiModel) {
        when (uiModel.currentFragment) {
            UiModel.CurrentFragment.NONE -> {
                profileFragmentContainer.visibility = View.GONE
                listFragmentContainer.visibility = View.GONE
            }
            UiModel.CurrentFragment.LIST_FRAGMENT -> {
                profileFragmentContainer.visibility = View.GONE
                listFragmentContainer.visibility = View.VISIBLE
                setListFragment()
            }
            UiModel.CurrentFragment.PROFILE_FRAGMENT -> {
                profileFragmentContainer.visibility = View.GONE
                listFragmentContainer.visibility = View.VISIBLE
                setProfileFragment()
            }
        }
    }

    private fun setLandscapeFragmentVisible(uiModel: UiModel) {
        when (uiModel.currentFragment) {
            UiModel.CurrentFragment.NONE -> {
                profileFragmentContainer.visibility = View.GONE
                listFragmentContainer.visibility = View.GONE
            }
            else -> {
                profileFragmentContainer.visibility = View.VISIBLE
                listFragmentContainer.visibility = View.VISIBLE
                setListFragment()
                setProfileFragment()
            }
        }
    }

    private fun setListFragment() {
        fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.listFragmentContainer, listFragment)
        fragmentTransaction.commit()
    }

    private fun setProfileFragment() {
        fragmentTransaction = supportFragmentManager.beginTransaction()
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.listFragmentContainer, profileFragment)
        } else {
            fragmentTransaction.replace(R.id.profileFragmentContainer, profileFragment)
        }
        fragmentTransaction.commit()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query!=null && query.trim() != "") {
            hideKeyboard()
            viewModel.query =  query.trim()
            viewModel.startLoadingNewQuery()
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    fun showProfile(owner: RepoOwner) {
        viewModel.chosenOwner = owner
        if (profileFragment.isAdded) {
            profileFragment.setProfile()
        }
    }

    private fun showLoadingState() {
        progressBar.visibility=View.VISIBLE
        errorMessage.visibility = View.GONE
        listFragmentContainer.visibility=View.GONE
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            profileFragmentContainer.visibility = View.VISIBLE
        }
    }

    private fun showDataState(dataList: ArrayList<RepoItem>) {
        listFragment.setList(dataList)
        progressBar.visibility=View.GONE
        errorMessage.visibility = View.GONE
        listFragment.hideLoadingProgressInTheEnd()

    }

    private fun showErrorState(message: String) {
        errorMessage.text = message
        errorMessage.visibility = View.VISIBLE
        progressBar.visibility=View.GONE
    }


    private fun hideKeyboard() {
        val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = this.currentFocus
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
