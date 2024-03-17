package com.example.demoproject.Activity


import android.app.ActivityManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demoproject.Adapter.DataAdapter
import com.example.demoproject.Model.DataModel
import com.example.demoproject.Model.Datum
import com.example.demoproject.R
import com.example.demoproject.Retrofit.APIClient
import com.example.demoproject.Retrofit.Constants
import com.example.demoproject.Retrofit.WebServices
import com.example.demoproject.Services.MyService
import com.example.demoproject.Viewmodel.DataViewModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Objects


class MainActivity : AppCompatActivity() {
    var context: Context? = null
    var progressbar: ProgressBar? = null
    var recyclerview: RecyclerView? = null
    var tvnodata: TextView? = null
    var dataAdapter: DataAdapter? = null

    var datalist: List<Datum> = ArrayList<Datum>()
    private var viewModel: DataViewModel? = null
    var pageNO: Int? = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this
        progressbar = findViewById(R.id.progressbar)
        recyclerview = findViewById(R.id.recyclerview)
        tvnodata = findViewById(R.id.tvnodata)

        viewModel = ViewModelProvider(this)[DataViewModel::class.java]
        if (!isMyServiceRunning(this, MyService::class.java)) {
            val myServiceIntent = Intent(this, MyService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ContextCompat.startForegroundService(this, myServiceIntent)
            } else {
                startService(myServiceIntent)
            }
        }

        recyclerview!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                val visibleItemCount = Objects.requireNonNull(layoutManager)!!.childCount
                val totalItemCount = layoutManager!!.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                        && firstVisibleItemPosition >= 0
                    ) {
                        pageNO = pageNO!! + 1
                        if (isOnline(context!!)) {
                            callAPI(pageNO!!)
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                resources.getString(R.string.msg_internet_connection),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        })


    }
    fun isOnline(context: Context): Boolean {
        val cm = context
            .getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkinfo = cm.activeNetworkInfo
        return if (networkinfo != null && networkinfo.isConnected) {
            true
        } else false
    }
    fun isMyServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        androidx.appcompat.app.AlertDialog.Builder(context!!)
            .setTitle(resources.getString(R.string.exit))
            .setMessage(resources.getString(R.string.str_do_u_wanna_exit)).setCancelable(false)
            .setPositiveButton(
                resources.getString(R.string.yes)
            ) { dialog: DialogInterface?, which: Int ->
                finishAffinity()
                System.exit(0)
            }.setNegativeButton(
                getResources().getString(R.string.no)
            ) { dialog: DialogInterface?, which: Int -> }.show()
    }


    override fun onResume() {
        super.onResume()

    }

    override fun onDestroy() {
        super.onDestroy()

        if (isMyServiceRunning(this, MyService::class.java)) {
            stopService(Intent(this@MainActivity, MyService::class.java))
        }
    }

    private fun callAPI(pageNO: Int) {
        progressbar!!.setVisibility(View.VISIBLE)
        val url = Constants.BASE_URL
        Log.e("LOG_TAG", "url ------->" + url)
        val webServices: WebServices =
            APIClient.getClient(url)!!.create(WebServices::class.java)

        val call: Call<DataModel?>? = webServices.getData(pageNO!!)

        call!!.enqueue(object : Callback<DataModel?> {
            override fun onResponse(
                call: Call<DataModel?>,
                response: Response<DataModel?>
            ) {
                progressbar!!.setVisibility(View.GONE)
                val model: DataModel? = response.body()
                if (model != null) {
                    Log.e("LOG_TAG", "model ------->" + model.toString())
                    if (model.data != null) {
                        val delete = Completable.fromAction {
                            viewModel!!.deleteData(                            )
                        }

                        val insert: Completable = Completable.fromAction {
                            viewModel!!.insertData(model.data)
                        }
                        delete
                            .andThen(Completable.fromAction {
                                Log.e(
                                    "LOG_TAG",
                                    "Delete finished"
                                )
                            })
                            .andThen(insert)
                            .andThen(Completable.fromAction {
                                Log.e(
                                    "LOG_TAG",
                                    "Insert  finished "
                                )
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe()

                        setRecyclerData()
//                        loadData(model.data!!)


                    } else {
                        Toast.makeText(context, R.string.plz_try_again, Toast.LENGTH_SHORT).show()
                        Log.e("LOG_TAG", "No Records Found  List ------->")
                    }

                } else {
                    Toast.makeText(context!!, R.string.plz_try_again, Toast.LENGTH_LONG).show()
                }
            }


            override fun onFailure(call: Call<DataModel?>, t: Throwable) {
                progressbar!!.setVisibility(View.GONE)
                Toast.makeText(context, R.string.plz_try_again, Toast.LENGTH_SHORT).show()
                Log.e("LOG_TAG", "call----->$call")
            }
        })
    }

    private fun setRecyclerData() {
        val listLiveData: LiveData<List<Datum?>?>? = viewModel!!.getDataList()

        listLiveData!!.observe(this) { data ->
            listLiveData!!.removeObservers(this)
            Log.e("LOG_TAG", "list--------->" + data.toString())
            if (data != null && data.size > 0) {
               setAdapter(data!!)
            } else {
                Toast.makeText(context, R.string.str_data_not_avail, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData(data: List<Datum>) {
        dataAdapter!!.addAll(data)
    }

    private fun setAdapter(datalist: List<Datum?>) {
        if (datalist != null && datalist.size > 0) {
            tvnodata!!.setVisibility(View.GONE)
            recyclerview!!.setVisibility(View.VISIBLE)
            recyclerview!!.setHasFixedSize(true)
            recyclerview!!.getRecycledViewPool().setMaxRecycledViews(0, 0)
            dataAdapter = DataAdapter(this@MainActivity)
            recyclerview!!.setAdapter(dataAdapter)
            recyclerview!!.setLayoutManager(LinearLayoutManager(this@MainActivity))
        } else {
            tvnodata!!.setVisibility(View.VISIBLE)
            recyclerview!!.setVisibility(View.GONE)
        }
    }


}

