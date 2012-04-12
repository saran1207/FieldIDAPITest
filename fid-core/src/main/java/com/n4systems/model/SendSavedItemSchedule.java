package com.n4systems.model;

import com.n4systems.model.common.SimpleFrequency;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.saveditem.SavedItem;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.util.DateHelper;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.IndexColumn;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name="send_saved_item_schedules")
public class SendSavedItemSchedule extends EntityWithTenant {

    @ManyToOne
    @JoinColumn(name="saved_item_id")
    private SavedItem savedItem;

    @Column(name="frequency", nullable=false)
    @Enumerated(EnumType.STRING)
    private SimpleFrequency frequency = SimpleFrequency.DAILY;

    // Hour on 24 hour clock, 0 = midnight
    @Column(name="hour_to_send", nullable = false)
    private Integer hourToSend = 0;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @Column(name="send_to_owner", nullable = false)
    private boolean sendToOwner = true;

    @Column(name="message")
    private String message;

    @Column(name="email", nullable=false, length=255)
    @ElementCollection(fetch= FetchType.EAGER)
    @IndexColumn(name="orderidx")
    @JoinTable(name="send_saved_item_schedules_emails", joinColumns = {@JoinColumn(name="send_saved_item_schedule_id")})
    private List<String> emailAddresses = new ArrayList<String>();

    public SavedItem getSavedItem() {
        return savedItem;
    }

    public void setSavedItem(SavedItem savedItem) {
        this.savedItem = savedItem;
    }

    public SimpleFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(SimpleFrequency frequency) {
        this.frequency = frequency;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isSendToOwner() {
        return sendToOwner;
    }

    public void setSendToOwner(boolean sendToOwner) {
        this.sendToOwner = sendToOwner;
    }

    public List<String> getEmailAddresses() {
        return emailAddresses;
    }

    public void setEmailAddresses(List<String> emailAddresses) {
        this.emailAddresses = emailAddresses;
    }

    public void clearBlankEmailAddresses() {
        for (Iterator<String> iter = emailAddresses.iterator(); iter.hasNext(); ) {
            if (StringUtils.isBlank(iter.next())) {
                iter.remove();
            }
        }
    }
    
    public String getEmailAddressesJoined() {
        return StringUtils.join(emailAddresses, ", ");
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        clearBlankEmailAddresses();
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();
        clearBlankEmailAddresses();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getHourToSend() {
        return hourToSend;
    }

    public void setHourToSend(Integer hourToSend) {
        this.hourToSend = hourToSend;
    }
    
    @Transient
    public Set<String> getAddressesToDeliverTo() {
        Set<String> addresses = new HashSet<String>(getEmailAddresses());
        if (sendToOwner) {
            addresses.add(user.getEmailAddress());
        }
        return addresses;
    }
    
    public boolean shouldRunNow(Date utcDateTimeOnTheHour) {
        Calendar localizedDate = Calendar.getInstance();
        localizedDate.setTime(DateHelper.localizeDate(utcDateTimeOnTheHour, user.getTimeZone()));

        return frequency.isSameDay(localizedDate.getTime()) && hourToSend == localizedDate.get(Calendar.HOUR_OF_DAY);
    }
    
}
