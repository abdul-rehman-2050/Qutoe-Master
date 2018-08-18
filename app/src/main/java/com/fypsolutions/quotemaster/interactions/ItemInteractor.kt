package com.fypsolutions.quotemaster.interactions

import com.fypsolutions.quotemaster.models.Quote


interface  ItemInteractor{
    fun onShareButonClick(quote: Quote)
    fun onCopyText(quote: Quote)
}