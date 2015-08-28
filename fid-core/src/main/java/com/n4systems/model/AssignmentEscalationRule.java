package com.n4systems.model;

import com.n4systems.model.location.Location;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.ArchivableEntityWithTenant;
import com.n4systems.model.user.Assignable;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import javolution.io.Struct;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is the JPA entity which represents rows in the assignment_escalation_rules table.
 *
 * Created by Jordan Heath on 2015-08-14.
 */
@Entity
@Table(name="assignment_escalation_rules")
public class AssignmentEscalationRule extends ArchivableEntityWithTenant {

    @Column(name = "rule_name")
    private String ruleName;

    @ManyToOne(optional=true)
    @JoinColumn(name = "escalate_to_user_id")
    private User escalateToUser;

    @ManyToOne(optional=true)
    @JoinColumn(name = "escalate_to_user_group_id")
    private UserGroup escalateToUserGroup;

    @Column(name = "notifyAssignee")
    private Boolean notifyAssignee = false;

    @ManyToOne(optional=true)
    @JoinColumn(name = "reassign_user_id")
    private User reassignUser;

    @ManyToOne(optional=true)
    @JoinColumn(name = "reassign_user_group_id")
    private UserGroup reassignUserGroup;

    @Column(name = "overdue_quantity")
    private Long overdueQuantity;

    @Column(name = "subject_text")
    private String subjectText;

    @Column(name = "custom_message_text")
    private String customMessageText;

    @Column(name = "additional_emails")
    private String additionalEmails; //This one actually gets broken apart into an array of Strings, but is held as a String.

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;

    @ManyToOne(optional = true)
    @JoinColumn(name = "event_type_group_id")
    private EventTypeGroup eventTypeGroup;

    @ManyToOne(optional = true)
    @JoinColumn(name = "event_type_id")
    private EventType eventType;

    @ManyToOne(optional = true)
    @JoinColumn(name = "asset_status_id")
    private AssetStatus assetStatus;

    @ManyToOne(optional = true)
    @JoinColumn(name = "asset_type_id")
    private AssetType assetType;

    @ManyToOne(optional = true)
    @JoinColumn(name = "asset_type_group_id")
    private AssetTypeGroup assetTypeGroup;

    @ManyToOne(optional = true)
    @JoinColumn(name = "assigned_to_id")
    private User assignedTo;

    @ManyToOne(optional = true)
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @ManyToOne(optional = true)
    @JoinColumn(name = "owner_id")
    private BaseOrg owner;

    @Embedded
    private Location location;

    @Column(name = "freeform_location")
    private String freeformLocation;

    @Column(name = "rfid_number")
    private String rfidNumber;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "reference_number")
    private String referenceNumber;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "purchase_order")
    private String purchaseOrder;

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public User getEscalateToUser() {
        return escalateToUser;
    }

    public void setEscalateToUser(User escalateToUser) {
        this.escalateToUser = escalateToUser;
        this.escalateToUserGroup = null;
    }

    public Boolean getNotifyAssignee() {
        return notifyAssignee;
    }

    public void setNotifyAssignee(Boolean notifyAssignee) {
        this.notifyAssignee = notifyAssignee;
    }

    public User getReassignUser() {
        return reassignUser;
    }

    public void setReassignUser(User reassignUser) {
        this.reassignUser = reassignUser;
        this.reassignUserGroup = null;
    }

    public Long getOverdueQuantity() {
        return overdueQuantity;
    }

    public void setOverdueQuantity(Long overdueQuantity) {
        this.overdueQuantity = overdueQuantity;
    }

    public String getSubjectText() {
        return subjectText;
    }

    public void setSubjectText(String subjectText) {
        this.subjectText = subjectText;
    }

    public String getCustomMessageText() {
        return customMessageText;
    }

    public void setCustomMessageText(String customMessageText) {
        this.customMessageText = customMessageText;
    }

    public String getAdditionalEmails() {
        return additionalEmails;
    }

    public void setAdditionalEmails(String additionalEmails) {
        this.additionalEmails = additionalEmails;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public EventTypeGroup getEventTypeGroup() {
        return eventTypeGroup;
    }

    public void setEventTypeGroup(EventTypeGroup eventTypeGroup) {
        this.eventTypeGroup = eventTypeGroup;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public AssetStatus getAssetStatus() {
        return assetStatus;
    }

    public void setAssetStatus(AssetStatus assetStatus) {
        this.assetStatus = assetStatus;
    }

    public AssetType getAssetType() {
        return assetType;
    }

    public void setAssetType(AssetType assetType) {
        this.assetType = assetType;
    }

    public AssetTypeGroup getAssetTypeGroup() {
        return assetTypeGroup;
    }

    public void setAssetTypeGroup(AssetTypeGroup assetTypeGroup) {
        this.assetTypeGroup = assetTypeGroup;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public BaseOrg getOwner() {
        return owner;
    }

    public void setOwner(BaseOrg owner) {
        this.owner = owner;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getFreeformLocation() {
        return freeformLocation;
    }

    public void setFreeformLocation(String freeformLocation) {
        this.freeformLocation = freeformLocation;
    }

    public String getRfidNumber() {
        return rfidNumber;
    }

    public void setRfidNumber(String rfidNumber) {
        this.rfidNumber = rfidNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(String purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public UserGroup getEscalateToUserGroup() {
        return escalateToUserGroup;
    }

    public void setEscalateToUserGroup(UserGroup escalateToUserGroup) {
        this.escalateToUserGroup = escalateToUserGroup;
        this.escalateToUser = null;
    }

    @Transient
    public Assignable getEscalateUserOrGroup() {
        return escalateToUser != null ?  escalateToUser : escalateToUserGroup;
    }

    public void setEscalateUserOrGroup(Assignable assignee) {
        if (assignee instanceof User) {
            setEscalateToUser((User) assignee);
        } else if (assignee instanceof UserGroup) {
            setEscalateToUserGroup((UserGroup) assignee);
        } else if (assignee == null) {
            this.escalateToUser = null;
            this.escalateToUserGroup = null;
        }
    }

    public UserGroup getReassignToUserGroup() {
        return reassignUserGroup;
    }

    public void setReassignToUserGroup(UserGroup escalateToUserGroup) {
        this.reassignUserGroup = escalateToUserGroup;
        this.reassignUser = null;
    }

    @Transient
    public Assignable getReassignUserOrGroup() {
        return reassignUser != null ?  reassignUser : reassignUserGroup;
    }

    public void setReassignUserOrGroup(Assignable assignee) {
        if (assignee instanceof User) {
            setReassignUser((User) assignee);
        } else if (assignee instanceof UserGroup) {
            setReassignToUserGroup((UserGroup) assignee);
        } else if (assignee == null) {
            this.reassignUser = null;
            this.reassignUserGroup = null;
        }
    }

    public List<String> getAdditionalEmailsList() {
        if(additionalEmails != null) {
            //If additional emails isn't null, we'll process a list.  Otherwise, we're going to provide null, indicating
            //that there's nothing in the field.  Pretty simpe.
            if (!additionalEmails.isEmpty()) {
                return new ArrayList<>(Arrays.asList(additionalEmails.split("\\|")));
            } else {
                return new ArrayList<>();
            }
        }

        return null;
    }

    public void addAdditionalEmail(String email) {
        if(additionalEmails == null) {
            additionalEmails = "";
            additionalEmails += email;
        } else {
            if (!additionalEmails.endsWith("|")) {
                additionalEmails += "|";
            }
            additionalEmails += email + "|";
        }
    }

    public boolean removeEmail(String email) {
        List<String> emailList = Arrays.asList(additionalEmails.split("\\|"));
        boolean result = emailList.remove(email);

        additionalEmails = "";
        emailList.forEach(emailAddress -> additionalEmails += emailAddress + "|");

        return result;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public enum Type {
        EVENT, ACTION
    }

    public boolean requiresThingEvent() {
        return assetStatus != null || assetType != null || assetTypeGroup != null || rfidNumber != null || serialNumber != null || purchaseOrder != null || referenceNumber != null || freeformLocation != null || location != null || assignedTo != null;
    }
}
