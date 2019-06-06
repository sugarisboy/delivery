package dev.muskrat.delivery.dao.order;

public class OrderStatus {

    private static final Integer PROCCESING = 0;
    private static final Integer ASSEMBLY = 1;
    private static final Integer DELIVERY = 2;

    private static final Integer DONE = 10;
    private static final Integer CANCEL = 11;

    public static Integer byId(Integer id) {
        if (id == null)
            return null;

        switch (id) {
            case 0:
                return PROCCESING;
            case 1:
                return ASSEMBLY;
            case 2:
                return DELIVERY;
            case 10:
                return DONE;
            case 11:
                return CANCEL;
            default:
                return null;
        }
    }
}
