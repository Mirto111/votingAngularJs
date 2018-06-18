package myProject.voting.repository.datajpa;

import myProject.voting.model.Role;
import myProject.voting.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)

public class CrudUserRepositoryImplTest {

    @Autowired
    CrudUserRepository crudUserRepository;

    @Test
    public void save() {
        User user = new User(null, "Test@ya.ru", "test", "test", Role.ROLE_USER, Role.ROLE_ADMIN);
        crudUserRepository.save(user);

    }


    @Test
    public void get() {
        User user = crudUserRepository.getByEmail("user@yandex.ru");
        crudUserRepository.getOne(1);
        Assert.assertEquals(UserTestData.USER, user);
    }

    @Test
    public void getAll() {
    }

    @Test
    public void getByEmail() {

        User user = crudUserRepository.getByEmail("user@yandex.ru");
        Assert.assertEquals(UserTestData.USER, user);
    }

    @Test
    public void delete() {
        crudUserRepository.delete(1);
        Assert.assertTrue(crudUserRepository.delete(1)!=0);
    }
}