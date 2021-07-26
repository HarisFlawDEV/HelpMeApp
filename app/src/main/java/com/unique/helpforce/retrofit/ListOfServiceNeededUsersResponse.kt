package com.unique.helpforce.retrofit

data class ListOfServiceNeededUsersResponse(
    var serviceRequests: List<ServiceRequests>,
    var helpRequests : List<HelpRequests>
){}