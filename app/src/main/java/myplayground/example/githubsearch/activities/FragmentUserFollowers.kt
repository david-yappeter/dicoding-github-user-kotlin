package myplayground.example.githubsearch.activities

import android.net.DnsResolver
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
import myplayground.example.githubsearch.models.UserListResponse
import myplayground.example.githubsearch.models.UserResponse
import myplayground.example.githubsearch.network.GithubService
import myplayground.example.githubsearch.network.NetworkConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FragmentUserFollowers : Fragment() {
    private var _binding: FragmentUserFollowBinding? = null
    private val binding get() = _binding!!
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
        with(binding) {

            NetworkConfig.Create<GithubService>(NetworkConfig.GITHUB_SERVICE_BASE_URL)
                .getUserFollower(username!!)
                .enqueue(object : Callback<List<UserResponse>> {
                    override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<List<UserResponse>>,
                        response: Response<List<UserResponse>>
                    ) {
                        if (response.isSuccessful) {
                            (binding.rvFollow.adapter as UserFollowAdapter?)?.apply {
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}