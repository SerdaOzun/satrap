package screens.proxy

import AppDatabase
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import data.SqlDelightJumpProxy
import domain.JumpServer
import domain.Proxy
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class ProxyViewModel(
    private val jumpProxyDataSource: SqlDelightJumpProxy = AppDatabase.sqlProxy
) : ViewModel() {

    //All jumpproxies
    val proxies = jumpProxyDataSource.getAll()
    //Updated with updateSelectedProxy
    var proxy by mutableStateOf<Proxy?>(null)

    fun updateSelectedProxy(proxyId: Long) {
        proxy = jumpProxyDataSource.getProxy(proxyId)
    }

    fun onEvent(proxyEvent: ProxyEvent) {
        viewModelScope.launch {
            when (proxyEvent) {
                is ProxyEvent.InsertProxy -> jumpProxyDataSource.insertProxy(proxyEvent.proxy)
                is ProxyEvent.InsertJumpserver -> jumpProxyDataSource.insertJumpserver(proxyEvent.jumpserver)
                is ProxyEvent.DeleteJumpserver -> {
                    jumpProxyDataSource.deleteJumpserver(proxyEvent.jumpserver)
                    //Rearrange order after deleting one item
                    proxies.first().first { it.id == proxyEvent.jumpserver.proxyId }.jumpserverList
                        .sortedBy { it.order }
                        .mapIndexed { index, jumpServer ->
                            jumpProxyDataSource.insertJumpserver(jumpServer.copy(order = index + 1L))
                        }
                }

                is ProxyEvent.DeleteProxy -> jumpProxyDataSource.deleteProxy(proxyEvent.proxy)
                is ProxyEvent.SortJumpserver -> {
                    //Switch order of jumpserver and the one above/below it
                    proxyEvent.run {
                        val newOrder = if (sortUpwards) jumpserver.order - 1 else jumpserver.order + 1
                        val otherJumpserver = proxies.first().first { it.id == jumpserver.proxyId }
                            .jumpserverList.firstOrNull { it.order == newOrder }
                        //Only switch if jumpserver above/below exists
                        otherJumpserver?.let { otherJs ->
                            jumpProxyDataSource.insertJumpserver(otherJs.copy(order = if (sortUpwards) otherJs.order + 1 else otherJs.order - 1))
                            jumpProxyDataSource.insertJumpserver(jumpserver.copy(order = newOrder))
                        }
                    }
                }
            }
        }
    }
}

sealed class ProxyEvent {
    data class InsertProxy(val proxy: Proxy) : ProxyEvent()
    data class InsertJumpserver(val jumpserver: JumpServer) : ProxyEvent()
    data class DeleteProxy(val proxy: Proxy) : ProxyEvent()
    data class DeleteJumpserver(val jumpserver: JumpServer) : ProxyEvent()
    data class SortJumpserver(val sortUpwards: Boolean, val jumpserver: JumpServer) : ProxyEvent()
}
