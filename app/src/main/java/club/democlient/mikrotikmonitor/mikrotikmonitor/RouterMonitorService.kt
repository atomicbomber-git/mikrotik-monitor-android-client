package club.democlient.mikrotikmonitor.mikrotikmonitor

import retrofit2.Call
import retrofit2.http.*

data class Interface(
    val name: String = "",
    val type: String = "",
    val mtu: Int = -1,
    val l2mtu: Int = -1
)

class InterfaceIndex {
    val data: List<Interface> = ArrayList()
}

data class AuthData (
    val token_type: String? = null,
    val expires_in: Int? = null,
    val refresh_token: String? = null,
    val access_token: String? = null,

    val message: String? = null,
    val error: String? = null
)

interface RouterMonitorService {
    @FormUrlEncoded
    @POST("/oauth/token")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("grant_type") grantType: String = "password",
        @Field("client_id") clientId: Int = 2,
        @Field("client_secret") clientSecret: String = "Yj5u0UpSOmDkZ5QDEVuzaSf4KzT00rBndZUVSMnV"
    ): Call<AuthData>

    @GET("api/interface/{router_id}/index")
    fun interfaceIndex(@Path("router_id") routerId: Int): Call<InterfaceIndex>
}