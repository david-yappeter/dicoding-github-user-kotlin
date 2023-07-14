package myplayground.example.githubuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import myplayground.example.githubuser.GlideApp
import myplayground.example.githubuser.R
import myplayground.example.githubuser.api.UserFollowersResponseItem

class UserDetailFollowerListAdapter() : RecyclerView.Adapter<UserDetailFollowerListAdapter.MyHolder>() {
    private val itemLists: MutableList<UserFollowersResponseItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.user_row, parent, false)
        return MyHolder(v)
    }

    override fun getItemCount(): Int = itemLists.size

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(itemLists[position])
    }

    fun addData(items: List<UserFollowersResponseItem>?) {
        if(items != null) {
            itemLists.addAll(items)
        }

        notifyDataSetChanged()
    }

    fun replaceData(items: List<UserFollowersResponseItem>?) {
        if(items != null) {
            itemLists.clear()
            itemLists.addAll(items)
        }

        notifyDataSetChanged()
    }

    fun loadEmptyData(count: Int) {
        val l = ArrayList<UserFollowersResponseItem>();

        for(i in 1..count step 1) {
            l.add(UserFollowersResponseItem("","", "", "", "david", "", "", "", "", "", "", "", "", false,  0, "", "", ""))
        }
        replaceData(l)
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        fun bind(item: UserFollowersResponseItem) {
            GlideApp.with(itemView).load(item.avatarUrl).into(itemView.findViewById<ImageView>(R.id.iv_user))
            itemView.findViewById<TextView>(R.id.tv_user).text = item.login

            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {

        }

    }
}