package japbook.jpashop.domain.item;

import jakarta.persistence.*;
import japbook.jpashop.domain.Category;
import japbook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)   //타입이 여러가지 있음 JOINED 가장 정규화된 스타일, SINGLE_TABLE : 한 테이블에 다 때려박음 TABLE_PER_CLASS : BOOK, MOVIE, ALBUM 3개의 테이블만 나오는 전략
@DiscriminatorColumn(name = "dtype")
@Getter
@Setter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //==비즈니스 로직==//
    //재고 수량을 증가하는 로직.
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity){
        int restStock = this.stockQuantity -=quantity;
        if (restStock < 0){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }

}
