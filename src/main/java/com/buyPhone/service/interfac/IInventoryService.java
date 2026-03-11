package com.buyPhone.service.interfac;

import java.util.UUID;

public interface IInventoryService {

        void decreaseStock(UUID productId, int quantity);

        void increaseStock(UUID productId, int quantity);


}
