package coroutine.lec03

import coroutine.lec01.printWithThread
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

fun main():Unit = runBlocking {
    val time = measureTimeMillis {
        example1()
        example2()
        example3()
        example4()
    }
    printWithThread("소요 시간 : $time ms")
}

fun example4(): Unit = runBlocking {
    val job1= launch {
        delay(1000)
        printWithThread("[example4]Job 1")
    }

    val job2 = launch {
        delay(1000)
        printWithThread("[example4]Job 2")
    }
}

fun example3(): Unit = runBlocking {
    val job = launch {
        (1..5).forEach {
            printWithThread("[example3] $it")
            delay(500)
        }
    }

    delay(1000)
    job.cancel()
}

fun example2(): Unit = runBlocking {
    val job = launch(start = CoroutineStart.LAZY) {
        printWithThread("[example2]Hello launch")
    }

    delay(1000)
    job.start()
}

fun example1() {
    runBlocking {
        printWithThread("[example1]START")
        launch {
            delay(2_000L)
            printWithThread("[example1]STOP")
        }
        printWithThread("[example1]zz")
    }

    printWithThread("[example1]END")
}