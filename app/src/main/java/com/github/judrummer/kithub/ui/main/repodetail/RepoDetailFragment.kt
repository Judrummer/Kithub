package com.github.judrummer.kithub.ui.main.repodetail

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.github.judrummer.kithub.R
import com.github.judrummer.kithub.extension.addTo
import com.github.judrummer.kithub.extension.errorMessage
import com.github.judrummer.kithub.ui.base.BaseFragment
import com.github.judrummer.kithub.extension.parseJson
import com.github.judrummer.kithub.extension.toJson
import kotlinx.android.synthetic.main.fragment_repo_detail.*

/**
 * Created by judrummer on 4/5/2560.
 */

class RepoDetailFragment : BaseFragment() {

    companion object {
        val ARG_REPO_ID = "ARG_REPO_ID"
        fun instance(id: String) = RepoDetailFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_REPO_ID, id)
            }
        }
    }

    override val contentLayoutResourceId: Int = R.layout.fragment_repo_detail
    val repoId: String by lazy { arguments.getString(ARG_REPO_ID) }
    val viewModel: RepoDetailViewModel by lazy { ViewModelProviders.of(this).get(RepoDetailViewModel::class.java) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.state.subscribe { state ->
            with(state.repo) {
                tvRepoName.text = name
                tvRepoStar.text = starCount.toString()
            }

            llRepoDetail.visibility = if (state.fetching || state.error != null) View.GONE else View.VISIBLE
            pbRepoDetail.visibility = if (state.fetching) View.VISIBLE else View.GONE
            tvRepoDetailError.visibility = if (state.error != null) View.VISIBLE else View.GONE
            tvRepoDetailError.text = state.error?.errorMessage(context) ?: ""

        }.addTo(disposables)
        viewModel.viewIntent.repoId.onNext(repoId)
    }

}