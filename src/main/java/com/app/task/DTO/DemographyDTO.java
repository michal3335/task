package com.app.task.DTO;

public class DemographyDTO {

    private String city;

    private String country;

    private Long population;

    public DemographyDTO(){}

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getPopulation() {
        return population;
    }

    public void setPopulation(Long population) {
        this.population = population;
    }

    @Override
    public String toString() {
        return  "{\"city\" : \"" + city + "\", " +
                "\"country\" : \"" + country + "\", " +
                "\"population\" : \"" + population + "\"" +
                '}';
    }
}
