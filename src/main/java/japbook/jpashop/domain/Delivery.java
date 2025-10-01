package japbook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @JsonIgnore
    @OneToOne(mappedBy = "delivery",fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING) //ORDINAL enum의 가장 기본 타입임 이걸로 하면 중간에 다른 값이 들어오면 순서가 바뀌어서 에러가 날 수 있음 , STRING은 순서에 대해 밀리는게 없어서 사용.
    private DeliveryStatus deliveryStatus; //READY , COMP
}
