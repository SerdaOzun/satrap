package util

import domain.ServerComplete

fun getServerCompleteComparator(ascending: Boolean) =
    if (ascending) compareBy<ServerComplete> { it.server.title.lowercase() }
    else compareByDescending { it.server.title.lowercase() }
