package com.home.opencarshare.model

import com.home.opencarshare.model.pojo.Driver
import com.home.opencarshare.model.pojo.ServiceMessage
import com.home.opencarshare.network.Response

class CreateTripUiModel {
    var state: Response<ServiceMessage> = Response.Data(ServiceMessage())
    var driver: Driver = Driver()
}