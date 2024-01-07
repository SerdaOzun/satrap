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
    val server: Server?,
    val port: Long?
) {
    constructor(proxyId: Long, order: Long, port: Long? = 22) : this(-1L, proxyId, order, null, null, port)
}

fun GetAllJumpserversByProxyId.toJumpServer() = JumpServer(
    id = jumpserver_id,
    proxyId = proxy_id,
    order = jumpserver_order,
    user = User(jumpserver_userId ?: -1L, username ?: ""),
    server = Server(jumpsserver_serverId ?: -1L, server_url ?: "", title ?: ""),
    port = jumpserver_port
)