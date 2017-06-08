package sk.lighture.betteradapter.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import sk.lighture.betteradapter.BetterAdapter
import sk.lighture.betteradapter.EmptyBinderHolder

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        list.layoutManager = LinearLayoutManager(this)
        val betterAdapter = BetterAdapter(HeaderHolder(), ItemHolder(), FooterHolder())
        betterAdapter.data.add(Header("Header"))
        (0..10).forEach {
            betterAdapter.data.add(Item("Item $it", "Ttem text"))
        }
        betterAdapter.data.add(Footer("Footer"))
        list.adapter = betterAdapter
    }

    fun onItemClicked(item: Item) {
        Toast.makeText(this, "Clicked: $item", Toast.LENGTH_SHORT).show()
    }

    class HeaderHolder : EmptyBinderHolder<Header>(R.layout.row_header, Header::class.java) {

        override fun bind(item: Header) {
            (itemView as TextView).text = item.text
        }

    }

    inner class ItemHolder : EmptyBinderHolder<Item>(R.layout.row_item, Item::class.java) {

        override fun bind(item: Item) {
            (itemView as TextView).text = "${item.title} ${item.text}"
            itemView.setOnClickListener {
                onItemClicked(item)
            }
        }

    }

    class FooterHolder : EmptyBinderHolder<Footer>(R.layout.row_footer, Footer::class.java) {

        override fun bind(item: Footer) {
            (itemView as TextView).text = item.text
        }

    }
}

data class Header(val text: String)
data class Item(val title: String, val text: String)
data class Footer(val text: String)