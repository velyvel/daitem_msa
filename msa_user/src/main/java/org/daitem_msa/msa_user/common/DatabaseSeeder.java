package org.daitem_msa.msa_user.common;

import lombok.RequiredArgsConstructor;
import org.daitem_msa.msa_user.entity.User;
import org.daitem_msa.msa_user.enumset.UserRoles;
import org.daitem_msa.msa_user.enumset.YN;
import org.daitem_msa.msa_user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        List<User> users = new ArrayList<>();
        long count = userRepository.count();
        if (count < 3) {
            String encodePassword = passwordEncoder.encode("0000");
            for (int i = 3; i <= 10003; i++) {
                User user = User.builder()
                        .loginId("iam_dummy" + i)
                        .email("iam_dummy" + i + "@gmail.com")
                        .password(encodePassword)
                        .userName(makeRandomName())
                        .userRole(UserRoles.NORMAL)
                        .address1("서울시")
                        .address2("강남구")
                        .address3("역삼동")
                        .phone("010-1234-5678")
                        .isValid(YN.Y)
                        .build();

                users.add(user);
                if (i % 100 == 0) {
                    userRepository.saveAll(users);
                    users.clear();
                }
            }
        }
    }

    public String makeRandomName(){
        StringBuilder sb = new StringBuilder();
        String[] lastNames = {"김","이","박","강","최","권","정"};
        String[] middleNames = {"현","원","민","희","진","유","수"};
        String[] fistNames = {"재","수","영","주","아","하"};

        // 난수 생성을 위한 Random 객체 생성
        Random random = new Random();

        // 성, 중간 이름, 마지막 이름을 각각의 배열에서 무작위로 선택
        String chosenLastName = lastNames[random.nextInt(lastNames.length)];
        String chosenMiddleName = middleNames[random.nextInt(middleNames.length)];
        String chosenFirstName = fistNames[random.nextInt(fistNames.length)];

        // StringBuilder 객체에 성과 이름 조합 추가
        sb.append(chosenLastName);
        sb.append(chosenMiddleName);
        sb.append(chosenFirstName);
        return sb.toString();
    }
}
