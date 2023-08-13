package myplayground.example.githubsearch.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import myplayground.example.githubsearch.R
import myplayground.example.githubsearch.models.User

class UserFollowAdapter: RecyclerView.Adapter<UserFollowAdapter.ViewHolder>() {
    private var listUserFollow: MutableList<User> = mutableListOf()

    override fun getItemCount(): Int  = listUserFollow.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listUserFollow[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.user_row, parent, false)
        return ViewHolder(v)
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setData(newUserList: List<User>?) {
        if (newUserList != null) {
            listUserFollow.clear()
            listUserFollow.addAll(newUserList)
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            Glide.with(itemView).load(user.avatar_url).into(itemView.findViewById(R.id.iv_user))
            itemView.findViewById<TextView>(R.id.tv_name).text = user.login
        }
    }
}