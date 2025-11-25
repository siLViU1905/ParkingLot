package org.example.parkinglot.ejb;

import jakarta.ejb.EJBException;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.example.parkinglot.common.CarDto;
import org.example.parkinglot.common.UserDto;
import org.example.parkinglot.entities.Car;
import org.example.parkinglot.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class UsersBean {
    private static final Logger LOG = Logger.getLogger(UsersBean.class.getName());

    @PersistenceContext
    EntityManager entityManager;

    private List<UserDto> copyUsersToDto(List<User> users) {
        List<UserDto> dtos = new ArrayList<>();

        for (User user: users)
            dtos.add(new UserDto(user.getId(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getUsername()));

        return dtos;
    }

    public List<UserDto> findAllUsers() {
        LOG.info("findAllUsers");

        try {
            TypedQuery<User> typedQuery = entityManager.createQuery("SELECT u FROM User u", User.class);

            List<User> users = typedQuery.getResultList();

            return copyUsersToDto(users);
        } catch (Exception e) {
            throw new EJBException(e);
        }
    }
}
