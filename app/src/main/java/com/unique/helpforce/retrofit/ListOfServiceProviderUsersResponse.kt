package com.unique.helpforce.retrofit

data class ListOfServiceProviderUsersResponse(
    var serviceRequests: List<ServiceRequests>,
    var helpRequests : List<HelpRequests>
){}