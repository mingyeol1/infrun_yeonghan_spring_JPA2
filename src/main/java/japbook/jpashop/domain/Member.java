package japbook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String name;

    @Embedded //내장타입을 포함을 했다.(매핑)
    private Address address;

    @JsonIgnore //양방향 연관관계시 한쪽은 이그노어 해줘야함.
    @OneToMany(mappedBy = "member") //누구에 의해서 매핑이 되었나? order테이블에 있는 member에 대해.
    private List<Order> orders = new ArrayList<>();



}
