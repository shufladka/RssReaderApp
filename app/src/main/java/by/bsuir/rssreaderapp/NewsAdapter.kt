package by.bsuir.rssreaderapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import by.bsuir.rssreaderapp.model.Item
import com.bumptech.glide.Glide

class NewsAdapter : ListAdapter<Item, NewsAdapter.NewsViewHolder>(NewsDiffCallback()) {

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.textTitle)
        val date: TextView = view.findViewById(R.id.txtPubdate)
        val image: ImageView = view.findViewById(R.id.imageNews)
//        val description: TextView = view.findViewById(R.id.txtDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = getItem(position)
        holder.title.text = item.title
        holder.date.text = item.pubDate
//        holder.description.text = item.description

        // Загрузка изображения с использованием Glide
        if (item.thumbnail.isNotEmpty()) {
            Glide.with(holder.image.context)
                .load(item.thumbnail)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.ic_launcher_background) // Заглушка, если нет изображения
        }
    }
}

// Оптимизируем обновление списка с DiffUtil
class NewsDiffCallback : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item) = oldItem.guid == newItem.guid
    override fun areContentsTheSame(oldItem: Item, newItem: Item) = oldItem == newItem
}
