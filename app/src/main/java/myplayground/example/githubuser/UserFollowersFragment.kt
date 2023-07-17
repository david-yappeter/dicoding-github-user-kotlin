package myplayground.example.githubuser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton
import myplayground.example.githubuser.adapter.UserDetailFollowerListAdapter
import myplayground.example.githubuser.api.NetworkConfig
import myplayground.example.githubuser.api.UserFollowersResponseItem
import myplayground.example.githubuser.databinding.FragmentUserFollowersBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserFollowersFragment : Fragment() {
    private lateinit var binding: FragmentUserFollowersBinding
    var username: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            rvFollower.adapter = UserDetailFollowerListAdapter()
            rvFollower.stopScroll()
            rvFollower.layoutManager = LinearLayoutManager(view.context)
        }

        loadData()
    }

    private fun loadData() {
        with(binding) {
            (rvFollower.adapter as UserDetailFollowerListAdapter).loadEmptyData(3)
            rvFollower.loadSkeleton()

            NetworkConfig()
                .getService()
                .getUserFollowers(username!!)
                .enqueue(object: Callback<List<UserFollowersResponseItem>> {
                    override fun onFailure(call: Call<List<UserFollowersResponseItem>>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                        binding.rvFollower.hideSkeleton()
                    }

                    override fun onResponse(
                        call: Call<List<UserFollowersResponseItem>>,
                        response: Response<List<UserFollowersResponseItem>>
                    ) {
                        binding.rvFollower.hideSkeleton()
                        if(response.isSuccessful) {
                            (binding.rvFollower.adapter as UserDetailFollowerListAdapter)?.apply {
                                replaceData(response.body()!!)
                                notifyDataSetChanged()
                            }
                        } else {
                            Toast.makeText(context, "FAILED TO DO REQUEST", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
        }
    }
}