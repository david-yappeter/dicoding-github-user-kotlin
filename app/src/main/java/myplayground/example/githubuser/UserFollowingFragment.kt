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
import myplayground.example.githubuser.adapter.UserDetailFollowingListAdapter
import myplayground.example.githubuser.api.NetworkConfig
import myplayground.example.githubuser.api.UserFollowingResponseItem
import myplayground.example.githubuser.databinding.FragmentUserFollowingBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserFollowingFragment : Fragment() {
    private lateinit var binding: FragmentUserFollowingBinding
    var username: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        with(binding) {
            rvFollowing.adapter = UserDetailFollowingListAdapter()
            rvFollowing.layoutManager = LinearLayoutManager(view.context)
        }

        loadData()
    }

    private fun loadData() {
        with(binding) {
            (rvFollowing.adapter as UserDetailFollowingListAdapter).loadEmptyData(3)
            rvFollowing.loadSkeleton()

            NetworkConfig()
                .getService()
                .getUserFollowing(username!!)
                .enqueue(object: Callback<List<UserFollowingResponseItem>> {
                    override fun onFailure(call: Call<List<UserFollowingResponseItem>>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                        binding.rvFollowing.hideSkeleton()
                    }

                    override fun onResponse(
                        call: Call<List<UserFollowingResponseItem>>,
                        response: Response<List<UserFollowingResponseItem>>
                    ) {
                        binding.rvFollowing.hideSkeleton()
                        if(response.isSuccessful) {
                            (binding.rvFollowing.adapter as UserDetailFollowingListAdapter)?.apply {
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