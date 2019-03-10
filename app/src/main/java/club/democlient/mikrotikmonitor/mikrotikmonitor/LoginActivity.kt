package club.democlient.mikrotikmonitor.mikrotikmonitor

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.ext.android.inject
import org.koin.android.ext.android.startKoin
import org.koin.core.parameter.parametersOf
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private val routerMonitorService: RouterMonitorService by inject()
    private val sharedPreferences: SharedPreferences by inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        startKoin(this, listOf(dependencyModule))

        if (sharedPreferences.getString("access_token", null) !== null) {
            gotoNextActivity()
            return
        }

        buttonLogin.setOnClickListener {

            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()

            if (username.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Nama pengguna dan kata sandi wajib diisi.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            this.routerMonitorService.login(username, password)
                .enqueue(object: Callback<AuthData> {
                    override fun onResponse(call: Call<AuthData>, response: Response<AuthData>) {
                        response.body()?.let {

                            if (response.isSuccessful) {
                                persistAuthData(it)
                                return@let
                            }

                            Toast.makeText(this@LoginActivity, it.message, Toast.LENGTH_SHORT).show()
                        }

                    }
                    override fun onFailure(call: Call<AuthData>, t: Throwable) {
                        Toast.makeText(this@LoginActivity, "Terjadi gangguan pada jaringan.", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    private fun gotoNextActivity() {
        with(Intent(this, InterfaceActivity::class.java)) {
            startActivity(this)
        }

        finish()
    }

    private fun persistAuthData(authData: AuthData) {
        with (sharedPreferences.edit()) {
            putString("access_token", authData.access_token)
            putString("refresh_token", authData.refresh_token)
            apply()
        }

        gotoNextActivity()
    }
}
