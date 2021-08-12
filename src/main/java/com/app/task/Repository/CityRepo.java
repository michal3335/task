package com.app.task.Repository;

import com.app.task.Model.City;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepo extends CrudRepository<City, Long> {


    @Query("SELECT a.country FROM City a where a.city = :city")
    String checkCountry(@Param("city") String city);
}