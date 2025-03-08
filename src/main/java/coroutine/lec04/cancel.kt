package coroutine.lec04

import coroutine.lec01.printWithThread
import kotlinx.coroutines.*

/** 코루틴을 취소하는 방법 2-----------------------------------------------------
 * isActive 를 통해 코루틴 스스로 본인의 상태를 확인해 취소 요청을 받았으면, CancellationException 던지기
 */

// delay, yield 도 job.cancel 이 발상하면 CancellationException 을 발생시키는데 try ~ catch 로 잡아버리면 취소가 되지않는다.
fun main(): Unit = runBlocking {
    val job = launch {
        try {
            delay(1000)
        } catch (e: CancellationException) {
            printWithThread("CancellationException 발생! -> 무시됩니다.")
            // ignore..
        }

        printWithThread("delay 에 의해 취소되지 않았다")
    }

    delay(100)
    printWithThread("취소 시작")
    job.cancel()
}

// Dispatchers.Default 를 사용하여 main thread 가 아닌 다른 thread 에서 실행하도록 한다.
fun lec04Example3(): Unit = runBlocking {
    val job = launch(Dispatchers.Default) {
        var i = 1
        var nextPrintTime = System.currentTimeMillis()
        while (i <= 5) {
            if (nextPrintTime <= System.currentTimeMillis()) {
                printWithThread("${i++}번째 출력!")
                nextPrintTime += 1000L
            }

            if (!isActive) {
                throw CancellationException()
            }
        }
    }

    delay(100)
    job.cancel()
}


/** 코루틴을 취소하는 방법 1-----------------------------------------------------
 * 코루틴을 취소하려면 코루틴에서 delay, yield 와 같은 suspend 함수를 사용해야한다.
 */

// 아래는 코루틴 함수에서 delay, yield 를 사용하지 않아 cancel에 실패한다.
fun lec04Example2(): Unit = runBlocking {
    val job = launch {
        var i = 1
        var nextPrintTime = System.currentTimeMillis()
        while (i <= 5) {
            if (nextPrintTime <= System.currentTimeMillis()) {
                printWithThread("${i++}번째 출력!")
                nextPrintTime += 1000L
            }
        }
    }

    delay(100)
    job.cancel()
}

// 아래는 코루틴 함수에서 delay 를 사용하여 cancel이 성공한다.
fun lec04Example1(): Unit = runBlocking {
    val job1 = launch {
        printWithThread("Job 1 start")
        delay(1000)
        printWithThread("Job 1 end")
    }

    val job2 = launch {
        printWithThread("Job 2 start")
        delay(1000)
        printWithThread("Job 2 end")
    }

    printWithThread("delay start")
    delay(100)
    printWithThread("delay end")
    job1.cancel()
}
// 코루틴을 취소하는 방법 1-----------------------------------------------------