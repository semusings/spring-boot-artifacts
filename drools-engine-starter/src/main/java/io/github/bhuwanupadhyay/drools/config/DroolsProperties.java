package io.github.bhuwanupadhyay.drools.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Objects;

@ConfigurationProperties(prefix = "boot.drools")
public class DroolsProperties {

    private String rulesPath = "drools/rules/";

    public String getRulesPath() {
        return rulesPath;
    }

    public void setRulesPath(String rulesPath) {
        this.rulesPath = rulesPath;
    }

    @Override
    public String toString() {
        return "DroolsProperties{" +
                "rulesPath='" + rulesPath + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DroolsProperties that = (DroolsProperties) o;
        return Objects.equals(rulesPath, that.rulesPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rulesPath);
    }
}
