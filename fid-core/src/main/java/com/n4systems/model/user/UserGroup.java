package com.n4systems.model.user;

import com.n4systems.model.api.Listable;
import com.n4systems.model.parents.ArchivableEntityWithTenant;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name="user_groups")
@Cacheable
@org.hibernate.annotations.Cache(region = "SetupDataCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserGroup extends ArchivableEntityWithTenant implements Listable<Long>, Assignable {

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "group_id", length = 255)
    private String groupId;

    @ManyToMany
    @JoinTable(name = "users_user_groups", joinColumns = @JoinColumn(name="user_group_id"), inverseJoinColumns = @JoinColumn(name="user_id"))
    @org.hibernate.annotations.Cache(region = "SetupDataCache-Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Collection<User> members = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Collection<User> getMembers() {
        return members;
    }

    public void setMembers(Collection<User> members) {
        this.members = members;
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    public String getAssignToDisplayName() {
        return getDisplayName();
    }

    public String getKeyForStruts() {
        return "G"+getId();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UserGroup)) {
            return false;
        }
        return getId() != null && getId().equals(((UserGroup) obj).getId());
    }
}
