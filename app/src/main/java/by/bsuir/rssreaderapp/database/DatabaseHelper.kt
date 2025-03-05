package by.bsuir.rssreaderapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import by.bsuir.rssreaderapp.model.Enclosure
import by.bsuir.rssreaderapp.model.Item

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, SCHEMA) {

    companion object {
        private const val DATABASE_NAME = "rssreaderapp.db"
        private const val SCHEMA = 1

        private const val TABLE_NEWS = "news"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_PUB_DATE = "pub_date"
        private const val COLUMN_LINK = "link"
        private const val COLUMN_GUID = "guid"
        private const val COLUMN_AUTHOR = "author"
        private const val COLUMN_THUMBNAIL = "thumbnail"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_ENCLOSURE_LINK = "enclosure_link"
        private const val COLUMN_ENCLOSURE_TYPE = "enclosure_type"
        private const val COLUMN_ENCLOSURE_LENGTH = "enclosure_length"
        private const val COLUMN_CATEGORIES = "categories"
    }

    // Создание таблицы для хранения записей в списке "Читать позже"
    override fun onCreate(db: SQLiteDatabase?) {
        val createNewsTable = """
            CREATE TABLE $TABLE_NEWS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT,
                $COLUMN_PUB_DATE TEXT,
                $COLUMN_LINK TEXT,
                $COLUMN_GUID TEXT,
                $COLUMN_AUTHOR TEXT,
                $COLUMN_THUMBNAIL TEXT,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_CONTENT TEXT,
                $COLUMN_ENCLOSURE_LINK TEXT,
                $COLUMN_ENCLOSURE_TYPE TEXT,
                $COLUMN_ENCLOSURE_LENGTH INTEGER,
                $COLUMN_CATEGORIES TEXT)
        """.trimIndent()

        db?.execSQL(createNewsTable)
    }

    // Пересоздание таблицы новостей
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NEWS")
        onCreate(db)
    }

    // Сохранение записи
    fun insertItem(item: Item): Long {
        val db = writableDatabase
        db.beginTransaction()
        var id: Long = -1

        try {
            val values = ContentValues().apply {
                put(COLUMN_TITLE, item.title)
                put(COLUMN_PUB_DATE, item.pubDate)
                put(COLUMN_LINK, item.link)
                put(COLUMN_GUID, item.guid)
                put(COLUMN_AUTHOR, item.author)
                put(COLUMN_THUMBNAIL, item.thumbnail)
                put(COLUMN_DESCRIPTION, item.description)
                put(COLUMN_CONTENT, item.content)
                put(COLUMN_ENCLOSURE_LINK, item.enclosure.link)
                put(COLUMN_ENCLOSURE_TYPE, item.enclosure.type)
                put(COLUMN_ENCLOSURE_LENGTH, item.enclosure.length)
                put(COLUMN_CATEGORIES, item.categories.joinToString(","))
            }

            id = db.insert(TABLE_NEWS, null, values)

            if (id != -1L) {
                db.setTransactionSuccessful()
            }
        } catch (e: Exception) {
            Log.e("DatabaseError", "Error inserting item: ${e.message}")
        } finally {
            db.endTransaction()
            db.close()
        }

        if (id == -1L) {
            Log.e("DatabaseError", "Error inserting item: ${item.title}")
        } else {
            Log.i("DatabaseSuccess", "Item inserted with id: $id")
        }

        return id
    }

    // Получение всех записей из списка "Читать позже"
    fun getAllItems(): List<Item> {
        val items = mutableListOf<Item>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NEWS", null)

        cursor.use { it ->
            while (it.moveToNext()) {
                try {
                    val title = it.getString(it.getColumnIndexOrThrow(COLUMN_TITLE))
                    val pubDate = it.getString(it.getColumnIndexOrThrow(COLUMN_PUB_DATE))
                    val link = it.getString(it.getColumnIndexOrThrow(COLUMN_LINK))
                    val guid = it.getString(it.getColumnIndexOrThrow(COLUMN_GUID))
                    val author = it.getString(it.getColumnIndexOrThrow(COLUMN_AUTHOR))
                    val thumbnail = it.getString(it.getColumnIndexOrThrow(COLUMN_THUMBNAIL))
                    val description = it.getString(it.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                    val content = it.getString(it.getColumnIndexOrThrow(COLUMN_CONTENT))
                    val enclosureLink = it.getString(it.getColumnIndexOrThrow(COLUMN_ENCLOSURE_LINK)) ?: "empty_link"
                    val enclosureType = it.getString(it.getColumnIndexOrThrow(COLUMN_ENCLOSURE_TYPE)) ?: "empty_type"
                    val enclosureLength = it.getInt(it.getColumnIndexOrThrow(COLUMN_ENCLOSURE_LENGTH)).takeIf { it > 0 } ?: 0

                    val categories = it.getString(it.getColumnIndexOrThrow(COLUMN_CATEGORIES)).split(",").toList()

                    val enclosure = Enclosure(enclosureLink, enclosureType, enclosureLength)

                    val id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID))

                    val item = Item(title, pubDate, link, guid, author, thumbnail, description, content, enclosure, categories, id)
                    items.add(item)
                } catch (e: Exception) {
                    Log.e("DatabaseError", "Error processing row: ${e.message}")
                }
            }
        }
        db.close()
        return items
    }

    // Получение записи по идентификатору
    fun getItemById(id: Int): Item? {
        val db = readableDatabase
        var item: Item? = null
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NEWS WHERE $COLUMN_ID = ?", arrayOf(id.toString()))

        cursor.use {
            if (it.moveToFirst()) {
                try {
                    val title = it.getString(it.getColumnIndexOrThrow(COLUMN_TITLE))
                    val pubDate = it.getString(it.getColumnIndexOrThrow(COLUMN_PUB_DATE))
                    val link = it.getString(it.getColumnIndexOrThrow(COLUMN_LINK))
                    val guid = it.getString(it.getColumnIndexOrThrow(COLUMN_GUID))
                    val author = it.getString(it.getColumnIndexOrThrow(COLUMN_AUTHOR))
                    val thumbnail = it.getString(it.getColumnIndexOrThrow(COLUMN_THUMBNAIL))
                    val description = it.getString(it.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                    val content = it.getString(it.getColumnIndexOrThrow(COLUMN_CONTENT))
                    val enclosureLink = it.getString(it.getColumnIndexOrThrow(COLUMN_ENCLOSURE_LINK)) ?: "empty_link"
                    val enclosureType = it.getString(it.getColumnIndexOrThrow(COLUMN_ENCLOSURE_TYPE)) ?: "empty_type"
                    val enclosureLength = it.getInt(it.getColumnIndexOrThrow(COLUMN_ENCLOSURE_LENGTH)).takeIf { it > 0 } ?: 0

                    val categories = it.getString(it.getColumnIndexOrThrow(COLUMN_CATEGORIES)).split(",").toList()
                    val enclosure = Enclosure(enclosureLink, enclosureType, enclosureLength)
                    val itemId = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID))

                    item = Item(title, pubDate, link, guid, author, thumbnail, description, content, enclosure, categories, itemId)
                } catch (e: Exception) {
                    Log.e("DatabaseError", "Error processing row: ${e.message}")
                }
            }
        }

        db.close()
        return item
    }

    // Обновление записи
    fun updateItem(item: Item): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, item.title)
            put(COLUMN_PUB_DATE, item.pubDate)
            put(COLUMN_LINK, item.link)
            put(COLUMN_GUID, item.guid)
            put(COLUMN_AUTHOR, item.author)
            put(COLUMN_THUMBNAIL, item.thumbnail)
            put(COLUMN_DESCRIPTION, item.description)
            put(COLUMN_CONTENT, item.content)
            put(COLUMN_ENCLOSURE_LINK, item.enclosure.link)
            put(COLUMN_ENCLOSURE_TYPE, item.enclosure.type)
            put(COLUMN_ENCLOSURE_LENGTH, item.enclosure.length)
            put(COLUMN_CATEGORIES, item.categories.joinToString(","))
        }
        val affectedRows = db.update(TABLE_NEWS, values, "$COLUMN_ID = ?", arrayOf(item.id.toString()))
        db.close()
        return affectedRows
    }

    // Удаление записи
    fun deleteItem(item: Item) {
        val db = writableDatabase
        db.delete(TABLE_NEWS, "$COLUMN_ID = ?", arrayOf(item.id.toString()))
        db.close()
    }
}

