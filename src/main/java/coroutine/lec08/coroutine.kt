package coroutine.lec08

import coroutine.lec01.printWithThread
import kotlinx.coroutines.*

fun main(): Unit = runBlocking {
    printWithThread("coroutineScopeAsync START")
    printWithThread(coroutineScopeAsync())
    printWithThread("coroutineScopeAsync END")
    println()

    printWithThread("withContextAsync START")
    printWithThread(withContextAsync())
    printWithThread("withContextAsync END")
    println()

    printWithThread("coroutineScopeLaunch START")
    printWithThread(coroutineScopeLaunch())
    printWithThread("coroutineScopeLaunch END")
    println()

    printWithThread("withContextLaunch START")
    printWithThread(withContextLaunch())
    printWithThread("withContextLaunch END")
    println()

    printWithThread("newCoroutineScopeLaunch START")
    printWithThread(newCoroutineScopeLaunch())
    printWithThread("newCoroutineScopeLaunch END")
    println()

    try {
        printWithThread("withTimeoutException START")
        printWithThread(withTimeoutException())
        printWithThread("withTimeoutException END")
    } catch (e: Exception) {
        printWithThread("withTimeoutException exception 발생!")
        e.printStackTrace()
    }
    println()

    printWithThread("withTimeoutNoException START")
    printWithThread(withTimeoutNoException())
    printWithThread("withTimeoutNoException END")
    println()

    printWithThread("withTimeoutOrNullTimeout START")
    printWithThread(withTimeoutOrNullTimeout())
    printWithThread("withTimeoutOrNullTimeout END")
    println()

    printWithThread("withTimeoutOrNullNoTimeout START")
    printWithThread(withTimeoutOrNullNoTimeout())
    printWithThread("withTimeoutOrNullNoTimeout END")
    println()
}

// withTimeoutOrNull 는 시간을 설정할 수 있으며 시간이 넘어가게 되면 null 을 리턴한다..
suspend fun withTimeoutOrNullNoTimeout(): Int? = withTimeoutOrNull(1000) {
    delay(500)
    10 + 20
}

suspend fun withTimeoutOrNullTimeout(): Int? = withTimeoutOrNull(1000) {
    delay(1500)
    10 + 20
}

// withTimeout 는 시간을 설정할 수 있으며 시간이 넘어가게 되면 TimeoutCancellationException 을 발생시킨다.
suspend fun withTimeoutNoException(): Int = withTimeout(1000) {
    delay(500)
    10 + 20
}

suspend fun withTimeoutException(): Int = withTimeout(1000) {
    delay(1500)
    10 + 20
}

// CoroutineScope(Dispatchers.Default) 로 생성하게 되면 코드를 즉시 실행시키지 않는다.
suspend fun newCoroutineScopeLaunch(): Job = CoroutineScope(Dispatchers.Default).launch {
    val job1 = launch {
        delay(1000)
    }
    printWithThread("newCoroutineScopeLaunch Job End")
}

// coroutineScope 는 현재 루트 코루틴의 자식 코루틴을 만들고 즉시 실행시킨다.
// withContext 는 현재 코루틴의 context 만 변경하고(자식 코루틴 X) 즉시 실행시킨다.
// 둘다 자기자신의 코루틴이 끝나야지 다음 코드를 실행한다.
suspend fun coroutineScopeLaunch(): Unit = coroutineScope {
    val job1 = launch {
        delay(1000)
    }
    printWithThread("coroutineScopeLaunch Job End")
}

suspend fun withContextLaunch(): Unit = withContext(Dispatchers.Default) {
    val job1 = launch {
        delay(1000)
    }
    printWithThread("withContextLaunch Job End")
}
suspend fun coroutineScopeAsync(): Int = coroutineScope {
    val job1 = async {
        delay(1000)
        10
    }

    val result = job1.await()
    printWithThread("coroutineScopeAsync Job End")
    result
}

suspend fun withContextAsync(): Int = withContext(Dispatchers.Default) {
    val job1 = async {
        delay(1000)
        10
    }

    val result = job1.await()
    printWithThread("withContextAsync Job End")
    result
}