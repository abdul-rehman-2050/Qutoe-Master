package com.fypsolutions.quotemaster

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import com.android.volley.AuthFailureError
import com.fypsolutions.quotemaster.interactions.ItemInteractor
import com.fypsolutions.quotemaster.models.Quote
import com.google.gson.Gson
import org.jetbrains.anko.toast
import org.json.JSONArray
import java.util.*
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.view.View
import com.fypsolutions.quotemaster.Items.SpacesItemDecoration


class MainActivity : AppCompatActivity(), ItemInteractor {
    override fun onCopyText(quote: Quote) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(quote.author, quote.quote)
        clipboard.primaryClip = clip
        toast("Quote Copied to Clipboard")
    }

    override fun onShareButonClick(quote: Quote) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                quote.quote)
        sendIntent.type = "text/plain"
        startActivity(sendIntent)
    }
    private val TAG = this.javaClass.simpleName
    private val API_KEY = "q6ihdw5y6smshuoV0AKYmHGhrrOep1bCf7PjsnQDGhoHuZqDVy"
    private val API_URL = "https://andruxnet-random-famous-quotes.p.mashape.com"

    var gson = Gson()

    var requestQueue:RequestQueue? = null
    var stringRequest: StringRequest?  = null

    private val quoteList: ArrayList<Quote> = ArrayList<Quote>()
    lateinit var mAdapter: QuoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        quoteList.clear()
        mAdapter = QuoteAdapter(quoteList,this)
        recView1.adapter = mAdapter
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recView1.addItemDecoration(itemDecoration)

        recView1.addItemDecoration(SpacesItemDecoration(1,16,true))


        val totalURL = String.format(API_URL+"?cat=%1\$s&count=%2\$s",
                "famous",
                10)
        // Instantiate the RequestQueue.
        val requestQueue = Volley.newRequestQueue(this)
        stringRequest = object : StringRequest(Request.Method.GET, totalURL,
                Response.Listener { response ->


                    val precount = quoteList.count()

                    val quoteJsonArray = JSONArray(response)
                    for (i in 0 until quoteJsonArray.length()) {
                        val singleObject = quoteJsonArray.getJSONObject(i)
                        val gsonparse = gson.fromJson(singleObject.toString(), Quote::class.java)
                        quoteList.add(gsonparse)


                    }

                    if(quoteJsonArray.length()>0){
                        mAdapter.notifyItemRangeChanged(precount,quoteList.count())


                    }

                    tvStatus.text="Length = ${quoteJsonArray.length()}"




                },
                Response.ErrorListener { error ->
                    tvStatus.text  = error.toString()
                    Log.d("ERROR", "error => " + error.toString())
                }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["X-Mashape-Key"] = API_KEY
                params["Accept"] = "application/json"

                return params
            }
        }



        // Add the request to the RequestQueue.
        requestQueue?.add(stringRequest)

    }

    override fun onStop() {
        super.onStop()
        requestQueue?.cancelAll(TAG)
    }



}
