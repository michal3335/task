package com.app.task.Repository;

import com.app.task.Model.Demography;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepo extends CrudRepository<Demography, Long> {


    @Query("SELECT a.country FROM Demography a where a.city = :city")
    String checkCountry(@Param("city") String city);
}