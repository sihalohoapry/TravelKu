package com.sihaloho.travelku.notificationbooking;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAALMjDHLA:APA91bFeP9a6PT4-Y-IfJtw7wIksCYFqhY1ZP2VDrgTmW9Caf03ERpe5Gilm6SHQkR0V21P0MgA3FwJwhv4vw_LlTMgTUIPwN8JtgJ4H4_aHXc0EXX8Gfc6BR5lsKMF2nyO__AvogqVt"

    })

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
