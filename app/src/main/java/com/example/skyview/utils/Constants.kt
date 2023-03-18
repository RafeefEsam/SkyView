package com.example.skyview.utils


object Constants {
//    const val BASE_URL = "https://api.openweathermap.org/data/2.5/weather?"
//    const val BASE_URL_FORECAST = "https://api.openweathermap.org/data/2.5/forecast?"
    //const val API_KEY = "d9abb2c1d05c5882e937cffd1ecd4923"
//

    //const val BASE_URL = "https://api.openweathermap.org/data/2.5/onecall?lat=33.44&lon=-94.04&exclude=daily&appid=bec88e8dd2446515300a492c3862a10e/"
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
   const val API_KEY = "992213628dbceb7e7fb06cf59035697d"

    const val PASSED_LOCATION_DATA = "locationData"

     const val MY_PERMISSIONS_REQUEST_LOCATION = 100
    //ExCLUDE
    const val EXCLUDE="minutely"


    //Langudes
    const val ARABIC="ar"
    const val ENGLISH ="en"

    //Units

    //Tempret
    const val FAHRENHEIT="imperial"
    const val CELSIUS="metric"
    const val KELVIN="standard"

    //Wind Speed
    const val MILESHOUR="imperial"
    const val METERSEC="metric"

   //For notification
    const val NOTIFICATION_ID = 1
    const val NOTIFICATION_CHANNEL_ID = "notification_channel_id"
    const val NOTIFICATION_CHANNEL_NAME = "Notification Channel"

    //For sharedPrefrence
    const val SHARED_LATITUDE = "latitude"
    const val SHARED_LONGTITUDE = "longtitude"
    const val SHARED_LOCATION = "location"
    const val SHARED_LANGUAGE = "language"
    const val SHARED_WIND_SPEED = "wind_speed"
    const val SHARED_TEMPERATURE = "temperature"
    const val SHARED_NOTIFICATION = "notification"




}
