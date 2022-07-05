package service.impl;

import dao.User;
import service.UserService;
import java.util.Random;
import java.util.UUID;

public class UserServiceImpl implements UserService {

    public User getUserById(Integer id) {

        System.out.println("客户端查询了"+id+"的用户");

        // 模拟从数据库中取用户的行为
        Random random = new Random();
        User user = User.builder().name(UUID.randomUUID().toString())
                .id(id)
                .sex(random.nextBoolean()).build();
        return user;
    }
}
