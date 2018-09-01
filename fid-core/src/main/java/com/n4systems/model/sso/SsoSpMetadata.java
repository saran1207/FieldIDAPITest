package com.n4systems.model.sso;

import com.n4systems.model.Tenant;

import javax.persistence.Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by agrabovskis on 2018-08-07.
 */
@Entity
@Table(name="sso_sp_metadata")
@NamedEntityGraph(
        name = "lazyCollections",
        attributeNodes = {
                @NamedAttributeNode("bindingsSLO")
        }
)
public class SsoSpMetadata implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @JoinColumn(name="sso_entity_id", nullable = false)
    @OneToOne
    private SsoEntity ssoEntity;

    // part of our implementation, not used by Spring SAML
    @JoinColumn(name="tenant_id", nullable = false)
    @OneToOne
    private Tenant tenant;

    // part of our implementation, not used by Spring SAML
    @Column(name = "match_user_id", nullable = false)
    private boolean matchOnUserId;
    @Column(name = "user_id_attribute_name")
    private String userIdAttributeName;

    // part of our implementation, not used by Spring SAML
    @Column(name = "match_email_address", nullable = false)
    private boolean matchOnEmailAddress;
    @Column(name = "email_address_attribute_name")
    private String emailAddressAttributeName;

    @Column(name="request_signed")
    private boolean requestSigned;
    @Column(name="want_assertion_signed")
    private boolean wantAssertionSigned;

    @ElementCollection
    @CollectionTable(name = "sso_sp_name_id", joinColumns = @JoinColumn(name = "id"))
    @Column(name="name_id")
    private Collection<String> NameID;

    @Column(name="entity_base_url")
    private String entityBaseURL;

    @Column(name="entity_alias")
    private String entityAlias;

    @ElementCollection
    @CollectionTable(name = "sso_sp_bindings_sso", joinColumns = @JoinColumn(name = "id"))
    @Column(name="bindings_sso")
    private Collection<String> bindingsSSO;

    @ElementCollection
    @CollectionTable(name = "sso_sp_bindings_hok_sso", joinColumns = @JoinColumn(name = "id"))
    @Column(name="bindings_hok_sso")
    private Collection<String> bindingsHoKSSO;

    @ElementCollection
    @CollectionTable(name = "sso_sp_bindings_slo", joinColumns = @JoinColumn(name = "id"))
    @Column(name="bingings_slo")
    private Collection<String> bindingsSLO;

    @Column(name="include_discovery_extension")
    private boolean includeDiscoveryExtension;

    @Column(name="assertion_consumer_index")
    private int assertionConsumerIndex;

    @Column(name="signing_key")
    private String signingKey;

    @Column(name="encryption_key")
    private String encryptionKey;

    @Column(name="tls_key")
    private String tlsKey;

    @Column(name = "idp_discovery_enabled")
    private boolean idpDiscoveryEnabled;

    @Column(name = "idp_discovery_url")
    private String idpDiscoveryURL;

    @Column(name="idp_discovery_response_url")
    private String idpDiscoveryResponseURL;

    @Column(name = "alias")
    private String alias;

    @Column(name="security_profile")
    private String securityProfile;

    @Column(name="ssl_security_profile")
    private String sslSecurityProfile;

    @Column(name= "require_logout_request_signed")
    private boolean requireLogoutRequestSigned;

    @Column(name = "require_logout_response_signed")
    private boolean requireLogoutResponseSigned;

    @Column(name = "require_artifact_resolve_signed")
    private boolean requireArtifactResolveSigned;

    @Column(name = "ssl_hostname_verification")
    private String sslHostnameVerification;

    @Column(name = "sign_metadata")
    private boolean signMetadata;

    @Column(name = "signing_algorithm")
    private String signingAlgorithm;

    @Column(name = "local")
    private boolean local;

    @Column(name = "serialized_metadata", length=65000)
    private String serializedMetadata;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SsoEntity getSsoEntity() {
        return ssoEntity;
    }

    public void setSsoEntity(SsoEntity entityName) {
        this.ssoEntity = entityName;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public boolean isMatchOnUserId() {
        return matchOnUserId;
    }

    public void setMatchOnUserId(boolean matchOnUserid) {
        this.matchOnUserId = matchOnUserid;
    }

    public String getUserIdAttributeName() {
        return userIdAttributeName;
    }

    public void setUserIdAttributeName(String userIdAttributeName) {
        this.userIdAttributeName = userIdAttributeName;
    }

    public boolean isMatchOnEmailAddress() {
        return matchOnEmailAddress;
    }

    public void setMatchOnEmailAddress(boolean matchOnEmailAddress) {
        this.matchOnEmailAddress = matchOnEmailAddress;
    }

    public String getEmailAddressAttributeName() {
        return emailAddressAttributeName;
    }

    public void setEmailAddressAttributeName(String emailAddressAttributeName) {
        this.emailAddressAttributeName = emailAddressAttributeName;
    }

    public boolean isRequestSigned() {
        return requestSigned;
    }

    public void setRequestSigned(boolean requestSigned) {
        this.requestSigned = requestSigned;
    }

    public boolean isWantAssertionSigned() {
        return wantAssertionSigned;
    }

    public void setWantAssertionSigned(boolean wantAssertionSigned) {
        this.wantAssertionSigned = wantAssertionSigned;
    }

    public Collection<String> getNameID() {
        return NameID;
    }

    public void setNameID(Collection<String> nameID) {
        NameID = nameID;
    }

    public String getEntityBaseURL() {
        return entityBaseURL;
    }

    public void setEntityBaseURL(String entityBaseURL) {
        this.entityBaseURL = entityBaseURL;
    }

    public String getEntityAlias() {
        return entityAlias;
    }

    public void setEntityAlias(String entityAlias) {
        this.entityAlias = entityAlias;
    }

    public Collection<String> getBindingsSSO() {
        return bindingsSSO;
    }

    public void setBindingsSSO(Collection<String> bindingsSSO) {
        this.bindingsSSO = bindingsSSO;
    }

    public Collection<String> getBindingsHoKSSO() {
        return bindingsHoKSSO;
    }

    public void setBindingsHoKSSO(Collection<String> bindingsHoKSSO) {
        this.bindingsHoKSSO = bindingsHoKSSO;
    }

    public Collection<String> getBindingsSLO() {
        return bindingsSLO;
    }

    public void setBindingsSLO(Collection<String> bindingsSLO) {
        this.bindingsSLO = bindingsSLO;
    }

    public boolean isIncludeDiscoveryExtension() {
        return includeDiscoveryExtension;
    }

    public void setIncludeDiscoveryExtension(boolean includeDiscoveryExtension) {
        this.includeDiscoveryExtension = includeDiscoveryExtension;
    }

    public int getAssertionConsumerIndex() {
        return assertionConsumerIndex;
    }

    public void setAssertionConsumerIndex(int assertionConsumerIndex) {
        this.assertionConsumerIndex = assertionConsumerIndex;
    }

    public String getSigningKey() {
        return signingKey;
    }

    public void setSigningKey(String signingKey) {
        this.signingKey = signingKey;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public String getTlsKey() {
        return tlsKey;
    }

    public void setTlsKey(String tlsKey) {
        this.tlsKey = tlsKey;
    }

    public boolean isIdpDiscoveryEnabled() {
        return idpDiscoveryEnabled;
    }

    public void setIdpDiscoveryEnabled(boolean idpDiscoveryEnabled) {
        this.idpDiscoveryEnabled = idpDiscoveryEnabled;
    }

    public String getIdpDiscoveryURL() {
        return idpDiscoveryURL;
    }

    public void setIdpDiscoveryURL(String idpDiscoveryURL) {
        this.idpDiscoveryURL = idpDiscoveryURL;
    }

    public String getIdpDiscoveryResponseURL() {
        return idpDiscoveryResponseURL;
    }

    public void setIdpDiscoveryResponseURL(String idpDiscoveryResponseURL) {
        this.idpDiscoveryResponseURL = idpDiscoveryResponseURL;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getSecurityProfile() {
        return securityProfile;
    }

    public void setSecurityProfile(String securityProfile) {
        this.securityProfile = securityProfile;
    }

    public String getSslSecurityProfile() {
        return sslSecurityProfile;
    }

    public void setSslSecurityProfile(String sslSecurityProfile) {
        this.sslSecurityProfile = sslSecurityProfile;
    }

    public boolean isRequireLogoutRequestSigned() {
        return requireLogoutRequestSigned;
    }

    public void setRequireLogoutRequestSigned(boolean requireLogoutRequestSigned) {
        this.requireLogoutRequestSigned = requireLogoutRequestSigned;
    }

    public boolean isRequireLogoutResponseSigned() {
        return requireLogoutResponseSigned;
    }

    public void setRequireLogoutResponseSigned(boolean requireLogoutResponseSigned) {
        this.requireLogoutResponseSigned = requireLogoutResponseSigned;
    }

    public boolean isRequireArtifactResolveSigned() {
        return requireArtifactResolveSigned;
    }

    public void setRequireArtifactResolveSigned(boolean requireArtifactResolveSigned) {
        this.requireArtifactResolveSigned = requireArtifactResolveSigned;
    }

    public String getSslHostnameVerification() {
        return sslHostnameVerification;
    }

    public void setSslHostnameVerification(String sslHostnameVerification) {
        this.sslHostnameVerification = sslHostnameVerification;
    }

    public boolean isSignMetadata() {
        return signMetadata;
    }

    public void setSignMetadata(boolean signMetadata) {
        this.signMetadata = signMetadata;
    }

    public String getSigningAlgorithm() {
        return signingAlgorithm;
    }

    public void setSigningAlgorithm(String signingAlgorithm) {
        this.signingAlgorithm = signingAlgorithm;
    }

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    public String getSerializedMetadata() {
        return serializedMetadata;
    }

    public void setSerializedMetadata(String serializedMetadata) {
        this.serializedMetadata = serializedMetadata;
    }
}
