package com.aws.lambda.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Response implements Serializable {

    @JsonProperty("addressList")
    private List<Address> addressList;

    @JsonCreator
    public Response(List<Address> addressList) {
        this.addressList = addressList;
    }

    public Collection<Address> getAddressList() {
        return addressList;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Response)) return false;

        Response response = (Response) obj;

        return Objects.equals(addressList, response.addressList);
    }

    @Override
    public int hashCode() {
        return addressList != null ? addressList.hashCode() : 0;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Response.class.getSimpleName() + "[", "]")
                .add("addressList=" + addressList)
                .toString();
    }
}
