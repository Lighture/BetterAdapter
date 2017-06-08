package sk.lighture.betteradapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.lang.RuntimeException

class BetterAdapter(vararg val binders: Binder<*, Holder<*>>) : RecyclerView.Adapter<BetterAdapter.MyViewHolder<*, Holder<*>>>() {

    var data = mutableListOf<Any>()

    override fun onBindViewHolder(holder: MyViewHolder<*, Holder<*>>, position: Int) = holder.bind(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder<*, Holder<*>> = MyViewHolder(parent, binders[viewType] as Binder<Any, Holder<Any>>)

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int {
        val obj = data[position]
        return binders.withIndex().filter { it.value.isMyType(obj) }.map { it.index }.firstOrNull() ?: throw RuntimeException("No binder for position $position")
    }

    inner class MyViewHolder<T, out H : Holder<T>>(parent: ViewGroup, binder: Binder<T, H>) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(binder.getLayoutResId(), parent, false)) {

        private val holder: H = binder.createHolder()

        init {
            holder.initViews(itemView)
        }

        fun bind(position: Int) {
            val item = getItem(position)
            holder.bind(item)
        }

        fun getItem(position: Int): T {
            return data[position] as T
        }
    }
}

interface Binder<in T, out H : Holder<T>> {
    fun getLayoutResId(): Int
    fun createHolder(): H
    fun isMyType(obj: Any): Boolean
}

interface Holder<in T> {
    fun initViews(itemView: View)
    fun bind(item: T)
}

abstract class EasyBinderHolder<T>(private val layoutResId: Int, private val clazz: Class<T>) : Holder<T>, Binder<T, EasyBinderHolder<T>>, Cloneable {

    protected lateinit var itemView: View


    override fun initViews(itemView: View) {
        this.itemView = itemView
    }

    override fun getLayoutResId(): Int {
        return layoutResId
    }

    override fun isMyType(obj: Any): Boolean {
        return obj.javaClass.isClassInstance(clazz)
    }

    abstract override fun bind(item: T)

    override fun createHolder(): EasyBinderHolder<T> {
        return clone() as EasyBinderHolder<T>
    }

    private fun Class<*>.isClassInstance(clazz: Class<*>): Boolean {
        if (this == clazz) {
            return true
        } else if (this == Object::class.java) {
            return false
        } else {
            return superclass.isClassInstance(clazz)
        }
    }

}

open class EmptyBinderHolder<T>(layoutResId: Int, clazz: Class<T>) : EasyBinderHolder<T>(layoutResId, clazz) {

    override fun initViews(itemView: View) {
        super.initViews(itemView)
    }

    override fun bind(item: T) {}

}