package pt.ipp.estg.cmu.datastore

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pt.ipp.estg.cmu.datastore.utils.LocalDateConverter

@Database(entities = [UserModel::class,Exercise::class,Segments::class], version = 6)
@TypeConverters(LocalDateConverter::class)
abstract class FitnessAppRoomDatabase : RoomDatabase(){
    abstract fun fitnessAppDao(): FitnessAppDao
    companion object {
        /**
         * guarantees that the variable is always read and written in main memory (RAM),
         * preventing read and write operations from being cached.
         */
        @Volatile
        private var INSTANCE: FitnessAppRoomDatabase? = null

        fun getInstance(context: Context): FitnessAppRoomDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FitnessAppRoomDatabase::class.java,
                        "fitness_app_database"
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}
