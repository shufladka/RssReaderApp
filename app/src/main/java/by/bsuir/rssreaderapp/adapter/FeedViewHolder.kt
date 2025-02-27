package by.bsuir.rssreaderapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import by.bsuir.rssreaderapp.R
import by.bsuir.rssreaderapp.interfaces.ItemClickListener
import by.bsuir.rssreaderapp.model.RSSObject
import by.bsuir.rssreaderapp.ui.pages.OneNewsActivity
import com.bumptech.glide.Glide

class FeedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {

    var txtTitle: TextView = itemView.findViewById(R.id.textTitle)
    var txtPubDate: TextView = itemView.findViewById(R.id.txtPubdate)
    var srcImage: ImageView = itemView.findViewById(R.id.imageNews)
    private var itemClickListener: ItemClickListener? = null

    init {
        itemView.setOnClickListener(this)
        itemView.setOnLongClickListener(this) }
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    override fun onClick(v: View?) {
        itemClickListener!!.onClick(v, adapterPosition, false)
    }

    override fun onLongClick(v: View?): Boolean {
        itemClickListener!!.onClick(v, adapterPosition, true)
        return true
    }
}

class FeedAdapter(private val rssObject: RSSObject, private val mContext: Context): RecyclerView.Adapter<FeedViewHolder>() {
    private val inflater = LayoutInflater.from(mContext)

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.txtTitle.text = rssObject.items[position].title
        holder.txtPubDate.text = rssObject.items[position].pubDate
        Glide.with(mContext).load(rssObject.items[position].thumbnail).into(holder.srcImage)
        holder.setItemClickListener(ItemClickListener { _, pos, isLongClick ->
            if (!isLongClick) {
                val intent = Intent(mContext, OneNewsActivity::class.java)
                intent.putExtra("TITLE", rssObject.items[pos].title)
                intent.putExtra("DATE", rssObject.items[pos].pubDate)
                intent.putExtra("CONTENT", rssObject.items[pos].content)
                intent.putExtra("AUTHOR", rssObject.items[pos].author)
                intent.putExtra("LINK", rssObject.items[pos].link)
                intent.putExtra("IMAGE", rssObject.items[pos].enclosure.link)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                mContext.startActivity(intent)

            }
        })
    }

    override fun getItemCount(): Int = rssObject.items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val itemView = inflater.inflate(R.layout.row, parent, false)
        return FeedViewHolder(itemView)
    }
}
