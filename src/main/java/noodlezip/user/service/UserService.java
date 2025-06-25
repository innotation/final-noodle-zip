package noodlezip.user.service;

import noodlezip.user.entity.User;

import java.util.Map;

public interface UserService {
    Map<String, String> registUser(User user);
}
