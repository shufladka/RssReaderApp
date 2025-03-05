package by.bsuir.rssreaderapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import by.bsuir.rssreaderapp.R
import by.bsuir.rssreaderapp.model.Item
import com.bumptech.glide.Glide

class NewsAdapter(
    // Флаг отображения записи в списке "Читать позже"
    private val showReadLater: Boolean,
    private val onItemClick: (Item) -> Unit,

    // Обработчик для раздела "Читать позже"
    private val onReadLaterClick: (Item) -> Unit,

    // Обработчик для раздела "Главная"
    private val onRemoveFavoriteClick: (Item) -> Unit
) : ListAdapter<Item, NewsAdapter.NewsViewHolder>(NewsDiffCallback()) {

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.textTitle)
        val date: TextView = view.findViewById(R.id.txtPubdate)
        val image: ImageView = view.findViewById(R.id.imageNews)
        val btnReadLater: Button = view.findViewById(R.id.btnReadLater)
        val btnRemoveFavorite: Button = view.findViewById(R.id.btnRemoveFavorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = getItem(position)
        holder.title.text = item.title
        holder.date.text = item.pubDate

        // Загрузка изображения с использованием Glide
        if (item.thumbnail.isNotEmpty()) {
            Glide.with(holder.image.context)
                .load(item.thumbnail)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.image)
        } else {
            // Заглушка, если нет изображения
            holder.image.setImageResource(R.drawable.ic_launcher_background)
        }

        // Отображение кнопок в зависимости от статуса
        holder.btnReadLater.visibility = if (showReadLater) View.VISIBLE else View.GONE
        holder.btnRemoveFavorite.visibility = if (showReadLater) View.GONE else View.VISIBLE

        // Обработчик нажатия на карточку
        holder.itemView.setOnClickListener { onItemClick(item) }

        // Обработчик нажатия на кнопку "Читать позже"
        holder.btnReadLater.setOnClickListener { onReadLaterClick(item) }

        // Обработчик нажатия на кнопку "Удалить из избранного"
        holder.btnRemoveFavorite.setOnClickListener { onRemoveFavoriteClick(item) }
    }
}

// Оптимизируем обновление списка с DiffUtil
class NewsDiffCallback : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item) = oldItem.guid == newItem.guid
    override fun areContentsTheSame(oldItem: Item, newItem: Item) = oldItem == newItem
}
