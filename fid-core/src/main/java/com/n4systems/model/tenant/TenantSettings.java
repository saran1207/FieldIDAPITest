package com.n4systems.model.tenant;

import com.google.common.collect.Lists;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.security.AccountPolicy;
import com.n4systems.model.security.KeyPair;
import com.n4systems.model.security.PasswordPolicy;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("serial")
@Entity
@Table(name = "tenant_settings")
@Cacheable
@org.hibernate.annotations.Cache(region = "SetupDataCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TenantSettings extends EntityWithTenant {
	private boolean secondaryOrgsEnabled;
	
	@Embedded
	private UserLimits userLimits = new UserLimits();
	
	@Embedded
	private AccountPolicy accountPolicy = new AccountPolicy();
	
	@Embedded
	private PasswordPolicy passwordPolicy = new PasswordPolicy();
	
	private Boolean gpsCapture = false;

	private String supportUrl;
    
    private String logoutUrl;

    @Column(name="inspections_enabled", nullable = false)
    private Boolean inspectionsEnabled = false;

    @Column(name="loto_enabled", nullable = false)
    private Boolean lotoEnabled = false;

    @Column(name="language", nullable=false)
    @ElementCollection(fetch= FetchType.EAGER)
    @JoinTable(name="tenant_languages", joinColumns = @JoinColumn(name = "tenant_settings_id"))
    @OrderColumn(name="orderidx")
    // NOTE : this does NOT include the default language.  just the ones that are translated to.
    private List<Locale> translatedLanguages = Lists.newArrayList();

    @Column(name="default_language")
    private Locale defaultLanguage;

	@Column(name="public_api_enabled")
	private boolean publicApiEnabled;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "key", column = @Column(name = "consumer_key")),
		@AttributeOverride(name = "secret", column = @Column(name = "consumer_secret"))
	})
	private KeyPair authConsumer = new KeyPair();

	@Override
	protected void onUpdate() {
		super.onUpdate();
		generateOAuthTokens();
	}

	@Override
	protected void onCreate() {
		super.onCreate();
		generateOAuthTokens();
	}

	private void generateOAuthTokens() {
		if (!authConsumer.isSet()) {
			authConsumer = KeyPair.generate();
		}
	}

    public boolean isSecondaryOrgsEnabled() {
		return secondaryOrgsEnabled;
	}

	public void setSecondaryOrgsEnabled(boolean secondaryOrgsEnabled) {
		this.secondaryOrgsEnabled = secondaryOrgsEnabled;
	}

	public UserLimits getUserLimits() {
		return userLimits;
	}

	public void setUserLimits(UserLimits userLimits) {
		this.userLimits = userLimits;
	}

	public void setAccountPolicy(AccountPolicy accountPolicy) {
		this.accountPolicy = accountPolicy;
	}

	public AccountPolicy getAccountPolicy() {
		return accountPolicy;
	}

	public void setPasswordPolicy(PasswordPolicy passwordPolicy) {
		this.passwordPolicy = passwordPolicy;
	}

	public PasswordPolicy getPasswordPolicy() {
		return passwordPolicy;
	}

	public Boolean isGpsCapture() {
		return gpsCapture;
	}

	public void setGpsCapture(Boolean gpsCapture) {
		this.gpsCapture = gpsCapture;
	}

	public void setSupportUrl(String supportUrl) {
		this.supportUrl = supportUrl;
	}

	public String getSupportUrl() {
		return supportUrl;
	}

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    public List<Locale> getTranslatedLanguages() {
        return translatedLanguages;
    }

    public void setTranslatedLanguages(List<Locale> translatedLanguages) {
        this.translatedLanguages = translatedLanguages;
    }

    public Locale getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(Locale defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public Boolean isInspectionsEnabled() {
        return inspectionsEnabled;
    }

    public void setInspectionsEnabled(Boolean inspectionsEnabled) {
        this.inspectionsEnabled = inspectionsEnabled;
    }

    public Boolean isLotoEnabled() {
        return lotoEnabled;
    }

    public void setLotoEnabled(Boolean lotoEnabled) {
        this.lotoEnabled = lotoEnabled;
    }

	public Boolean getGpsCapture() {
		return gpsCapture;
	}

	public Boolean getInspectionsEnabled() {
		return inspectionsEnabled;
	}

	public Boolean getLotoEnabled() {
		return lotoEnabled;
	}

	public boolean isPublicApiEnabled() {
		return publicApiEnabled;
	}

	public void setPublicApiEnabled(boolean publicApiEnabled) {
		this.publicApiEnabled = publicApiEnabled;
	}

	public KeyPair getAuthConsumer() {
		return authConsumer;
	}

	public void setAuthConsumer(KeyPair authConsumer) {
		this.authConsumer = authConsumer;
	}
}
