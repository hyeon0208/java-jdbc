package com.techcourse.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.jdbc.core.JdbcTemplate;
import com.techcourse.config.DataSourceConfig;
import com.techcourse.domain.User;
import com.techcourse.support.DataBaseCleaner;
import com.techcourse.support.jdbc.init.DatabasePopulatorUtils;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserDaoTest {

    private JdbcTemplate jdbcTemplate;
    private DataBaseCleaner dataBaseCleaner;
    private UserDao userDao;

    @BeforeEach
    void setup() {
        DataSource dataSource = DataSourceConfig.getInstance();
        DatabasePopulatorUtils.execute(dataSource);

        jdbcTemplate = new JdbcTemplate(dataSource);
        dataBaseCleaner = new DataBaseCleaner(jdbcTemplate);
        dataBaseCleaner.cleanUp("users");
        userDao = new UserDao(jdbcTemplate);

        final var user = new User("gugu", "password", "hkkang@woowahan.com");
        userDao.insert(user);
    }

    @DisplayName("모든 User를 조회한다.")
    @Test
    void findAll() {
        final var users = userDao.findAll();

        assertThat(users).isNotEmpty();
    }

    @DisplayName("id로 User를 조회한다.")
    @Test
    void findById() {
        final var user = userDao.findById(1L);

        assertThat(user.getAccount()).isEqualTo("gugu");
    }

    @DisplayName("account로 User를 조회한다.")
    @Test
    void findByAccount() {
        final var account = "gugu";
        final var user = userDao.findByAccount(account);

        assertThat(user.getAccount()).isEqualTo(account);
    }

    @DisplayName("User를 저장한다.")
    @Test
    void insert() {
        final var account = "insert-gugu";
        final var user = new User(account, "password", "hkkang@woowahan.com");
        userDao.insert(user);

        final var actual = userDao.findById(2L);

        assertThat(actual.getAccount()).isEqualTo(account);
    }

    @DisplayName("id와 일치하는 User의 비밀번호를 저장한다.")
    @Test
    void update() {
        final var newPassword = "password99";
        final var user = userDao.findById(1L);
        user.changePassword(newPassword);

        userDao.update(user);

        final var actual = userDao.findById(1L);

        assertThat(actual.getPassword()).isEqualTo(newPassword);
    }
}
