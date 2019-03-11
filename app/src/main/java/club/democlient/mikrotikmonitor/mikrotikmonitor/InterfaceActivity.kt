package club.democlient.mikrotikmonitor.mikrotikmonitor

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import retrofit2.Callback
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_interface.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import retrofit2.Call
import retrofit2.Response

class InterfaceActivity : AppCompatActivity() {

    private val routerMonitorService: RouterMonitorService by inject()
    private val sharedPreferences: SharedPreferences by inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interface)

        val interfaces = ArrayList<Interface>()

        interfaceList.adapter = InterfaceListAdapter(this, interfaces)
        interfaceList.layoutManager = LinearLayoutManager(this)

        routerMonitorService.interfaceIndex(1)
            .enqueue(object: Callback<InterfaceIndex> {
                override fun onResponse(call: Call<InterfaceIndex>, response: Response<InterfaceIndex>) {

                    response.body()?.let {
                        interfaces.addAll(it.data)
                        interfaceList.adapter?.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<InterfaceIndex>, t: Throwable) {
                    Toast.makeText(this@InterfaceActivity, "Error occured.", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    class InterfaceListAdapter(private val context: Context, private val dataset: List<Interface>): RecyclerView.Adapter<InterfaceListAdapter.ViewHolder>() {

        class ViewHolder(view: View): RecyclerView.ViewHolder(view)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.interface_list_item, parent, false))
        }

        override fun getItemCount(): Int {
            return this.dataset.count()
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            with (viewHolder.itemView) {
                findViewById<TextView>(R.id.textInterfaceName).text = dataset[position].name
                findViewById<TextView>(R.id.textInterfaceType).text = dataset[position].type
                findViewById<TextView>(R.id.textInterfaceMtu).text = dataset[position].mtu.toString()
                findViewById<TextView>(R.id.textInterfaceM2tu).text = dataset[position].l2mtu.toString()
            }
        }
    }
}
