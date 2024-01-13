package domain

import satrapinsatrap.GetAllJumpserversByProxyId

data class Proxy(
    val id: Long,
    val title: String,
    val jumpserverList: List<JumpServer>
) {
    constructor(title: String) : this(-1L, title, emptyList())

    override fun toString(): String {
        return title
    }
}

data class JumpServer(
    val id: Long,
    val proxyId: Long,
    val order: Long,
    val user: User?,
    val server: Server?
) {
    constructor(proxyId: Long, order: Long) : this(-1L, proxyId, order, null, null)
}

fun GetAllJumpserversByProxyId.toJumpServer() = JumpServer(
    id = jumpserver_id,
    proxyId = proxy_id,
    order = jumpserver_order,
    user = User(jumpserver_userId ?: -1L, username ?: ""),
    server = Server(jumpsserver_serverId ?: -1L, server_url ?: "", title ?: "", port ?: 22L)
)