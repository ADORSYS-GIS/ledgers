package de.adorsys.ledgers.um.converter;

import de.adorsys.ledgers.um.api.domain.UserBO;
import de.adorsys.ledgers.um.db.domain.UserEntity;
import de.adorsys.ledgers.um.impl.converter.UserConverter;
import org.junit.Test;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class UserConverterTest {
    public static final String USER_ID = "someID";
    public static final String USER_EMAIL = "spe@adorsys.com.ua";
    public static final String USER_LOGIN = "speex";
    public static final String USER_PIN = "1234567890";

    UserConverter converter = Mappers.getMapper(UserConverter.class);

    @Test
    public void toUserBOList() {
        //empty list case
        List<UserEntity> users = Collections.emptyList();
        List<UserBO> result = converter.toUserBOList(users);
        assertThat(result, is(Collections.emptyList()));

        //user = null
        users = Collections.singletonList(null);
        result = converter.toUserBOList(users);
        assertThat(result, is(Collections.singletonList(new UserBO())));
    }

    @Test
    public void toUserEntityList() {
        //empty list case
        List<UserBO> users = Collections.emptyList();
        List<UserEntity> result = converter.toUserEntityList(users);
        assertThat(result, is(Collections.emptyList()));

        //user = null
        users = Collections.singletonList(null);
        result = converter.toUserEntityList(users);
        assertThat(result, is(Collections.singletonList(new UserEntity())));
    }

    @Test
    public void toScaUserDataBO() {

    }

    @Test
    public void toScaUserDataEntity() {

    }

    @Test
    public void toScaUserDataListBO() {

    }

    @Test
    public void toScaUserDataListEntity() {

    }

    @Test
    public void toAccountAccessBO() {

    }

    @Test
    public void toAccountAccessEntity() {

    }

    @Test
    public void toAccountAccessListBO() {

    }

    @Test
    public void toAccountAccessListEntity() {

    }

    @Test
    public void toUserRoleBO() {

    }

    @Test
    public void toUserRole() {

    }

    @Test
    public void toUserBO() {
        UserBO bo = converter.toUserBO(buildUserPO());

        assertThat(bo.getId(), is(USER_ID));
        assertThat(bo.getEmail(), is(USER_EMAIL));
        assertThat(bo.getLogin(), is(USER_LOGIN));
        assertThat(bo.getPin(), is(USER_PIN));
    }

    @Test
    public void toUserPO() {

        UserEntity po = converter.toUserPO(buildUserBO());

        assertThat(po.getId(), is(USER_ID));
        assertThat(po.getEmail(), is(USER_EMAIL));
        assertThat(po.getLogin(), is(USER_LOGIN));
        assertThat(po.getPin(), is(USER_PIN));
    }

    //todo: @spe replace by json source file
    private UserBO buildUserBO() {
        UserBO bo = new UserBO();
        bo.setId(USER_ID);
        bo.setEmail(USER_EMAIL);
        bo.setLogin(USER_LOGIN);
        bo.setPin(USER_PIN);
        return bo;
    }

    //todo: @spe replace by json source file
    private UserEntity buildUserPO() {
        UserEntity entity = new UserEntity();
        entity.setId(USER_ID);
        entity.setEmail(USER_EMAIL);
        entity.setLogin(USER_LOGIN);
        entity.setPin(USER_PIN);
        return entity;
    }
}