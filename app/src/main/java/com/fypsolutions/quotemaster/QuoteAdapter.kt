package com.fypsolutions.quotemaster

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.fypsolutions.quotemaster.interactions.ItemInteractor
import com.fypsolutions.quotemaster.models.Quote

class QuoteAdapter(private val itemList:ArrayList<Quote>, private val interactorIntrface:ItemInteractor?): RecyclerView.Adapter<QuoteAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.mTitleTView.text = itemList[position].quote
        holder.mAuthor.text = itemList[position].author
        holder.mBookMarkButton.setOnClickListener {
            interactorIntrface?.onCopyText(itemList[position])
        }

        holder.mShareButton.setOnClickListener {
            interactorIntrface?.onShareButonClick(itemList[position])
        }
    }


    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mTitleTView: TextView = mView.findViewById<View>(R.id.quote_text) as TextView
        val mAuthor: TextView = mView.findViewById<View>(R.id.tvAuthor) as TextView
        val mBookMarkButton:LinearLayout = mView.findViewById<View>(R.id.markIntentButton) as LinearLayout
        val mShareButton:LinearLayout=mView.findViewById<View>(R.id.shareIntentButton) as LinearLayout

        override fun toString(): String {
            return super.toString() + " '" + mTitleTView.text + "'"+mAuthor.text+";"
        }
    }
}