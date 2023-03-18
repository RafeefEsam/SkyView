package com.example.skyview.viewmodel.viewmodel

import FakeAlarmRepository
import androidx.annotation.VisibleForTesting
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.skyview.model.AlarmPojo
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.Is.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@RunWith(JUnit4::class)

class AlarmViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AlarmViewModel
    private lateinit var fakeRepository: FakeAlarmRepository

    @Before
    fun setup() {
        fakeRepository = FakeAlarmRepository()
        viewModel = AlarmViewModel(fakeRepository)
    }

    @Test
    fun getAllAlarms_returnListOfAlarms(): Unit = runBlockingTest {
        // Given
        val expectedData = listOf(
            AlarmPojo(1, true),
            AlarmPojo(2, true),
            AlarmPojo(3, false)
        )
        fakeRepository.setAlarms(expectedData)

        // When
        viewModel.getAllAlarms()

        // Then
        val actualData = viewModel.alarmData.value
        assertThat(actualData, `is`(expectedData))

    }

    @Test
    fun addAlarm_alarm_alarmAdded() = runBlocking {
        // Given
        val newAlarm = AlarmPojo(4, true)

        // When
        viewModel.addAlarm(newAlarm)

        // Then
        val actualData = viewModel.alarmData.getOrAwaitValue().get(0)
        assertThat(actualData, `is`(newAlarm))

    }

    @Test
    fun deleteAlarm_alarm_alarmDeleted() = runBlocking {
        // Given
        val alarmToDelete = AlarmPojo(1, true)
        fakeRepository.setAlarms(listOf(alarmToDelete, AlarmPojo(2, false)))

        // When
        viewModel.deleteAlarm(alarmToDelete)

        // Then
        val actualData = viewModel.alarmData.value
        assertTrue(actualData?.contains(alarmToDelete) ?: true)
    }

}


@VisibleForTesting(otherwise = VisibleForTesting.NONE)
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {}
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)

    try {
        afterObserve.invoke()

        // Don't wait indefinitely if the LiveData is not set.
        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }

    } finally {
        this.removeObserver(observer)
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}

