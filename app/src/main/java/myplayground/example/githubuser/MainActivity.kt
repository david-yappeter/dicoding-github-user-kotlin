package myplayground.example.githubuser

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton
import myplayground.example.githubuser.adapter.UserListAdapter
import myplayground.example.githubuser.api.NetworkConfig
import myplayground.example.githubuser.api.UserListResponse
import myplayground.example.githubuser.databinding.ActivityMainBinding
import myplayground.example.githubuser.viewmodel.MainViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        with(binding) {
            rvUsers.adapter = UserListAdapter()
            rvUsers.addItemDecoration(
                DividerItemDecoration(
                    this@MainActivity,
                    DividerItemDecoration.VERTICAL
                )
            )

            displayResult()

            rvUsers.layoutManager = LinearLayoutManager(this@MainActivity)

            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.text = searchView.text
                    searchView.hide()

                    if(searchView.text. toString().trim().isNotEmpty()) {
                        loadData(searchView.text.toString())
                    } else {
                        Toast.makeText(this@MainActivity, "Search must not empty", Toast.LENGTH_LONG).show()
                    }

                    false
                }
        }
    }

    private fun loadData(search: String) {

        (binding.rvUsers.adapter as UserListAdapter).loadEmptyData(5)
        binding.rvUsers.loadSkeleton()

        NetworkConfig()
            .getService()
            .getUsers(search)
            .enqueue(object: Callback<UserListResponse> {
                override fun onFailure(call: Call<UserListResponse>, t: Throwable) {
                    binding.rvUsers.hideSkeleton()
                    Toast.makeText(this@MainActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<UserListResponse>,
                    response: Response<UserListResponse>
                ) {
                    binding.rvUsers.hideSkeleton()
                    if(response.isSuccessful) {
                        viewModel.replaceData(response.body()!!.items)
                        displayResult()

                        if(viewModel.usersList.isEmpty()) {
                            Toast.makeText(this@MainActivity, "No Data", Toast.LENGTH_LONG).show()
                        }
                    }else {
                        Toast.makeText(this@MainActivity, "Failed to load data.", Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

    private fun displayResult() {
        (binding.rvUsers.adapter as? UserListAdapter)?.apply {
            replaceData(viewModel.usersList)
            notifyDataSetChanged()
        }

    }
}