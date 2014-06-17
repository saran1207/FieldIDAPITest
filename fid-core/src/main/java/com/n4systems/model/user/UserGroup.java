package com.n4systems.model.user;

import com.n4systems.model.api.Listable;
import com.n4systems.model.parents.ArchivableEntityWithTenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name="user_groups")
public class UserGroup extends ArchivableEntityWithTenant implements Listable<Long>, Assignable {

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "group_id", length = 255)
    private String groupId;

    @ManyToMany(mappedBy = "groups")
    private Collection<User> members = new ArrayList<User>();

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
