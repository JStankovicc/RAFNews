package com.raf.rafnews.repository.user;

import com.raf.rafnews.entities.User;

import java.util.List;

public interface UserRepository {
    List<User> allUsers();
    List<User> allUsersPaginated(int offset, int perPage);
    int countAllUsers();
    User addUser(User user);
    User findUser(String email);
    void deleteUser(String email);
    void changeUserActivity(String email);
    User updateUser(User user, String email);
    User findUserById(Integer id);
    User updateUserById(User user, Integer id);
}
