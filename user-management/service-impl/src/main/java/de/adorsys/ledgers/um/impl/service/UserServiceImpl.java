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

package de.adorsys.ledgers.um.impl.service;

import de.adorsys.ledgers.um.api.domain.AccountAccessBO;
import de.adorsys.ledgers.um.api.domain.UserBO;
import de.adorsys.ledgers.um.api.exception.UserAlreadyExistsException;
import de.adorsys.ledgers.um.api.exception.UserNotFoundException;
import de.adorsys.ledgers.um.api.service.UserService;
import de.adorsys.ledgers.um.db.domain.UserEntity;
import de.adorsys.ledgers.um.db.repository.UserRepository;
import de.adorsys.ledgers.um.impl.converter.UserConverter;
import de.adorsys.ledgers.util.MD5Util;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserConverter userConverter) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    @Override
    public UserBO create(UserBO user) throws UserAlreadyExistsException {
        UserEntity userPO = userConverter.toUserPO(user);

        if (userRepository.existsById(userPO.getId())) {
            throw new UserAlreadyExistsException(user);
        }

        userPO.setPin(MD5Util.encode(user.getPin()));

        return userConverter.toUserBO(userRepository.save(userPO));
    }

    @Override
    public boolean authorize(String login, String pin) throws UserNotFoundException {
        UserEntity user = getUser(login);
        return MD5Util.verify(pin, user.getPin());
    }

    @Override
    public boolean authorize(String login, String pin, String accountId) throws UserNotFoundException {
        UserEntity user = getUser(login);
        //        long count = user.getAccounts().stream().filter(a -> a.getId().equals(accountId)).count();
//        return pinVerified && count > 0;
        return MD5Util.verify(pin, user.getPin());
    }

//    @Override
//    public void addAccount(String login, LedgerAccount account) throws UserNotFoundException {
//        UserPO user = getUser(login);
//        List<LedgerAccount> accounts = user.getAccounts();
//        accounts.add(account);
//        userRepository.save(user);
//    }

    @Override
    public UserBO findById(String id) throws UserNotFoundException {
        Optional<UserEntity> userPO = userRepository.findById(id);
        userPO.orElseThrow(() -> new UserNotFoundException("User with id=" + id + " was not found"));
        return userConverter.toUserBO(userPO.get());
    }

    @Override
    public List<AccountAccessBO> getAccountAccess(String userId) throws UserNotFoundException {
        Optional<UserEntity> user = userRepository.findById(userId);
        user.orElseThrow(() -> new UserNotFoundException("User with id=" + userId + " was not found"));
        UserBO userBO = userConverter.toUserBO(user.get());
        return userBO.getAccountAccesses();
    }

    @NotNull
    private UserEntity getUser(String login) throws UserNotFoundException {
        Optional<UserEntity> userOptional = userRepository.findFirstByLogin(login);
        userOptional.orElseThrow(() -> userNotFoundException(login));
        return userOptional.get();
    }

    @NotNull
    private UserNotFoundException userNotFoundException(String login) {
        return new UserNotFoundException(String.format("User with login %s was not found", login));
    }
}
