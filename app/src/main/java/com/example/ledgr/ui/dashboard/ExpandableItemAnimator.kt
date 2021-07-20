package com.example.ledgr.ui.dashboard

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.example.ledgr.adapters.PendingTransactionsAdapter

class ExpandableItemAnimator : DefaultItemAnimator() {

    override fun recordPreLayoutInformation(
        state: RecyclerView.State,
        viewHolder: RecyclerView.ViewHolder,
        changeFlags: Int,
        payloads: MutableList<Any>
    ): ItemHolderInfo {
        return if (viewHolder is PendingTransactionsAdapter.ViewHolder.HeaderVH) {
            HeaderItemInfo().also {
                it.setFrom(viewHolder)
            }
        } else {
            super.recordPreLayoutInformation(state, viewHolder, changeFlags, payloads)
        }
    }

    override fun recordPostLayoutInformation(
        state: RecyclerView.State,
        viewHolder: RecyclerView.ViewHolder
    ): ItemHolderInfo {
        return if (viewHolder is PendingTransactionsAdapter.ViewHolder.HeaderVH) {
            HeaderItemInfo().also {
                it.setFrom(viewHolder)
            }
        } else {
            super.recordPostLayoutInformation(state, viewHolder)
        }
    }

    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder,
        newHolder: RecyclerView.ViewHolder,
        preInfo: ItemHolderInfo,
        postInfo: ItemHolderInfo
    ): Boolean {
        if (preInfo is HeaderItemInfo && postInfo is HeaderItemInfo && newHolder is PendingTransactionsAdapter.ViewHolder.HeaderVH) {
            ObjectAnimator
                .ofFloat(
                    newHolder.icExpand,
                    View.ROTATION,
                    preInfo.arrowRotation,
                    postInfo.arrowRotation
                )
                .also {
                    it.addListener(object: AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            newHolder.icExpand.rotation = postInfo.arrowRotation
                            dispatchAnimationFinished(newHolder)
                        }
                    })
                    it.start()
                }
        }

        return super.animateChange(oldHolder, newHolder, preInfo, postInfo)
    }

    //It means that for animation we don’t need to have separated objects of ViewHolder (old and new holder)
    override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
        return true
    }

}

class HeaderItemInfo: RecyclerView.ItemAnimator.ItemHolderInfo() {
    internal var arrowRotation: Float = 0F

    override fun setFrom(holder: RecyclerView.ViewHolder): RecyclerView.ItemAnimator.ItemHolderInfo {
        if (holder is PendingTransactionsAdapter.ViewHolder.HeaderVH) {
            arrowRotation = holder.icExpand.rotation
        }
        return super.setFrom(holder)
    }
}