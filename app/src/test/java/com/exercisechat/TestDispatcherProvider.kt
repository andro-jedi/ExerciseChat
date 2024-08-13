package com.exercisechat

import com.exercisechat.domain.DispatchersProvider
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher

class TestDispatchersProvider(
    val testDispatcher: TestDispatcher = StandardTestDispatcher()
) : DispatchersProvider {

    override val default = testDispatcher
    override val io = testDispatcher
    override val main = testDispatcher
    override val unconfined = testDispatcher
}
