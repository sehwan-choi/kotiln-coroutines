package coroutine.continuation

import kotlinx.coroutines.delay

suspend fun main() {
    val service = UserServiceV2()
    println(service.findUser(1L, null))
}

interface Continuation {
    suspend fun resumeWith(data: Any?)
}

class UserServiceV2 {

    private val userProfileRepository = UserProfileRepositoryV2()
    private val userImageRepository = UserImageRepositoryV2()

    private abstract class FindUserContinuation() : Continuation {
        var label = 0
        var profile: ProfileV2? = null
        var image: ImageV2? = null
    }

    suspend fun findUser(userId: Long, continuation: Continuation?): UserDtoV2 {
        val sm = continuation as? FindUserContinuation ?: object: FindUserContinuation() {

            override suspend fun resumeWith(data: Any?) {
                when (label) {
                    0 -> {
                        profile = data as ProfileV2
                        label = 1
                    }
                    1 -> {
                        image = data as ImageV2
                        label = 2
                    }
                }
                findUser(userId, this)
            }
        }

        when(sm.label) {
            0 -> {
                println("유저를 가져오겠습니다")
                userProfileRepository.findProfile(userId, sm)
            }
            1 -> {
                println("이미지를 가져오겠습니다")
                userImageRepository.findImage(sm.profile!!, sm)
            }
        }

        return UserDtoV2(sm.profile!!, sm.image!!)
    }

}

data class UserDtoV2(
    val profile: ProfileV2,
    val image: ImageV2,
)


class UserProfileRepositoryV2 {
    suspend fun findProfile(userId: Long, continuation: Continuation) {
        delay(100L)
        continuation.resumeWith(ProfileV2())
    }
}

class ProfileV2

class UserImageRepositoryV2 {
    suspend fun findImage(profile: ProfileV2, continuation: Continuation) {
        delay(100L)
        continuation.resumeWith(ImageV2())
    }
}

class ImageV2