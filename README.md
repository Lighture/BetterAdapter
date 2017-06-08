# BetterAdapter

[![](https://jitpack.io/v/Lighture/BetterAdapter.svg)](https://jitpack.io/#Lighture/BetterAdapter)

Adapter that reduce boilerplate that is needed for writing RecyclerView adapters.
You need to specify Binder for each type you add to adapter.


## Usage

First create you list data type classes
```
data class Header(val text: String)
data class Item(val title: String, val text: String)
data class Footer(val text: String)
```

Then extend EmptyBinderHolder if you want just load layout 
```
class HeaderHolder : EmptyBinderHolder<Header>(R.layout.row_header, Header::class.java)
```

or override function bind if you want to set data to simple layout
```
class FooterHolder : EmptyBinderHolder<Footer>(R.layout.row_footer, Footer::class.java) {

    override fun bind(item: Footer) {
        (itemView as TextView).text = item.text
    }

}
```

or extend EasyBinderHolder if you want fine control over initialization and binding
```
inner class ItemHolder : EasyBinderHolder<Item>(R.layout.row_item, Item::class.java) {

    lateinit var title: TextView
    lateinit var text: TextView

    override fun init() {
        title = itemView.findViewById(R.id.title) as TextView
        text = itemView.findViewById(R.id.text) as TextView
    }

    override fun bind(item: Item) {
        title.text = item.title
        text.text = item.text
        itemView.setOnClickListener {
            onItemClicked(item)
        }
    }

}
```
if you create holder inside Fragment/Activity, create it as inner class for easy resending of click like events


finally create adapter with all Holders, add some data and set it to RecyclerView
```
val betterAdapter = BetterAdapter(HeaderHolder(), ItemHolder(), FooterHolder())
betterAdapter.data.add(Header("Header"))
(0..100).forEach {
    betterAdapter.data.add(Item("Item $it", "Ttem text"))
}
betterAdapter.data.add(Footer("Footer"))
list.adapter = betterAdapter
```