package coroutine.lec03

import coroutine.lec01.printWithThread
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

fun main():Unit = runBlocking {
    val time = measureTimeMillis {
        val job1 = async { apiCall3() }
        val job2 = async { apiCall4(job1.await()) }
        printWithThread(job2.await())
    }
    printWithThread("소요 시간 : $time ms")
}

suspend fun apiCall3(): Int {
    delay(1000)
    return 1
}

suspend fun apiCall4(num: Int): Int {
    delay(2000)
    return num + 2
}

fun example6():Unit = runBlocking {
    val time = measureTimeMillis {
        val job1 = async { apiCall1() }
        val job2 = async { apiCall2() }
        printWithThread(job1.await() + job2.await())
    }
    printWithThread("소요 시간 : $time ms")
}

suspend fun apiCall1(): Int {
    delay(1000)
    return 1
}

suspend fun apiCall2(): Int {
    delay(2000)
    return 3
}

fun example5() = runBlocking<Unit> {
    val job = async {
        3 + 5
    }
    printWithThread(job.await())
}