package store;

import controller.OrderController;

public class Application {
    public static void main(String[] args) {
        OrderController orderController = new OrderController();
        orderController.run();
    }
}
