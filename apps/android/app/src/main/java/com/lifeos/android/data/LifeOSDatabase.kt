package com.lifeos.android.data
import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName="habits")
data class HabitEntity(@PrimaryKey val id:String, val title:String, val note:String="", val area:String="Mind", val done:Boolean=false)
@Entity(tableName="life_areas")
data class AreaEntity(@PrimaryKey val id:String, val name:String, val note:String="")
@Entity(tableName="tasks")
data class TaskEntity(@PrimaryKey val id:String, val title:String, val done:Boolean=false)
@Entity(tableName="goals")
data class GoalEntity(@PrimaryKey val id:String, val title:String, val progress:Int=0)

@Dao interface LifeOSDao {
 @Query("SELECT * FROM habits ORDER BY title") fun habits():Flow<List<HabitEntity>>
 @Insert suspend fun addHabit(x:HabitEntity)
 @Update suspend fun updateHabit(x:HabitEntity)
 @Delete suspend fun deleteHabit(x:HabitEntity)
 @Query("SELECT * FROM life_areas ORDER BY name") fun areas():Flow<List<AreaEntity>>
 @Insert suspend fun addArea(x:AreaEntity)
 @Query("SELECT * FROM tasks ORDER BY title") fun tasks():Flow<List<TaskEntity>>
 @Insert suspend fun addTask(x:TaskEntity)
 @Update suspend fun updateTask(x:TaskEntity)
 @Delete suspend fun deleteTask(x:TaskEntity)
 @Query("SELECT * FROM goals ORDER BY title") fun goals():Flow<List<GoalEntity>>
 @Insert suspend fun addGoal(x:GoalEntity)
 @Update suspend fun updateGoal(x:GoalEntity)
}
@Database(entities=[HabitEntity::class,AreaEntity::class,TaskEntity::class,GoalEntity::class],version=1,exportSchema=false)
abstract class LifeOSDatabase:RoomDatabase(){
 abstract fun dao():LifeOSDao
 companion object{
  @Volatile private var INSTANCE:LifeOSDatabase?=null
  fun get(c:Context)=INSTANCE?: synchronized(this){INSTANCE?:Room.databaseBuilder(c.applicationContext,LifeOSDatabase::class.java,"lifeos.db").build().also{INSTANCE=it}}
 }
}