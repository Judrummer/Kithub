package com.github.judrummer.kithub

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith


class Mathematic {
    var callCount = 0
    val sum = { a: Int, b: Int ->
        callCount++
        a + b
    }
}

@RunWith(JUnitPlatform::class)
class HelloSpec : Spek({
    describe("HelloSpec") {

        var math = Mathematic()

        beforeEachTest {
            println("beforeEach")
            math = Mathematic()
        }

        beforeGroup {
            println("beforeGroup")
        }


        on("1+1") {
            println("1+1")
            val result = math.sum(1, 1)
            it("equal 2") {
                println("equal 2")
                assert(result == 2)
            }
        }

        on("2+3") {
            println("2+3")
            val result = math.sum(2, 3)
            it("equal 5") {
                println("equal 5")
                assert(result == 5)
            }
        }
    }
})
