package com.quayintech.roomdatabase.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.quayintech.roomdatabase.model.ModelData;

@Dao
public interface DataDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertBarang(ModelData modelData);

    @Update
    int updateBarang(ModelData modelData);

    @Delete
    int deleteBarang(ModelData modelData);

    @Query("SELECT * FROM tbarang")
    ModelData[] selectAllBarangs();

    @Query("SELECT * FROM tbarang WHERE id = :id LIMIT 1")
    ModelData selectBarangDetail(int id);
}
