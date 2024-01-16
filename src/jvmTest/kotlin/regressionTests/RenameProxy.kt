package regressionTests

import DatabaseTestCase
import domain.JumpServer
import domain.Proxy
import io.kotest.assertions.withClue
import io.kotest.inspectors.forOne
import io.kotest.matchers.shouldBe
import org.testng.annotations.Test

class RenameProxy : DatabaseTestCase() {

    @Test
    fun `Renaming Proxy should not delete Jumpservers`() {
        var proxyId: Long = -1L
        var proxy: Proxy? = null
        withClue("Insert Proxy and 2 Jumpservers. They should be inside the database") {
            val testProxy = Proxy("Testproxy")
            proxyId = proxyDataSource.insertProxy(testProxy)!!
            listOf(JumpServer(proxyId, 1), JumpServer(proxyId, 2)).forEach{
                proxyDataSource.insertJumpserver(it)
            }

            proxy = proxyDataSource.getProxy(proxyId)!!.apply {
                title shouldBe "Testproxy"
                jumpserverList.let {
                    it.size shouldBe 2
                    it.forOne { js -> js.order shouldBe 1 }
                    it.forOne { js -> js.order shouldBe 2 }
                }
            }
        }

        withClue("Rename the Proxy. It should still contain both Jumpservers") {
            proxyDataSource.insertProxy(proxy!!.copy(title = "NewProxy"))
            proxy = proxyDataSource.getProxy(proxyId)!!.apply {
                title shouldBe "NewProxy"
                jumpserverList.let {
                    it.size shouldBe 2
                    it.forOne { js -> js.order shouldBe 1 }
                    it.forOne { js -> js.order shouldBe 2 }
                }
            }
        }
    }
}