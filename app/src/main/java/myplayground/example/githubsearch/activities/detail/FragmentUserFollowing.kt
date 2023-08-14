package myplayground.example.githubsearch.activities.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import myplayground.example.githubsearch.adapter.UserFollowAdapter
import myplayground.example.githubsearch.databinding.FragmentUserFollowBinding
import myplayground.example.githubsearch.models.User
import myplayground.example.githubsearch.models.UserResponse
import myplayground.example.githubsearch.network.GithubService
import myplayground.example.githubsearch.network.NetworkConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FragmentUserFollowing : Fragment() {
    private var _binding: FragmentUserFollowBinding? = null
    private val binding get() = _binding!!

    // cannot be private, pass variable through fragment
    var username: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserFollowBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            rvFollow.adapter = UserFollowAdapter()
            rvFollow.layoutManager = LinearLayoutManager(view.context)
        }

        loadData()
    }

    private fun loadData() {
        setLoadingAnimation(true)
        with(binding) {
            NetworkConfig.create<GithubService>(NetworkConfig.GITHUB_SERVICE_BASE_URL)
                .getUserFollowing(username!!)
                .enqueue(object : Callback<List<UserResponse>> {
                    override fun onFailure(ignoredCall: Call<List<UserResponse>>, t: Throwable) {
                        setLoadingAnimation(false)
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        ignoredCall: Call<List<UserResponse>>,
                        response: Response<List<UserResponse>>
                    ) {
                        setLoadingAnimation(false)
                        if (response.isSuccessful) {
                            (rvFollow.adapter as UserFollowAdapter?)?.apply {
                                setData(response.body()!!.map { User.fromUserResponse(it) })
                            }
                        } else {
                            Toast.makeText(context, "FAILED TO DO REQUEST", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                })
        }
    }

    private fun setLoadingAnimation(v: Boolean) {
        if (v) {
            binding.rvFollow.visibility = View.GONE
            binding.loading.visibility = View.VISIBLE
        } else {
            binding.loading.visibility = View.GONE
            binding.rvFollow.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}