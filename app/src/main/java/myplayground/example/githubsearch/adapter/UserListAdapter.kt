package myplayground.example.githubsearch.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import myplayground.example.githubsearch.R
import myplayground.example.githubsearch.models.User

class UserListAdapter(private val onClickListener: () -> Unit = {->}) : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {
    private val usersList: MutableList<User> = mutableListOf()


    override fun getItemCount(): Int = usersList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.user_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(usersList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newUserList: List<User>?) {
        if (newUserList != null) {
            usersList.clear()
            usersList.addAll(newUserList)
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        fun bind(user: User) {
            Glide.with(itemView).load(user.avatar_url).into(itemView.findViewById<CircleImageView>(R.id.iv_user))
            itemView.findViewById<TextView>(R.id.tv_name).text = user.login

            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            when(v.id) {
                R.id.cl_user->{
                    onClickListener()
                }
                else->{}
            }
        }
    }
}