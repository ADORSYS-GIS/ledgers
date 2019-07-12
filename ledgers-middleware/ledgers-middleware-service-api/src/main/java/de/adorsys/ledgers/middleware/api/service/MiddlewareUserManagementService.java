package de.adorsys.ledgers.middleware.api.service;

import de.adorsys.ledgers.middleware.api.domain.sca.ScaInfoTO;
import de.adorsys.ledgers.middleware.api.domain.um.AccountAccessTO;
import de.adorsys.ledgers.middleware.api.domain.um.ScaUserDataTO;
import de.adorsys.ledgers.middleware.api.domain.um.UserRoleTO;
import de.adorsys.ledgers.middleware.api.domain.um.UserTO;

import java.util.List;

public interface MiddlewareUserManagementService {
    /**
     * Creates a new user
     *
     * @param user User transfer object
     * @return A persisted user
     */
    UserTO create(UserTO user);

    /**
     * Finds a User by its identifier
     *
     * @param id User identifier
     * @return a User
     */
    UserTO findById(String id);

    /**
     * Finds user by login
     *
     * @param userLogin users login
     * @return UserTO object
     */
    UserTO findByUserLogin(String userLogin);

    /**
     * Update SCA methods by user login
     *
     * @param scaDataList user methods
     * @param userLogin   user login
     */
    UserTO updateScaData(String userLogin, List<ScaUserDataTO> scaDataList);

    /**
     * Adds new account for a specific User
     *
     * @param scaInfo container for TPP data from access token
     * @param userId  user id
     * @param access  Access to an account
     */
    void updateAccountAccess(ScaInfoTO scaInfo, String userId, AccountAccessTO access);

    /**
     * Loads paginated user collection
     *
     * @param page page number
     * @param size size of the page
     * @return list of users
     */
    List<UserTO> listUsers(int page, int size);


    /**
     * Loads list of users by branch and role
     *
     * @param roles user roles
     * @return list of users by branch and role
     */
    List<UserTO> getUsersByBranchAndRoles(String branch, List<UserRoleTO> roles);

    /**
     * Counts users by branch
     *
     * @param branch branch
     * @return amount of users
     */
    int countUsersByBranch(String branch);
}
