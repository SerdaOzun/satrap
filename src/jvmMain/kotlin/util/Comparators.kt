package util

import domain.ServerComplete

fun getServerCompleteComparator(ascending: Boolean) = if (ascending) compareBy<ServerComplete> { it.server.title } else compareByDescending { it.server.title }
