package com.lifeos.android.data

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey val id: String,
    val title: String,
    val note: String = "",
    val area: String = "Mind",
    val done: Boolean = false,
    val targetValue: Double = 1.0,
    val targetUnit: String = "",
    val trackingType: String = "BOOLEAN",
    val scheduledWeekdays: String = "1,2,3,4,5,6,7"
)

@Entity(
    tableName = "habit_completions",
    primaryKeys = ["habitId", "localDate"],
    indices = [Index("localDate")]
)
data class HabitCompletionEntity(
    val habitId: String,
    val localDate: String,
    val completedAtEpochMillis: Long
)

@Entity(tableName = "life_areas")
data class AreaEntity(@PrimaryKey val id: String, val name: String, val note: String = "")

@Entity(tableName = "tasks")
data class TaskEntity(@PrimaryKey val id: String, val title: String, val done: Boolean = false)

@Entity(tableName = "goals")
data class GoalEntity(@PrimaryKey val id: String, val title: String, val progress: Int = 0)

@Dao
interface LifeOSDao {
    @Query("SELECT * FROM habits ORDER BY title") fun habits(): Flow<List<HabitEntity>>
    @Insert suspend fun addHabit(x: HabitEntity)
    @Update suspend fun updateHabit(x: HabitEntity)
    @Delete suspend fun deleteHabit(x: HabitEntity)
    @Query("SELECT * FROM habit_completions WHERE localDate = :localDate")
    fun completionsForDate(localDate: String): Flow<List<HabitCompletionEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun markHabitComplete(completion: HabitCompletionEntity)
    @Query("DELETE FROM habit_completions WHERE habitId = :habitId AND localDate = :localDate")
    suspend fun undoHabitCompletion(habitId: String, localDate: String)
    @Query("SELECT * FROM life_areas ORDER BY name") fun areas(): Flow<List<AreaEntity>>
    @Insert suspend fun addArea(x: AreaEntity)
    @Query("SELECT * FROM tasks ORDER BY title") fun tasks(): Flow<List<TaskEntity>>
    @Insert suspend fun addTask(x: TaskEntity)
    @Update suspend fun updateTask(x: TaskEntity)
    @Delete suspend fun deleteTask(x: TaskEntity)
    @Query("SELECT * FROM goals ORDER BY title") fun goals(): Flow<List<GoalEntity>>
    @Insert suspend fun addGoal(x: GoalEntity)
    @Update suspend fun updateGoal(x: GoalEntity)
}

@Database(entities = [HabitEntity::class, HabitCompletionEntity::class, AreaEntity::class, TaskEntity::class, GoalEntity::class], version = 3, exportSchema = false)
abstract class LifeOSDatabase : RoomDatabase() {
    abstract fun dao(): LifeOSDao

    companion object {
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE habits ADD COLUMN targetValue REAL NOT NULL DEFAULT 1.0")
                db.execSQL("ALTER TABLE habits ADD COLUMN targetUnit TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE habits ADD COLUMN trackingType TEXT NOT NULL DEFAULT 'BOOLEAN'")
                db.execSQL("ALTER TABLE habits ADD COLUMN scheduledWeekdays TEXT NOT NULL DEFAULT '1,2,3,4,5,6,7'")
            }
        }
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS habit_completions (habitId TEXT NOT NULL, localDate TEXT NOT NULL, completedAtEpochMillis INTEGER NOT NULL, PRIMARY KEY(habitId, localDate))"
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_habit_completions_localDate ON habit_completions(localDate)")
                // Migrate the legacy single completion flag to today's local completion only.
                db.execSQL(
                    "INSERT OR IGNORE INTO habit_completions (habitId, localDate, completedAtEpochMillis) SELECT id, date('now','localtime'), CAST(strftime('%s','now') AS INTEGER) * 1000 FROM habits WHERE done = 1"
                )
            }
        }

        @Volatile private var INSTANCE: LifeOSDatabase? = null

        fun get(c: Context) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(c.applicationContext, LifeOSDatabase::class.java, "lifeos.db")
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .build()
                .also { INSTANCE = it }
        }
    }
}