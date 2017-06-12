package com.github.judrummer.kithub.ui.main.repodetail

import android.os.Bundle
import android.view.View
import com.github.judrummer.kithub.R
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
        fun instance(id:String) = RepoDetailFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_REPO_ID, id)
            }
        }
    }

    override val contentLayoutResourceId: Int = R.layout.fragment_repo_detail

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val repo = arguments.getString(ARG_REPO_ID).parseJson<RepoListContract.RepoItem>()
//        tvRepoName.text = repo.name
//        tvRepoStar.text = repo.starCount.toString()
    }

}