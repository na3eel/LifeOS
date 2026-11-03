package com.lifeos.android.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import android.content.Context
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LifeOSDaoTest {
    private lateinit var database: LifeOSDatabase
    private val dao get() = database.dao()

    @Before
    fun createDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext<Context>(),
            LifeOSDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDatabase() = database.close()

    @Test
    fun habitCanBeCreatedUpdatedAndDeleted() = runBlocking {
        val habit = HabitEntity(id = "habit-1", title = "Read", area = "Mind")
        dao.addHabit(habit)
        assertEquals(listOf(habit), dao.habits().first())

        dao.updateHabit(habit.copy(done = true))
        assertTrue(dao.habits().first().single().done)

        dao.deleteHabit(habit.copy(done = true))
        assertTrue(dao.habits().first().isEmpty())
    }

    @Test
    fun habitTargetAndRecurringWeekdaysPersist() = runBlocking {
        val habit = HabitEntity(
            id = "habit-target",
            title = "Walk",
            targetValue = 30.0,
            targetUnit = "minutes",
            scheduledWeekdays = "1,3,5"
        )
        dao.addHabit(habit)

        val saved = dao.habits().first().single()
        assertEquals(30.0, saved.targetValue, 0.0)
        assertEquals("minutes", saved.targetUnit)
        assertEquals(listOf(1, 3, 5), saved.scheduledWeekdays.split(",").map(String::toInt))
    }


    @Test
    fun habitCompletionIsScopedToLocalDateAndCanBeUndone() = runBlocking {
        dao.addHabit(HabitEntity(id = "habit-date", title = "Read"))
        dao.markHabitComplete(HabitCompletionEntity("habit-date", "2026-11-03", 100L))
        dao.markHabitComplete(HabitCompletionEntity("habit-date", "2026-11-03", 200L))

        assertEquals(1, dao.completionsForDate("2026-11-03").first().size)
        assertTrue(dao.completionsForDate("2026-11-04").first().isEmpty())

        dao.undoHabitCompletion("habit-date", "2026-11-03")
        assertTrue(dao.completionsForDate("2026-11-03").first().isEmpty())
    }

    @Test
    fun tasksAndGoalsPersistIndependently() = runBlocking {
        dao.addTask(TaskEntity(id = "task-1", title = "Plan week"))
        dao.addGoal(GoalEntity(id = "goal-1", title = "Run 5 km", progress = 25))

        assertEquals("Plan week", dao.tasks().first().single().title)
        assertEquals(25, dao.goals().first().single().progress)
    }

    @Test
    fun areasAreStoredAndOrdered() = runBlocking {
        dao.addArea(AreaEntity(id = "z", name = "Soul"))
        dao.addArea(AreaEntity(id = "a", name = "Body"))

        assertEquals(listOf("Body", "Soul"), dao.areas().first().map { it.name })
    }
}
