package coroutine.lec05

import coroutine.lec01.printWithThread
import kotlinx.coroutines.*

fun main() {
    try {
        launch1()
    } catch (e: Exception) {
        e.printStackTrace()
        println("launch1 실행 결과 예외 발생")
    }

    try {
        launch2()
    } catch (e: Exception) {
        println("launch2 실행 결과 예외 발생")
    }

    try {
        async1()
    } catch (e: Exception) {
        e.printStackTrace()
        println("async1 실행 결과 예외 발생")
    }

    try {
        async2()
    } catch (e: Exception) {
        e.printStackTrace()
        println("launch2 실행 결과 예외 발생}")
    }

    try {
        async3()
    } catch (e: Exception) {
        e.printStackTrace()
        println("launch3 실행 결과 예외 발생")
    }

    exception()
}

fun exception(): Unit = runBlocking {

    val exceptionHandler = CoroutineExceptionHandler { context, throwable ->
        printWithThread("[$context] [$throwable] 예외 발생")
        throw throwable
    }

    CoroutineScope(Dispatchers.Default).launch(exceptionHandler) {
        printWithThread("exceptionHandler 실행")
        throw IllegalArgumentException("예외가 발생했다!")
    }

    delay(1000)
}

// 부모 자식 관계 상태에서 async 는 예외가 발생하면 부모에게 전파되어 에러를 출력한다.
// 하지만 부모에게 예외를 전파하고싶지 않은경우 SupervisorJob() 을 사용한다.
fun async3(): Unit = runBlocking {
    val job1 = async(SupervisorJob()) {
        printWithThread("async3 job 1")
        throw IllegalArgumentException()
    }
    delay(1000)
    job1.await()    //  없으면 예외 출력 X
}


// 부모 자식 관계가 아닌 코루틴에서는 async 의 경우 자식의 예외가 부모에게 전파되지 않기 때문에 예외가 출력되지 않는다.
// await() 을 통해 결과를 호출해야만 에러가 출력된다.
fun async2(): Unit = runBlocking {
    val job1 = CoroutineScope(Dispatchers.Default).async {
        printWithThread("async2 job 1")
        throw IllegalArgumentException()
    }
    delay(1000)
    job1.await()    //  없으면 예외 출력 X
}

// 부모 자식 관계 상태에서 async 는 예외가 발생하면 부모에게 전파되어 에러를 출력한다.
fun async1(): Unit = runBlocking {
    async {
        printWithThread("async1 job 1")
        throw IllegalArgumentException()
    }
    delay(1000)
    printWithThread("async1 end")
}

// 부모 자식 관계가 아닌 코루틴에서는 자식의 예외가 부모에게 전파되지 않기 때문에 예외가 발생한 코루틴에서 예외가 출력된다. (DefaultDispatcher-worker-1 @coroutine#2)
fun launch2(): Unit = runBlocking {
    val job1 = CoroutineScope(Dispatchers.Default).launch {
        printWithThread("launch2 job 1")
        throw IllegalArgumentException()
    }
    delay(1000)
    printWithThread("launch2 end")
}

// 부모 자식 관계의 코루틴에서 자식이 예외가 발생하면 부모에게 전파가 되어 에러를 출력한다. (main)
fun launch1(): Unit = runBlocking {
    val job1 = launch {
        printWithThread("launch1 job 1")
        throw IllegalArgumentException()
    }
    delay(1000)
    printWithThread("launch1 end")
}
