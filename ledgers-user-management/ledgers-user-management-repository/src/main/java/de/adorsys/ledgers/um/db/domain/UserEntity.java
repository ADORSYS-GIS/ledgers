/*
 * Copyright 2018-2018 adorsys GmbH & Co KG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.adorsys.ledgers.um.db.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "login", name = UserEntity.USER_LOGIN_UNIQUE),
        @UniqueConstraint(columnNames = "email", name = UserEntity.USER_EMAIL_UNIQUE)
})
public class UserEntity {
	
	public static final String USER_LOGIN_UNIQUE = "user_login_unique";
	public static final String USER_EMAIL_UNIQUE = "user_email_unique";

    @Id
    @Column(name = "user_id")
    private String id;

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String pin;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<ScaUserDataEntity> scaUserData = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<AccountAccess> accountAccesses = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="users_roles", joinColumns = @JoinColumn(name="user_id"))
    @Column(name="role")
    @Enumerated(EnumType.STRING)
    private Collection<UserRole> userRoles =  new ArrayList<>();

    private String branch;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public List<ScaUserDataEntity> getScaUserData() {
        return scaUserData;
    }

    public void setScaUserData(List<ScaUserDataEntity> scaUserData) {
        this.scaUserData = scaUserData;
    }

    public List<AccountAccess> getAccountAccesses() {
        return accountAccesses;
    }

    public void setAccountAccesses(List<AccountAccess> accountAccesses) {
        this.accountAccesses = accountAccesses;
    }

    public Collection<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Collection<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserEntity)) {
            return false;
        }
        UserEntity that = (UserEntity) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getLogin(), that.getLogin()) &&
                Objects.equals(getEmail(), that.getEmail()) &&
                Objects.equals(getPin(), that.getPin()) &&
                Objects.equals(getScaUserData(), that.getScaUserData()) &&
                Objects.equals(getAccountAccesses(), that.getAccountAccesses()) &&
                Objects.equals(getUserRoles(), that.getUserRoles()) &&
                Objects.equals(getBranch(), that.getBranch());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLogin(), getEmail(), getPin(), getScaUserData(), getAccountAccesses(), getUserRoles(), getBranch());
    }
}