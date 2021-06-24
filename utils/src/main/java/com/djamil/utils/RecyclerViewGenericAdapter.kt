package com.djamil.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewGenericAdapter(
    private val layout: Int,
    val onBind: (view: View, item: Any, position: Int) -> Unit,
    private val onViewClicked: (view: View, item: Any, position: Int) -> Unit,
    private var items:ArrayList<Any> = ArrayList()
) : RecyclerView.Adapter<RecyclerViewGenericAdapter.ViewHolder>() {

    fun addItem(item: Any) {
        this.items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun addItems(items: List<Any>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }
    fun setItems(items: List<Any>) {
        this.items = ArrayList(items)
    }

    fun clearItems() {
        this.items.clear()
        notifyDataSetChanged()

    }

    class ViewHolder(
        private val view: View,
        val onBind: (view: View, item: Any, position: Int) -> Unit,
        val onViewClicked: (view: View, item: Any, position: Int) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        var DURATION: Long = 400
        val on_attach = true

        fun bind(item: Any, position: Int) {
            onBind(view, item, position)
            fromLeftToRight(view, position)
            view.setOnClickListener { onViewClicked(view, item, position) }
        }


        fun fromLeftToRight(itemView: View, i: Int) {
            var i = i
            if (!on_attach) {
                i = -1
            }
            val notFirstItem = i == -1
            i += 1

            itemView.translationX = -400f
            itemView.alpha = 0f
            val animatorSet = AnimatorSet()
            val animatorTranslateY = ObjectAnimator.ofFloat(itemView, "translationX", -400f, 0f)
            val animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1f)
            ObjectAnimator.ofFloat(itemView, "alpha", 0f).start()
            animatorTranslateY.startDelay = if (notFirstItem) DURATION else i * DURATION
            animatorTranslateY.duration = (if (notFirstItem) 2 else 1) * DURATION
            animatorSet.playTogether(animatorTranslateY, animatorAlpha)
            animatorSet.start()
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {

        return ViewHolder(p0.inflate(layout), onBind, onViewClicked)
//        val view = LayoutInflater.from(p0.context).inflate(layout,p0,false)
//        return ViewHolder(view,onBind,onViewClicked)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        holder.bind(items[pos], pos)
    }



}


