package com.quayintech.roomdatabase.data.factory;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.quayintech.roomdatabase.data.DataDAO;
import com.quayintech.roomdatabase.model.ModelData;



@Database(entities = {ModelData.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract DataDAO dataDAO();

}
