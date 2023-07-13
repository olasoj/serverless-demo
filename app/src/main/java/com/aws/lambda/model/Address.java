package com.aws.lambda.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonDeserialize(using = AddressJsonDeserializer.class)
@JsonSerialize(using = AddressJsonSerializer.class)
public class Address implements Serializable {

    @JsonProperty("streetName")
    private String streetName;

    @JsonProperty("city")
    private String city;

    @JsonProperty("state")
    private String state;

    @JsonProperty("country")
    private String country;

    public Address() {
    }

    @JsonCreator
    public Address(
            @JsonProperty("streetName") String streetName,
            @JsonProperty("city") String city,
            @JsonProperty("state") String state,
            @JsonProperty("country") String country) {
        this.streetName = streetName;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    public Address(AddressBuilder addressBuilder) {
        if (addressBuilder == null) {
            throw new IllegalArgumentException("Address builder cannot be null");
        }

        this.streetName = addressBuilder.streetName;
        this.city = addressBuilder.city;
        this.state = addressBuilder.state;
        this.country = addressBuilder.country;
    }

    public static AddressBuilder builder() {
        return new AddressBuilder();
    }

    public String getStreetName() {
        return streetName;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Address)) return false;

        Address address = (Address) obj;

        if (!Objects.equals(streetName, address.streetName)) return false;
        if (!Objects.equals(city, address.city)) return false;
        if (!Objects.equals(state, address.state)) return false;
        return Objects.equals(country, address.country);
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (streetName != null ? streetName.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Address.class.getSimpleName() + "[", "]")
                .add("streetName='" + streetName + "'")
                .add("city='" + city + "'")
                .add("state='" + state + "'")
                .add("postalCode='" + country + "'")
                .toString();
    }

    public static class AddressBuilder {
        private String streetName;
        private String city;
        private String state;
        private String country;

        public AddressBuilder setStreetName(String streetName) {
            this.streetName = streetName;
            return this;
        }


        public AddressBuilder setCity(String city) {
            this.city = city;
            return this;
        }

        public AddressBuilder setState(String state) {
            this.state = state;
            return this;
        }

        public AddressBuilder setCountry(String country) {
            this.country = country;
            return this;
        }

        public Address build() {
            return new Address(this);
        }
    }
}
