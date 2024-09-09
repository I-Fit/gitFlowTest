//package kr.co.ifit.api.service;
//
//import kr.co.ifit.db.entity.User;
//import kr.co.ifit.db.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//
//@Service
//@RequiredArgsConstructor
////  사용자 정보를 로드
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//    @Override
//    //  로그인 ID를 통해 사용자를 검색하고, UserDetails를 반환
//    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
//        User user = userRepository.findByUserName(userName);
//        if (user == null) {
//            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다." + userName);
//        }
//        return new org.springframework.security.core.userdetails.User(
//                user.getUserName(),
//                user.getPassword(),
//                new ArrayList<>()
//        );
//    }
//}
