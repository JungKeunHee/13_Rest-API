package com.ohgiraffers.restapi.section02.responseentity;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/entity")
public class ResponseEntityController {

    /* comment.
    *   ResponseEntity 란?
    *   결과 데이터와 HTTP 상태 코드 를 직접 제어할 수 있는
    *   클래스이다.
    *   내부에 HttpStatus, HttpHeaders, HttpBody 를 포함한다.
    *  */

    private List<UserDTO> users;

    /* 임시 DB 에서 조회해온 값 설정 */
    public ResponseEntityController() {
        users = new ArrayList<>();

        users.add(new UserDTO(1, "user01", "pass01", "너구리1", LocalDate.now()));
        users.add(new UserDTO(2, "user02", "pass02", "너구리2", LocalDate.now()));
        users.add(new UserDTO(3, "user03", "pass03", "너구리3", LocalDate.now()));
    }

    @GetMapping("/users")
    public ResponseEntity<ResponseMessage> findAllUsers() {

        HttpHeaders headers = new HttpHeaders();
        // 응답 할 데이터의 양식 지정
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("users", users);

        ResponseMessage responseMessage = new ResponseMessage(200, "조회 성공", responseMap);


        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    @GetMapping("/users/{userNo}")
    public ResponseEntity<ResponseMessage> findUserByNo(@PathVariable int userNo) {

        HttpHeaders headers = new HttpHeaders();
        // 응답 할 데이터의 양식 지정
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        UserDTO foundUser =
                users.stream().filter(user -> user.getNo() == userNo).collect(Collectors.toList()).get(0);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("user", foundUser);

        return ResponseEntity.ok().headers(headers).body(new ResponseMessage(200, "조회성공", responseMap));
    }

    /* comment.
    *   form 태그로 데이터 전달 받는 것과
    *   javaScript 로 데이터 전달 받는 것이 다르다.
    *  */
    @PostMapping("/user/regist")
    public ResponseEntity<?> regist(@RequestBody UserDTO newUser) {

        System.out.println("Json 데이터 @RequestBody 로 들어오는지 확인 = " + newUser);

        // List 에 들어있는 마지막 no 가져오기
        int lastNo = users.get(users.size() - 1).getNo();
        newUser.setNo(lastNo + 1);

        return ResponseEntity
                // 201 상태코드 -> 등록 관련(자원 생성 관련) 상태코드
                .created(URI.create("/entity/users/" + users.get(users.size() - 1).getNo())).build();
    }

    /* 수정 */
    @PutMapping("/users/{userNo}")
    public ResponseEntity<?> modifyUser(@PathVariable int userNo, @RequestBody UserDTO modifyInfo) {

        UserDTO foundUser = users.stream().filter(user -> user.getNo() == userNo)
                .collect(Collectors.toList()).get(0);

        // id, pwd, name 수정하기
        foundUser.setId(modifyInfo.getId());
        foundUser.setPwd(modifyInfo.getPwd());
        foundUser.setName(modifyInfo.getName());

        return ResponseEntity.created(URI.create("/entity/users/" + userNo)).build();
    }

    @DeleteMapping("/users/{userNo}")
    public ResponseEntity<?> removeUser(@PathVariable int userNo) {

        // userNo 1명 특정
        UserDTO foundUser = users.stream().filter(user -> user.getNo() == userNo)
                .collect(Collectors.toList()).get(0);

        // 특정한 유저 객체 배열에서 삭제
        users.remove(foundUser);

        // 자원 삭제 관련 noContent
        return ResponseEntity.noContent().build();
    }
}

