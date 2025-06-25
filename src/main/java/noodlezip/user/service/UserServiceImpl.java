package noodlezip.user.service;

import lombok.RequiredArgsConstructor;
import noodlezip.user.dto.UserDto;
import noodlezip.user.entity.ActiveStatus;
import noodlezip.user.entity.User;
import noodlezip.user.entity.UserType;
import noodlezip.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public void registUser(UserDto user) {

        User newUser = modelMapper.map(user, User.class);
        newUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        newUser.setUserType(UserType.NORMAL);
        newUser.setActiveStatus(ActiveStatus.ACTIVE);
        newUser.setIsEmailVerified(false);

        userRepository.save(newUser);
    }
}
