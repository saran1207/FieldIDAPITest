package com.n4systems.model.sso;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Global settings for SSO
 */
@Entity
@Table(name="sso_global_settings")
public class SsoGlobalSettings {

    @Id
    @Column(name="id")
    private Long id;

    @Column(name="max_authentication_age")
    private Long maxAuthenticationAge;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMaxAuthenticationAge() {
        return maxAuthenticationAge;
    }

    public void setMaxAuthenticationAge(Long maxAuthenticationAge) {
        this.maxAuthenticationAge = maxAuthenticationAge;
    }
}
