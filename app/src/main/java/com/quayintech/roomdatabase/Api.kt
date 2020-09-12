package simplifiedcoding.net.kotlinretrofittutorial.api


import com.quayintech.roomdatabase.model.LoginData
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @POST("api/user/create")
    fun getData(@Body jsonObject: LoginData): Call<JSONObject>


}