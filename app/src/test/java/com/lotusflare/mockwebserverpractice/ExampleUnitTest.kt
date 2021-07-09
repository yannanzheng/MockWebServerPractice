package com.lotusflare.mockwebserverpractice

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Throws(Exception::class)
    @Test
    fun test() {
        // Create a MockWebServer. These are lean enough that you can create a new
        // instance for every unit test.
        val server = MockWebServer()

        // Schedule some responses.
//        val response1 = MockResponse().apply {
//            addHeader("Content-Type", "application/json; charset=utf-8")
//            addHeader("Cache-Control", "no-cache")
//            setBody("hello, world!")
//        }
        server.enqueue(MockResponse().setBody("hello, world!"))
        server.enqueue(MockResponse().setBody("sup, bra?"))
        server.enqueue(MockResponse().setBody("yo dog"))

        // Start the server.
        server.start()

        // Ask the server for its URL. You'll need this to make HTTP requests.
        val baseUrl = server.url("/v1/chat/")

        // Exercise your application code, which should make those HTTP requests.
        // Responses are returned in the same order that they are enqueued.
        val chat = Chat(baseUrl)
        chat.loadMore()
        assertEquals("hello, world!", chat.messages())
        chat.loadMore()
        chat.loadMore()
        assertEquals(
            """
            hello, world!
            sup, bra?
            yo dog
            """.trimIndent(), chat.messages()
        )

        // Optional: confirm that your app made the HTTP requests you were expecting.
        val request1 = server.takeRequest()
        assertEquals("/v1/chat/messages/", request1.path)
        assertEquals("{}",request1.body.readUtf8())
        assertNotNull(request1.getHeader("Authorization"))
        val request2 = server.takeRequest()
        assertEquals("/v1/chat/messages/2", request2.path)
        val request3 = server.takeRequest()
        assertEquals("/v1/chat/messages/3", request3.path)

        // Shut down the server. Instances cannot be reused.
        server.shutdown()
    }

}