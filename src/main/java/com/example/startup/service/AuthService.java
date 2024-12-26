package com.example.startup.service;

import com.example.startup.entity.User;
import com.example.startup.entity.enums.UserRole;
import com.example.startup.payload.ApiResponse;
import com.example.startup.payload.auth.Login;
import com.example.startup.payload.auth.Register;
import com.example.startup.repository.UserRepository;
import com.example.startup.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtProvider jwtProvider;
    private final EmailSenderService emailSenderService;

    public ApiResponse register(Register register, UserRole role){
        if (userRepository.existsByEmail(register.email())){
            return new ApiResponse("Foydalanuvchi allaqachon mavjud!",null);
        }
        Integer code = generateCode();
        User user = User.builder()
                .fullname(register.fullname())
                .email(register.email())
                .password(encoder.encode(register.password()))
                .role(role)
                .code(code)
                .blocked(true)
                .build();
        emailSenderService.sendEmail(
                register.email(),
                "Tasdiqlash kodingiz",
                "Hurmatli " + register.email() +
                        "\n" +
                        "Tasdiqlash jarayonini yakunlash uchun quyidagi koddan foydalaning:\n" +
                        "\n" +
                        "KOD: \n" + code +
                        "\n" +
                        "Ushbu kod faqat sizga tegishli va xavfsizlikni ta'minlash uchun boshqalar bilan bo'lishmang. Kod 5 daqiqa ichida o'z kuchini yo'qotadi.\n" +
                        "\n" +
                        "Agar siz ushbu so'rovni amalga oshirmagan bo'lsangiz, darhol biz bilan bog'laning: support@codot.uz\n" +
                        "\n" +
                        "Rahmat,  \n" +
                        "Codot jamoasi.\n"
        );
        userRepository.save(user);
        return new ApiResponse("Tasdiqlash uchun emailga kod yuborildi!",null);
    }

    public ApiResponse checkCode(Integer code){
        User user = userRepository.findByCode(code);
        if (user == null){
            return new ApiResponse("Noto'g'ri kod kiritildi!",null);
        }
        user.setCode(null);
        user.setBlocked(false);
        userRepository.save(user);
        return new ApiResponse("Faollashtirildi!",null);
    }

    public ApiResponse login(Login login) {
        User user = userRepository.findByEmail(login.email());
        if (user == null || user.isBlocked() || !encoder.matches(login.password(), user.getPassword())) {
            return new ApiResponse("Foydalanuvchi topilmadi yoki parol xato!",null);
        }
        String token = jwtProvider.generateToken(login.email());
        return new ApiResponse(token,null);
    }

    private Integer generateCode(){
        Random random = new Random();
        return 100000 + random.nextInt(900000);
    }

}
