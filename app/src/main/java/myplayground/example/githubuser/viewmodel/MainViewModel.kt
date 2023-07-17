package myplayground.example.githubuser.viewmodel

import androidx.lifecycle.ViewModel
import myplayground.example.githubuser.api.ItemsItem

class MainViewModel: ViewModel() {
    var usersList: MutableList<ItemsItem> = ArrayList()

    fun replaceData(data: List<ItemsItem>) {
        usersList.clear()
        usersList.addAll(data)
    }
}