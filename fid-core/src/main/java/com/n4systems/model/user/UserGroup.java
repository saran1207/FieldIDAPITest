package com.n4systems.model.user;

import com.n4systems.model.parents.ArchivableEntityWithTenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name="user_groups")
public class UserGroup extends ArchivableEntityWithTenant {

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "group_id", length = 255)
    private String groupId;

    @OneToMany(mappedBy = "group")
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
}
