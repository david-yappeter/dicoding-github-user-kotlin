package playground.example.dicoding_pokedex.adapter

import android.content.ClipData.Item
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import myplayground.example.githubuser.GlideApp
import myplayground.example.githubuser.R
import myplayground.example.githubuser.api.ItemsItem

class UserListAdapter() : RecyclerView.Adapter<UserListAdapter.MyHolder>() {
    private val itemLists: MutableList<ItemsItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.user_row, parent, false)
        return MyHolder(v)
    }

    override fun getItemCount(): Int = itemLists.size

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(itemLists[position])
    }

    fun addData(items: List<ItemsItem>?) {
        if(items != null) {
            itemLists.addAll(items)
        }

        notifyDataSetChanged()
    }

    fun replaceData(items: List<ItemsItem>?) {
        if(items != null) {
            itemLists.clear()
            itemLists.addAll(items)
        }

        notifyDataSetChanged()
    }

    fun loadEmptyData(count: Int) {
        val l = ArrayList<ItemsItem>();

        for(i in 1..count step 1) {
            l.add(ItemsItem("","", "", "", "david", "", "", "", "", 1.0, "", "", "", "",  false, 0, "", "", ""))
        }
        replaceData(l)

    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ItemsItem) {
            GlideApp.with(itemView).load(item.avatarUrl).into(itemView.findViewById<ImageView>(R.id.iv_user))
            itemView.findViewById<TextView>(R.id.tv_user).text = item.login
        }

    }
}