package co.nxtgrid.api;

import java.util.Map;

public class ServiceInfo {

    private String name;
    private String version;
    private String description;
    private Map<String, String> endpoints;

    public ServiceInfo() {
    }

    public ServiceInfo(String name, String version, String description, Map<String, String> endpoints) {
        this.name = name;
        this.version = version;
        this.description = description;
        this.endpoints = endpoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, String> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(Map<String, String> endpoints) {
        this.endpoints = endpoints;
    }
}
