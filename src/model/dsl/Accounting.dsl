module Accounting {

 root Customer(id) {
   long           id;
   String         name;
   Profile        profile;
   List<Account>  ?accounts;
 }



 value Profile {
   String? email;
   String? phoneNumber;
 }



 value Account {
   String             IBAN;
   String             currency;
   List<Transaction>  transactions;
 }



 value Transaction {
   Double     inflow;
   Double     outflow;
   String     description;
   Timestamp  paymentOn;
 }

}
