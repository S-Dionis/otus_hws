package ru.dio.atm;

public class ATMExceptions {

    public static class NotEnoughBanknotes extends Exception {

        @Override
        public String getMessage() {
            return "Cell is out of money";
        }
    }

    public static class WithdrawalException extends Exception {

        final String message;

        public WithdrawalException(String message) {
            this.message = message;
        }

        @Override
        public String getMessage() {
            return message;
        }
    }
}
