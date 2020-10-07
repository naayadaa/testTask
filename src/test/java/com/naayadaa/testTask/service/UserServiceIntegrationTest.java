package com.naayadaa.testTask.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.naayadaa.testTask.dto.CreateUserResponse;
import com.naayadaa.testTask.dto.UserMapper;
import com.naayadaa.testTask.dto.UserRequest;
import com.naayadaa.testTask.dto.UserResponse;
import com.naayadaa.testTask.exception.BadRequestError;
import com.naayadaa.testTask.exception.NotFoundError;
import com.naayadaa.testTask.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {UserServiceIntegrationTest.Config.class})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@DatabaseSetup(value = "classpath:empty.xml")
@ActiveProfiles("h2")
public class UserServiceIntegrationTest {

    private UserService userService;

    @Autowired
    private UserRepository repository;

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Before
    public void setUp() {
        userService = new UserServiceImpl(repository, Mappers.getMapper(UserMapper.class));
    }

    @Test
    @DatabaseSetup("classpath:set_up.xml")
    public void shouldGetUser() throws Exception {
        UserResponse userResponse = userService.get("2e3953c3-c7f0-47ee-a86e-581aaffdb597");
        assertEquals(userResponse.getId(),"2e3953c3-c7f0-47ee-a86e-581aaffdb597");
        assertEquals(userResponse.getLogin(), "teddy_bear");
        assertEquals(userResponse.getFullName(), "Ted Mosby");
        assertEquals(userResponse.getBirthDate(), formatter.parse("1978-04-25"));
    }

    @Test
    @DatabaseSetup(value = "classpath:set_up.xml")
    @ExpectedDatabase(value = "classpath:user_created_expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void shouldCreateUser() {
        UserRequest userRequest = stubUserRequest();
        CreateUserResponse response = userService.create(userRequest);
        assertThat(response.getId(), notNullValue());
    }

    @Test(expected = BadRequestError.class)
    @DatabaseSetup("classpath:set_up.xml")
    @ExpectedDatabase(value = "classpath:set_up.xml")
    public void shouldNotCreateUserWithUnavailableLogin() {
        UserRequest userRequest = stubUserRequest();
        userRequest.setLogin("teddy_bear");
        userService.create(userRequest);
    }

    @Test
    @DatabaseSetup("classpath:set_up.xml")
    @ExpectedDatabase(value = "classpath:user_updated_expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void shouldUpdateUser() throws Exception {
        UserRequest userRequest = stubUserRequest();
        UserResponse userResponse = userService.update("2e3953c3-c7f0-47ee-a86e-581aaffdb597", userRequest);
        assertEquals(userResponse.getId(),"2e3953c3-c7f0-47ee-a86e-581aaffdb597");
        assertEquals(userResponse.getLogin(), userRequest.getLogin());
        assertEquals(userResponse.getFullName(), userRequest.getFullName());
        assertEquals(userResponse.getBirthDate(), userRequest.getBirthDate());
    }

    @Test(expected = BadRequestError.class)
    @DatabaseSetup("classpath:set_up.xml")
    @ExpectedDatabase(value = "classpath:set_up.xml")
    public void shouldNotUpdateUserWithUnavailableLogin() {
        UserRequest userRequest = stubUserRequest();
        userRequest.setLogin("robin_bobin");
        userService.update("2e3953c3-c7f0-47ee-a86e-581aaffdb597", userRequest);
    }

    @Test(expected = NotFoundError.class)
    @DatabaseSetup("classpath:set_up.xml")
    @ExpectedDatabase(value = "classpath:set_up.xml")
    public void shouldThrowExceptionWhenUpdateNotExistedUser() {
        UserRequest userRequest = stubUserRequest();
        userRequest.setLogin("new_login");
        userService.update("user_does_not_exist", userRequest);
    }

    private UserRequest stubUserRequest() {

        try {
            UserRequest userRequest = new UserRequest();
            userRequest.setLogin("sweetie");
            userRequest.setFullName("Barney Stinson");
            userRequest.setBirthDate(formatter.parse("1976-02-01"));

            return userRequest;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @TestConfiguration
    @EnableAutoConfiguration
    @EntityScan(basePackages = "com.naayadaa.testTask.domain")
    @EnableJpaRepositories(basePackages = "com.naayadaa.testTask.repository")
    public static class Config {

    }
}